#!/usr/bin/env python3
"""Offline, stdlib-only publication guard. Reports locations, never matched values.

Exit 0: no blocking findings; 1: blocking or unreadable scope. No file mutations.
Credential stores are blocked by filename and never opened, including Git blobs.
"""
from __future__ import annotations
import argparse
import collections
import hashlib
import ipaddress
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import zipfile
import io

HOME = re.compile(r'(?i)(?:[a-z]:[/\\]Users[/\\]|/(?:Users|home)/)(?!<)[\w.-]+')
ABSOLUTE = re.compile(r'(?i)(?<![a-z0-9])(?:[a-z]:[/\\]+(?=[\w-])|/(?:Users|home)/(?=[\w-]))')
EMAIL = re.compile(r'(?<![\w.+-])[\w.+-]+@[\w.-]+\.[a-zA-Z]{2,}\b')
UUID = re.compile(r'\b[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}\b')
IP = re.compile(r'(?<![\w.])(?:\d{1,3}\.){3}\d{1,3}(?![\w.])')
SECRET = re.compile(r'-----BEGIN (?:[A-Z ]+ )?PRIVATE KEY(?: BLOCK)?-----|\bgh[pousr]_[A-Za-z0-9]{30,}\b|\bgithub_pat_[A-Za-z0-9_]{40,}\b|\bsk-(?:proj-)?[A-Za-z0-9_-]{24,}\b|\b(?:AKIA|ASIA)[0-9A-Z]{16}\b|\beyJ[A-Za-z0-9_-]{15,}\.[A-Za-z0-9_-]{15,}\.[A-Za-z0-9_-]{15,}\b|\b[A-Za-z0-9_-]{24,}\.[A-Za-z0-9_-]{6}\.[A-Za-z0-9_-]{27,}\b')
ASSIGN = re.compile(r'(?i)\b(password|passwd|api[_-]?key|client[_-]?secret|access[_-]?token|refresh[_-]?token|session[_-]?token|secret[_-]?key|OPENAI_API_KEY|AWS_SECRET_ACCESS_KEY|cookie|authorization)["\x27]?\s*[=:]\s*["\x27]?([^\s"\x27`,;<>}{)]+)')
BEARER = re.compile(r'(?i)\bBearer\s+([a-z0-9_.~+/-]{12,}=*)')
BASIC = re.compile(r'(?i)\bAuthorization["\x27]?\s*:\s*["\x27]?Basic\s+[A-Za-z0-9+/]{8,}={0,2}')
CONTEXT = re.compile(r'(?i)account|profile|アカウント|認証|本人|正規MSA|selected user|username|Player UUID|実UUID|実player owner')
ACCOUNT = re.compile(r'(?i)(?:Prism\s+profile|Minecraft\s+account(?:\s+name)?|Microsoft\s+account|account\s+(?:name|display\s*name)|username|選択account|正規MSA|アカウント(?:名|表示名))\s*[=:：]?\s*[`"\x27]([A-Za-z0-9_@.+-]{3,64})[`"\x27]')
ACCOUNT_BARE = re.compile(r'(?i)(?:Prism\s+profile|Minecraft\s+account(?:\s+name)?|Microsoft\s+account|account\s+(?:name|display\s*name)|username|アカウント(?:名|表示名))\s*[:=：]\s*([A-Za-z0-9_@.+-]{3,64})')
HOST = re.compile(r'(?i)\b(?:hostname|computername|VPN[_ -]?endpoint|remote[_ -]?host|machine[_ -]?name)\s*[=:]\s*[`"\x27]?([A-Za-z0-9][A-Za-z0-9._-]{2,})')
PUBLIC_URL = re.compile(r'https://(?:github\.com|raw\.githubusercontent\.com)/[^\s`<>"\x27)]+')
SENSITIVE = re.compile(r'(?i)(?:^|/)(?:\.env(?:\..*)?|\.git-credentials|accounts\.json|.*cookies.*|credentials(?:\.[^/]*)?|.*credential.*\.(?:db|json)|id_(?:rsa|dsa|ecdsa|ed25519)|.*\.(?:pem|key|p12|pfx)|.*(?:oauth|token|keychain).*\.(?:db|json)|Login Data)$')
PRIVATE_ROOTS = ('build/', 'run/', 'run-data/', '.gradle/', 'logs/', 'crash-reports/', 'screenshots/', 'backups/', 'verification/', 'runtime/', 'runtimes/', '.privacy-local/')
SAFE_WORDS = {'null', 'none', 'false', 'true', 'redacted', 'placeholder', 'example', 'test', 'dummy', 'changeme', 'localhost', '127.0.0.1', '0.0.0.0'}
CLASS = {'SECRET':('A_CRITICAL_SECRET','CRITICAL'), 'ACCOUNT':('B_ACCOUNT_PERSONAL','HIGH'), 'LOCAL':('C_LOCAL_ENVIRONMENT','MEDIUM'), 'NETWORK':('D_NETWORK','MEDIUM'), 'PUBLIC':('E_PUBLIC_PROJECT','INFO')}

def git(repo, *args):
    p=subprocess.run(['git',*args],cwd=repo,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    if p.returncode: raise RuntimeError('Git read operation failed (details suppressed)')
    return p.stdout

def sensitive(path):
    return bool(SENSITIVE.search(path.replace('\\','/')))

def safe_path(path):
    # Filenames can themselves be private. Never echo risky components.
    path=path.replace('\\','/')
    if HOME.search(path) or Path(path).is_absolute(): return '<LOCAL_PATH>'
    path=EMAIL.sub('<PRIVATE_EMAIL>',path)
    path=UUID.sub('<UUID_REDACTED>',path)
    if SECRET.search(path): return '<SENSITIVE_PATH>'
    return path

def finding(rule,path,line,kind,blocking=True,**extra):
    category,severity=CLASS[kind]
    return dict(rule=rule,path=safe_path(path),line=line,classification=category,severity=severity,blocking=blocking,**extra)

def read_allowlist(repo):
    p=repo/'tools/privacy_guard_allowlist.json'
    if not p.exists(): return {}
    data=json.loads(p.read_text(encoding='utf-8'))
    return {(v['path'],v['lineSha256']):v['reason'] for v in data['reviewedUuidLines']}

def classify_text(text,path,allow=None,known=None):
    allow=allow or {}; known=known or set(); out=[]
    for num,line in enumerate(text.splitlines(),1):
        public=PUBLIC_URL.sub('<PUBLIC_PROJECT_URL>',line)
        if SECRET.search(line):out.append(finding('RULE_SECRET_SHAPE',path,num,'SECRET'))
        for m in ASSIGN.finditer(line):
            value=m.group(2)
            if len(value)>=8 and value.lower() not in SAFE_WORDS and not value.startswith(('$','%','os.','System.','process.')):
                out.append(finding('RULE_SECRET_ASSIGNMENT',path,num,'SECRET'))
        if any(m.group(1).lower() not in SAFE_WORDS for m in BEARER.finditer(line)):out.append(finding('RULE_BEARER',path,num,'SECRET'))
        if BASIC.search(line):out.append(finding('RULE_BASIC_AUTH',path,num,'SECRET'))
        if HOME.search(line):out.append(finding('RULE_LOCAL_HOME',path,num,'LOCAL'))
        elif ABSOLUTE.search(line):out.append(finding('RULE_LOCAL_ABSOLUTE',path,num,'LOCAL'))
        for m in EMAIL.finditer(line):
            domain=m.group().split('@',1)[1].lower()
            if domain=='users.noreply.github.com' or domain in ('example.com','example.org','example.net'):
                out.append(finding('RULE_PUBLIC_EMAIL_EXAMPLE',path,num,'PUBLIC',False))
            else:out.append(finding('RULE_PERSONAL_EMAIL',path,num,'ACCOUNT'))
        if ACCOUNT.search(public) or ACCOUNT_BARE.search(public) or any(re.search(r'(?<![\w])'+re.escape(v)+r'(?![\w])',public,re.I) for v in known if len(v)>=3):
            out.append(finding('RULE_ACCOUNT_IDENTIFIER',path,num,'ACCOUNT'))
        if UUID.search(line):
            approved=(path,hashlib.sha256(line.encode('utf-8')).hexdigest()) in allow
            synthetic=bool(re.search(r'(?i)synthetic(?:[- ]test)? UUID|test-only UUID|deterministic test UUID',line)) and not CONTEXT.search(line)
            if approved or synthetic:out.append(finding('RULE_REVIEWED_TECHNICAL_UUID',path,num,'PUBLIC',False))
            else:out.append(finding('RULE_UUID_REVIEW_REQUIRED',path,num,'ACCOUNT'))
        for m in IP.finditer(line):
            try: address=ipaddress.ip_address(m.group())
            except ValueError:continue
            # Dotted MOD versions are not network identifiers without network context.
            network=bool(re.search(r'(?i)\b(?:IP|host|address|server-ip|bind|endpoint)\b|https?://',line))
            if address.is_loopback or address.is_unspecified:continue
            if network:out.append(finding('RULE_NETWORK_ADDRESS',path,num,'NETWORK'))
        for m in HOST.finditer(public):
            if m.group(1).lower() not in SAFE_WORDS:out.append(finding('RULE_HOST_IDENTIFIER',path,num,'NETWORK'))
    return out

def discover_identifiers(texts):
    """Ephemeral matching context only; never serialized or hashed."""
    values=set()
    for text in texts:
        for m in HOME.finditer(text):values.add(re.split(r'[/\\]',m.group())[-1])
        for m in ACCOUNT.finditer(text):values.add(m.group(1))
        for m in ACCOUNT_BARE.finditer(text):values.add(m.group(1))
    return {v for v in values if v.lower() not in SAFE_WORDS}

def decode(data):
    if b'\0' in data[:8192]:return None
    try:return data.decode('utf-8-sig')
    except UnicodeDecodeError:return None

def binary_review(data,path,allow):
    """Bounded metadata/strings inspection, no OCR or decompression of nested Jars."""
    strings=re.findall(rb'[\x20-\x7e]{8,}',data)
    out=classify_text('\n'.join(s.decode('ascii') for s in strings),path,allow)
    if data.startswith(b'PK'):
        with zipfile.ZipFile(io.BytesIO(data)) as z:
            for info in z.infolist():
                out.extend(classify_text(info.filename,path,allow))
                if info.filename.lower().endswith(('.jar','.zip')):continue
                if sensitive(info.filename):
                    out.append(finding('RULE_SENSITIVE_ARCHIVE_ENTRY',path,None,'SECRET'));continue
                if info.file_size>2_000_000:
                    out.append(finding('RULE_BINARY_ENTRY_REVIEW_REQUIRED',path,None,'LOCAL'));continue
                payload=z.read(info)
                text=decode(payload)
                if text is None:text='\n'.join(s.decode('ascii') for s in re.findall(rb'[\x20-\x7e]{8,}',payload))
                out.extend(classify_text(text,path,allow))
    return out

def scan_records(records,allow=None):
    """records=(path, lazy read callable). Sensitive paths never invoke read."""
    out=[]; texts=[]; meta=[]; binaries=0
    for path,reader in records:
        if sensitive(path):
            out.append(finding('RULE_SENSITIVE_FILENAME',path,None,'SECRET'));continue
        if path.startswith(PRIVATE_ROOTS):
            out.append(finding('RULE_PRIVATE_EVIDENCE_TRACKED',path,None,'LOCAL'));continue
        out.extend(classify_text(path,path))
        try:data=reader()
        except (OSError,RuntimeError):out.append(finding('RULE_UNREADABLE',path,None,'LOCAL'));continue
        text=decode(data)
        meta.append(dict(path=safe_path(path),bytes=len(data),text=text is not None))
        if text is not None:texts.append((path,text))
        else:
            binaries+=1
            try:out.extend(binary_review(data,path,allow))
            except (ValueError,zipfile.BadZipFile):out.append(finding('RULE_BINARY_REVIEW_REQUIRED',path,None,'LOCAL'))
    known=discover_identifiers(t for _,t in texts)
    for path,text in texts:out.extend(classify_text(text,path,allow,known))
    # Re-scan filename identifiers after context discovery, without exposing names.
    for path,_ in records:
        if any(v.lower() in path.lower().split('/')[-1] for v in known):
            f=finding('RULE_PERSONAL_FILENAME','<REDACTED_FILENAME>',None,'ACCOUNT');out.append(f)
    for item in out+meta:
        if any(re.search(r'(?<![\w])'+re.escape(v)+r'(?![\w])',item['path'],re.I) for v in known):
            item['path']='<PERSONAL_PATH>'
    return dict(files=len(records),binaryFiles=binaries,findings=out,blocking=sum(v['blocking'] for v in out),inventory=meta)

def working_records(repo,mode,path=None):
    if mode=='staged':
        names=git(repo,'diff','--cached','--name-only','--diff-filter=ACMR','-z').decode().split('\0')
        return [(n,lambda n=n:git(repo,'show',':'+n)) for n in names if n]
    if mode=='tracked':names=git(repo,'ls-files','-z').decode().split('\0')
    elif mode=='candidates':names=git(repo,'ls-files','-co','--exclude-standard','-z').decode().split('\0')
    else:
        p=Path(path).resolve()
        if not p.is_relative_to(repo):raise RuntimeError('Path must be inside repository')
        if p.is_dir():
            names=[x for x in git(repo,'ls-files','-co','--exclude-standard','-z').decode().split('\0') if x and (repo/x).is_relative_to(p)]
        else:names=[p.relative_to(repo).as_posix()]
    def read(n):
        p=repo/n
        if p.is_symlink():raise RuntimeError('Symlink content is not followed')
        return p.read_bytes()
    return [(n,lambda n=n:read(n)) for n in sorted(set(names)-{''})]

def self_test():
    # Entirely synthetic values assembled at runtime, never reported.
    home='C:'+'/'+'Users/'+'SyntheticTester/docs.txt'
    pat='gh'+'p_'+'Z'*36
    key='s'+'k-'+'q'*40
    uuid='12345678-'+'1234-4123-8123-'+'123456789abc'
    cases=[('windows_home',home,True),
           ('windows_backslash',home.replace('/','\\'),True),
           ('unix_home','/home/'+'synthetic/docs',True),
           ('mac_home','/Users/'+'synthetic/docs',True),
           ('email','person'+'@'+'private.invalid',True),
           ('github_pat',pat,True),
           ('api_key',key,True),
           ('private_key','-----BEGIN '+'RSA PRIVATE KEY-----',True),
           ('player_uuid','authenticated Player UUID '+uuid,True),
           ('account','Minecraft account name: '+chr(96)+'SyntheticPlayer'+chr(96),True),
           ('assignment','password='+'NotARealPassword42',True),
           ('bearer','Bearer '+'abcdef'*5,True),
           ('aws','AK'+'IA'+'Z'*16,True),
           ('jwt','ey'+'J'+'A'*20+'.'+'B'*20+'.'+'C'*20,True),
           ('host','hostname'+'='+'synthetic-host',True),
           ('public_ip','server-ip='+'203'+'.0.113.42',True),
           ('localhost','http://localhost:25565',False),
           ('loopback','server-ip=127.0.0.1',False),
           ('bind_any','0.0.0.0',False),
           ('sha256','A'*64,False),
           ('synthetic_uuid','synthetic test-only UUID '+uuid,False),
           ('java_token','long token = nextSequence++;',False),
           ('public_url','https://github.com/public-project/sample-mod',False),
           ('mod_id','foodhealing',False),
           ('package','com.leva.foodhealing',False),
           ('placeholder','<USER_HOME>/Downloads',False),
           ('secret_placeholder','password=<REDACTED>',False),
           ('noreply','123+public'+'@'+'users.noreply.github.com',False),
           ('version','MOD version 1.20.1.47',False),
           ('substring','disk-file is not a credential',False)]
    results=[]
    cases.extend([('json_password',json.dumps({'pass'+'word':'SyntheticValue42'}),True),
                  ('json_token',json.dumps({'access'+'_token':'a'*40}),True),
                  ('hex_secret','api'+'_key='+'a'*64,True),
                  ('gpg_key','-----BEGIN '+'PGP PRIVATE KEY BLOCK-----',True),
                  ('basic_auth','Authorization'+': Basic '+'YWJjZGVmZ2hpamts',True),
                  ('unicode_home','/home/'+'検証用/sample.txt',True),
                  ('bare_account','Minecraft account'+': SyntheticPlayer',True),
                  ('regex_code_not_path','I:'+chr(92)+chr(34)+'Max Level',False),
                  ('internal_token_word','String accessToken; long sequenceToken = 12;',False)])
    for name,text,want in cases:
        actual=any(v['blocking'] for v in classify_text(text,'fixture.txt'))
        results.append(dict(case=name,passed=actual==want,expectedBlocking=want))
    calls=[]
    def never_read():calls.append(True);raise AssertionError('Sensitive content was read')
    result=scan_records([('.env',never_read),
           ('accounts.json',never_read),
           ('id_rsa',never_read)])
    results.append(dict(case='sensitive_files_never_read',passed=not calls and result['blocking']==3,expectedBlocking=True))
    # Output includes only rule/location metadata, never a credential fragment.
    report=json.dumps(classify_text(pat,'fixture.txt'))
    results.append(dict(case='no_match_echo',passed=pat not in report,expectedBlocking=True))
    identity='Synthetic'+'Player'
    records=[('docs/note.md',lambda:('Minecraft account name: '+chr(96)+identity+chr(96)).encode()),('docs/'+identity+'.md',lambda:b'ordinary text')]
    named=scan_records(records)
    results.append(dict(case='personal_filename_not_echoed',passed=identity not in json.dumps(named) and any(v['rule']=='RULE_PERSONAL_FILENAME' for v in named['findings']),expectedBlocking=True))
    return dict(mode='self-test',tests=len(results),passed=sum(v['passed'] for v in results),cases=results,blocking=sum(not v['passed'] for v in results))

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    group=parser.add_mutually_exclusive_group(required=True)
    for flag in ['tracked','staged','candidates','self-test']:group.add_argument('--'+flag,action='store_true')
    group.add_argument('--path')
    parser.add_argument('--json',action='store_true',help='Location-only JSON; never matched content')
    args=parser.parse_args()
    try:
        if args.self_test:result=self_test()
        else:
            repo=Path(git(Path.cwd(),'rev-parse','--show-toplevel').decode().strip()).resolve()
            mode='tracked' if args.tracked else 'staged' if args.staged else 'candidates' if args.candidates else 'path'
            result=scan_records(working_records(repo,mode,args.path),read_allowlist(repo));result['mode']=mode
        result['status']='PASS' if result['blocking']==0 else 'BLOCKED'
        if args.json:print(json.dumps(result,ensure_ascii=False,indent=2))
        else:
            print(f"privacy_guard: {result['status']} blocking={result['blocking']}")
            for v in result.get('findings',[]):
                if v['blocking']:print(f"{v['rule']} {v['path']}:{v['line']} {v['severity']}")
        return int(result['blocking']>0)
    except (RuntimeError,OSError,ValueError):
        print('privacy_guard: BLOCKED; input/read failure (details suppressed)')
        return 1

if __name__=='__main__':sys.exit(main())

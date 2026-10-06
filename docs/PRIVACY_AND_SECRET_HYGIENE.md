# Privacy and secret hygiene

Public artifacts must contain only information needed to understand or reproduce the project. This policy also covers historical sections in working-tree documents. Redaction must preserve technical results, artifact hashes, versions, counts, timestamps, and gameplay contracts.

| Category | Severity | Treatment |
|---|---|---|
| A: credentials, tokens, passwords, private keys | CRITICAL | Block publication; never echo, hash, validate, or transmit values |
| B: personal account, email, real Player identity | HIGH | Redact from public artifacts |
| C: user home, absolute local path, machine/profile layout | MEDIUM | Use relative paths or placeholders |
| D: identifiable network address or hostname | MEDIUM | Review and redact when unnecessary |
| E: intentionally public project identity | INFO | Preserve public repository URLs, package/API namespaces, MOD IDs |

Use these placeholders consistently: `<LOCAL_USER>`, `<USER_HOME>`, `<LOCAL_DOWNLOADS>`, `<LOCAL_GRADLE_HOME>`, `<LOCAL_PRISM_ROOT>`, `<LOCAL_PRISM_INSTANCE>`, `<MINECRAFT_ACCOUNT>`, `<PLAYER_UUID>`, `<PRIVATE_EMAIL>`, `<LOCAL_HOST>`, `<PUBLIC_IP>`, `<LOCAL_PATH>`. Use a repository-relative path whenever possible. A placeholder in a recorded shell command must be supplied locally before execution; it is not a literal executable path.

Actual client reports identify authenticated `<MINECRAFT_ACCOUNT>` and same `<PLAYER_UUID>` without recording their values. Generated test entity UUIDs, stable owned modifier IDs, and deterministic synthetic fixtures are distinct from account identity. A reviewed UUID line may be allowed only by exact path and full-line fingerprint in `tools/privacy_guard_allowlist.json`; edits invalidate that allowance. The allowance is for public technical UUIDs only, never credentials or personal values.

Do not read launcher account files, browser profiles/cookies, credential managers, private keys, `.env`, Git credential stores, OAuth caches, or token files for this work. The scanner blocks sensitive filenames before opening them, including staged/history blob readers. It never looks for credentials outside the repository. Use only already functional read-only Git/GitHub operations for remote audits; unavailable authentication means an unverified surface, not permission to search for a token.

`build/reports`, verification worlds, screenshots, raw logs, private launcher instances, and backups stay outside the publication boundary. Do not delete them for privacy cleanup and do not force-add ignored evidence. Scan tracked filenames too: ignore rules do not protect files already tracked or deliberately staged.

Before commit, push, PR, or release, run from the repository root:

```text
python tools/privacy_guard.py --self-test
python tools/privacy_guard.py --tracked
python tools/privacy_guard.py --candidates
python tools/privacy_guard.py --staged
```

`--tracked` reads current working copies of all tracked files. `--candidates` adds nonignored untracked files. `--staged` reads actual staged blobs, without changing the index; an empty stage is NOT APPLICABLE. `--path relative/path` checks a file or repository subtree. `--json` returns rule, location, category, severity and blocking status, never the matched value or a preview. Exit 0 means no blocking findings in the scanned scope; exit 1 means findings or incomplete input. This is a dependency-free local heuristic, not proof of absolute security. Review both planned `git diff` and `git diff --cached` separately; no hook is installed and no Git settings are changed.

Patterns cover user paths, account contexts, email, UUIDs, network contexts, credential-shaped strings, credential assignments, authorization/Bearer forms, private-key headers, and credential filenames. An internal Java token variable, SHA256, semantic version, localhost/loopback, or public project URL is not a secret by itself. Binary checks inspect bounded metadata/strings and archive entries, not image OCR. Unknown/suspicious findings require review; do not broadly suppress a class of credentials to obtain PASS.

Report three independent states: local publication candidate, current public default branch, and reachable history/commit metadata. Local edits do not update GitHub. Past exposure can survive a normal cleanup commit in prior commits, tags, clones, forks, caches, release artifacts, or comments. History rewrite, force push, tag changes, and credential rotation are separate authorized work. If a potential credential exists publicly or in history, keep gameplay work and publication on HOLD and request a security remediation decision without reproducing its value.

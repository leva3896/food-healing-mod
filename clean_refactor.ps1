# 1. コピー先のクリア
if (Test-Path src\main\java\com\leva) {
    Remove-Item -Recurse -Force src\main\java\com\leva
}

# 2. 新しいフォルダ作成
New-Item -ItemType Directory -Force -Path "src\main\java\com\leva"

# 3. フォルダごとコピー
Copy-Item -Path "src\main\java\com\example\examplemod" -Destination "src\main\java\com\leva\foodhealing" -Recurse -Force

# 4. 元のフォルダを削除
Remove-Item -Recurse -Force src\main\java\com\example

# 5. 一括置換 & BOMなしUTF-8保存
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
Get-ChildItem -Path "src\main\java\com\leva\foodhealing" -Filter *.java -Recurse | ForEach-Object {
    $content = [System.IO.File]::ReadAllText($_.FullName)
    $newContent = $content -replace 'com\.example\.examplemod', 'com.leva.foodhealing'
    [System.IO.File]::WriteAllText($_.FullName, $newContent, $utf8NoBom)
}

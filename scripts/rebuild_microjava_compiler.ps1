Set-Location -Path (Join-Path $PSScriptRoot "..")

Write-Host "Rebuilding MicroJava Compiler..."

Remove-Item "bin" -Recurse -Force -ErrorAction SilentlyContinue

mkdir "bin" -Force

javac -cp "bin;lib/ant.jar;lib/ant-launcher.jar" -d "bin" "src/rs/ac/bg/etf/pp1/autorun/AntRunner.java"

java -cp "bin;lib/ant.jar;lib/ant-launcher.jar" rs.ac.bg.etf.pp1.autorun.AntRunner ant generateJar

Write-Host "MicroJava Compiler rebuilt successfully."

Read-Host -Prompt "Press Enter to exit"
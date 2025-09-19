Set-Location -Path (Join-Path $PSScriptRoot "..")

# $defaultProgramPath = "test/official_tests/test303.mj"
$defaultProgramPath = "test/code_generation/import3.mj"
$defaultInputPath = "src/rs/ac/bg/etf/pp1/code_generation/input/input.txt"
$defaultOutputPath = "src/rs/ac/bg/etf/pp1/code_generation/output"
# $defaultCommands = "--build --disasm --debug --run"
$defaultCommands = "--build --run"

$useDefaults = Read-Host "Do you want to use predefined default paths and commands? (y/n)"

if ($useDefaults -match '^[Yy]$')
{
    $programPath = $defaultProgramPath
    $inputPath = $defaultInputPath
    $outputPath = $defaultOutputPath
    $commands = $defaultCommands
}
else
{
    $programPath = Read-Host "Enter path to the .mj program file"
    $inputPath = Read-Host "Enter path to the .txt input file"
    $outputPath = Read-Host "Enter path to the output directory"
    $commands = Read-Host "Enter commands for execution separated by space (options: --build, --disasm, --debug, --run)"
}
$commandArray = $commands -split ' '

java -jar "bin/mjcompiler.jar" `
    --program $programPath `
    --input $inputPath `
    --output $outputPath `
    $commandArray

Read-Host -Prompt "Press Enter to exit"
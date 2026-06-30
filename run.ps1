Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

mvn -q -DskipTests compile dependency:build-classpath `
    "-Dmdep.outputFile=target/classpath.txt" `
    "-Dmdep.pathSeparator=;"

$jars = (Get-Content 'target/classpath.txt' -Raw).Trim() -split ';' |
    Where-Object { $_ -and (Test-Path $_) }
$modulePath = (($jars + (Join-Path $PWD 'target\classes')) -join ';')

& java `
    --enable-native-access=javafx.graphics `
    -p $modulePath `
    -m com.example.generator.ui/com.example.generator.ui.app.Launcher

# Directory containing the current script, only need when running under PowerShell v2
if ($PSVersionTable.PSVersion.Major -le 2) {
	$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}

# Utility for writing log
. "$PSScriptRoot\util\Function-Write-Log.ps1"

# Utility for writing file in UTF8 without BOM
. "$PSScriptRoot\util\Out-FileUtf8NoBom.ps1"

# save XML file
. "$PSScriptRoot\util\Out-XmlUtf8.ps1"

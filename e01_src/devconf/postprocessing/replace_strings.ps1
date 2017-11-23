# Parameters
param (
	[string]$mapping = "mapping.csv"
)

if ([string]::IsNullOrEmpty($mapping)) {
	Write-Output 'Usage:'
	Write-Output '	replace_strings.ps1 -mapping <mapping file, in csv>'
	return
}

# Directory containing the current script, only need when running under PowerShell v2
if ($PSVersionTable.PSVersion.Major -le 2) {
	$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}

# Source common utilities
. "$PSScriptRoot\common_util.ps1"

# Log File
$LogFile = "$PSScriptRoot\replace_strings.log"

# Main Logic
Write-Log -Path $LogFile -Message '[replace_strings] Started' -Level Debug
$situationViewDir = $Env:SITUATIONVIEW_DIR
$updated = 0
Import-Csv $mapping | %{
	$dirPath = "$($situationViewDir)\$($_.path)"
	$filePattern = $_.filePattern
	$srcString = $_.srcString
	$destString = $_.destString

	Write-Log -Path $LogFile -Message "[replace_strings] Processing ""$dirPath"" for file pattern ""$filePattern"", for ""$srcString"" -> ""$destString""..." -Level Debug
	Get-ChildItem -Path $dirPath -Include $filePattern -Recurse | Where-Object {
		($fileContent = Get-Content "$($_.FullName)" -Encoding UTF8) -and
		($fileContent -match "$srcString")
	} | ForEach-Object {
		Write-Log -Path $LogFile -Message "Updating $($_.FullName)" -Level Debug
		$fileContent -Replace "$srcString", "$destString" | Out-FileUtf8NoBom "$($_.FullName)"
		++$updated
	}
	Write-Log -Path $LogFile -Message "[replace_srings] Processed ""$dirPath""." -Level Debug
}
Write-Log -Path $LogFile -Message '[replace_strings] Completed' -Level Debug
Write-Log -Path $LogFile -Message "[replace_strings] Updated: $updated" -Level Info

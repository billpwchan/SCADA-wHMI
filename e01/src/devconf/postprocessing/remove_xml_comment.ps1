param (
	[string]$blacklist="",
	[string]$whitelist="",
	[boolean]$verbose=$false
)

# Directory containing the current script, only need when running under PowerShell v2
if ($PSVersionTable.PSVersion.Major -le 2) {
	$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}

# Source common utilities
. "$PSScriptRoot\common_util.ps1"

# Log File
$ScriptName = 'remove_xml_comment'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

$situationViewDir = $Env:SITUATIONVIEW_DIR

$blacklistSet = @()
if ($blacklist) {
	Import-Csv "$blacklist" | Where-Object {
		![string]::IsNullOrEmpty($_.path)
	} | %{
		$dirPath = "$($situationViewDir)\$($_.path)"
		$filePattern = $_.filePattern

		Get-ChildItem -Path $dirPath -Include $filePattern -Recurse | %{
			$blacklistSet += $_.Fullname
		}
	}
}

if ($whitelist) {
	$processedTotal = 0
	$blacklistedTotal = 0
	Import-Csv "$whitelist" | Where-Object {
		![string]::IsNullOrEmpty($_.path)
	} | %{
		$dirPath = "$($situationViewDir)\$($_.path)"
		$filePattern = $_.filePattern
		$excludeFilePattern = $_.excludeFilePattern
		$omitXmlDeclaration = $false
		if ($_.omitXmlDeclaration.ToLower() -eq "true") { $omitXmlDeclaration = $true }

		if ($excludeFilePattern) {
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern"", exclude ""$excludeFilePattern""..." -Level Debug
			$matchedFiles = Get-ChildItem -Path $dirPath -Include $filePattern -Exclude $excludeFilePattern.Split(",") -Recurse
		} else {
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern""..." -Level Debug
			$matchedFiles = Get-ChildItem -Path $dirPath -Include $filePattern -Recurse
		}
		$matchedFiles | %{
			$fileName = $_.Fullname
			$found = [array]::IndexOf($blacklistSet, $fileName)
			if ($found -ge 0) {
				Write-Log -Path $LogFile -Message "[$ScriptName] [BLACKLIST] Skipped ""$fileName""" -Level Debug
				++$blacklistedTotal
			} else {
				[xml]$xml = Get-Content $fileName -Encoding UTF8
				Out-XmlUtf8 -xml $xml -filename $filename -removeComment $true -omitXmlDeclaration $omitXmlDeclaration
				Write-Log -Path $LogFile -Message "[$ScriptName] Modified ""$fileName""..." -Level Debug
				++$processedTotal
			}
		}
		Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processedTotal, Total Blacklisted: $blacklistedTotal"
} else {
	Write-Log -Path $LogFile -Message "[$ScriptName] No whitelist items defined." -Level Error
}

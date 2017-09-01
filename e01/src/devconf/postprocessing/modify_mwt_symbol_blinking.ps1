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
$ScriptName = 'modify_mwt_symbol_blinking'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

$situationViewDir = $Env:SITUATIONVIEW_DIR

[xml]$blinkTemplate = '
<animationRule>
    <source xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="ns2:statusDataSourceType" name="needack"></source>
    <sourceType>INT</sourceType>
    <blinkAnimation defaultValue="false">
        <rangeBinding>
            <range from="1" to="2147483647" value="true"></range>
        </rangeBinding>
    </blinkAnimation>
</animationRule>
'

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
	$alreadyAddedTotal = 0
	$errorTotal = 0
	Import-Csv "$whitelist" | Where-Object {
		![string]::IsNullOrEmpty($_.path)
	} | %{
		$dirPath = "$($situationViewDir)\$($_.path)"
		$filePattern = $_.filePattern
		$excludeFilePattern = $_.excludeFilePattern

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
				Write-Log -Path $LogFile -Message "[$ScriptName] Modifying ""$fileName""..." -Level Debug
				[xml]$xml = Get-Content $fileName -Encoding UTF8
				if ($xml.animationConfiguration.animationRules) {
					$alreadyAdded = Select-Xml -xml $xml -XPath "//*[@name='needack']"

					if (!$alreadyAdded) {
						$child = $xml.ImportNode($blinkTemplate.get_DocumentElement(), $true)
						$null = $xml.animationConfiguration.animationRules.AppendChild($child)

						Out-XmlUtf8 -xml $xml -filename $filename -removeComment $true
						Write-Log -Path $LogFile -Message "[$ScriptName] Modified ""$fileName""." -Level Debug
						++$processedTotal
					} else {
						Write-Log -Path $LogFile -Message "[$ScriptName] Not modified ""$fileName"" [already added]." -Level Warn
						++$alreadyAddedTotal
					}
				} else {
					Write-Log -Path $LogFile -Message "[$ScriptName] Not modified ""$fileName"" [animationRules not found]." -Level Warn
					++$errorTotal
				}
			}
		}
		Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processedTotal, Total Blacklisted: $blacklistedTotal, Total Already Added: $alreadyAddedTotal, Total Error: $errorTotal" -Level Info
} else {
	Write-Log -Path $LogFile -Message "[$ScriptName] No whitelist items defined." -Level Warn
}

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
$ScriptName = 'modify_mwt_symbol_blinking_point_level'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

$situationViewDir = $Env:SITUATIONVIEW_DIR

$valueNodePatterns = @(
	  "symbol1.value_node"
	, "symbol2.value_node"
	, "symbol3.value_node"
	, "symbol4.value_node"
	, "symbol5.value_node"
	, "symbol6.value_node"
	, "symbol7.value_node"
	, "symbol8.value_node"
	, "colorNumber1.value_node"
	, "colorNumber2.value_node"
	, "colorNumber3.value_node"
	, "colorNumber4.value_node"
	, "colorNumber5.value_node"
	, "colorNumber6.value_node"
	, "colorNumber7.value_node"
	, "colorNumber8.value_node"
)

$valueNodeAlarmValueOffset = 100

[xml]$blinkTemplate = '
<blinkAnimation defaultValue="false">
	<rangeBinding>
		<range from="100" to="2147483647" value="true" />
	</rangeBinding>
</blinkAnimation>
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
		if ([string]::IsNullOrEmpty($matchedFiles)) {
			Write-Log -Path $LogFile -Message "[$ScriptName] File Pattern ""$filePattern"" matched 0 files" -Level Warn
		} else {
			$matchedFiles | %{
				$fileName = $_.Fullname
				$found = [array]::IndexOf($blacklistSet, $fileName)
				if ($found -ge 0) {
					Write-Log -Path $LogFile -Message "[$ScriptName] [BLACKLIST] Skipped ""$fileName""" -Level Debug
					++$blacklistedTotal
				} else {
					Write-Log -Path $LogFile -Message "[$ScriptName] Modifying ""$fileName""..." -Level Debug
					$modified = $false
					[xml]$xml = Get-Content $fileName -Encoding UTF8
					
					$xml.animationConfiguration.animationRules.animationRule | %{
						$rule = $_
						$ruleName = $rule.svgRefAnimation.nodeId
						$found = [array]::IndexOf($valueNodePatterns, $ruleName)
						if ($found -ge 0) {
							$valueMap = @{}
							$rule.svgRefAnimation.enumerationBinding.item | %{
								$inputValue = [int]$_.inputValue
								$value = $_.value
								if ($valueMap.ContainsKey($inputValue)) {
									Write-Log -Path $LogFile -Message "[$ScriptName] Duplicated inputValue [""$($inputValue)"": ""$($valueMap[$inputValue])""] {$value}" -Level Warn
								} else {
									$valueMap.Add([int]$_.inputValue, $value)
								}
							}
							
							$valueMap.GetEnumerator() | %{
								$inputValue = $_.key
								$value = $_.value
								$alarmInputValue = $inputValue + $valueNodeAlarmValueOffset
								if ($inputValue -ge $valueNodeAlarmValueOffset) {
									Write-Log -Path $LogFile -Message "[$ScriptName] Skipped ""$($inputValue)"": ""$($value)""" -Level Debug
								} elseif ($valueMap.ContainsKey($alarmInputValue)) {
									Write-Log -Path $LogFile -Message "[$ScriptName] Already has matching alarmInputValue [""$($alarmInputValue)"": ""$($valueMap[$alarmInputValue])""] {$value}" -Level Warn
								} else {
									$newItem = $xml.ImportNode(([xml]"<item inputValue=""$($alarmInputValue)"" value=""$($value)""></item>").get_DocumentElement(), $true)
									$null = $rule.svgRefAnimation.enumerationBinding.AppendChild($newItem)
									Write-Log -Path $LogFile -Message "[$ScriptName] Added ""$($alarmInputValue)"": ""$($value)""" -Level Debug
									$modified = $true
								}
							}
							
							if ($modified) {
								$newItem = $xml.ImportNode(([xml]"<blinkAnimation defaultValue=""false""><rangeBinding><range from=""$($valueNodeAlarmValueOffset)"" to=""2147483647"" value=""true""/></rangeBinding></blinkAnimation>").get_DocumentElement(), $true)
								$null = $rule.AppendChild($newItem)
								Write-Log -Path $LogFile -Message "[$ScriptName] Added blinkAnimation" -Level Debug
							}
						}
					}

					if ($modified) {
						Out-XmlUtf8 -xml $xml -filename $filename -removeComment $true
						Write-Log -Path $LogFile -Message "[$ScriptName] Modified ""$fileName""." -Level Debug
						++$processedTotal
					} else {
						Write-Log -Path $LogFile -Message "[$ScriptName] Not modified ""$fileName"" [valueNode not found]." -Level Warn
						++$errorTotal
					}
				}
			}
		}
		Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processedTotal, Total Blacklisted: $blacklistedTotal, Total Error: $errorTotal" -Level Info
} else {
	Write-Log -Path $LogFile -Message "[$ScriptName] No whitelist items defined." -Level Warn
}

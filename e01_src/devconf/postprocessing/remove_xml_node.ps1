param (
	[string]$mapping="",
	[boolean]$verbose=$false
)

# Directory containing the current script, only need when running under PowerShell v2
if ($PSVersionTable.PSVersion.Major -le 2) {
	$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}

# Source common utilities
. "$PSScriptRoot\common_util.ps1"

# Log File
$ScriptName = 'remove_xml_node'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

$situationViewDir = $Env:SITUATIONVIEW_DIR

function RemoveNode($node, $level) {
	$owner = $node.ParentNode
	$target = $node
	if (!$target -or !$owner) {
		$false
		return
	}

	for ($i = 0; $i -lt $level; ++$i) {
		$target = $owner
		$owner = $owner.ParentNode
		if (!$target -or !$owner) {
			$false
			return
		}
	}
	if ($owner.RemoveChild($target)) {
		$true
		return
	} else {
		$false
		return
	}
}

if ($mapping) {
	$processedTotal = 0
	$unprocessedTotal = 0
	$errorTotal = 0
	Import-Csv "$mapping" | Where-Object {
		![string]::IsNullOrEmpty($_.path)
	} | %{
		$dirPath = "$($situationViewDir)\$($_.path)"
		$filePattern = $_.filePattern
		$excludeFilePattern = $_.excludeFilePattern
		$attributeName = $_.attributeName
		$attributeValue = $_.attributeValue
		# 0: mean only the matching node, 1: remove immediate parent, 2: remove grandparent, 3: remove grand-grand parent, etc
		$removeParentLevel = $_.removeParentLevel

		if ($excludeFilePattern) {
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern"", exclude ""$excludeFilePattern""..." -Level Debug
			$matchedFiles = Get-ChildItem -Path $dirPath -Include $filePattern -Exclude $excludeFilePattern.Split(",") -Recurse
		} else {
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern""..." -Level Debug
			$matchedFiles = Get-ChildItem -Path $dirPath -Include $filePattern -Recurse
		}
		$matchedFiles | %{
			$fileName = $_.Fullname
			Write-Log -Path $LogFile -Message "[$ScriptName] Modifying ""$fileName""..." -Level Debug
			[xml]$xml = Get-Content $fileName -Encoding UTF8
			$modified = $false
			$hasError = $false
			$xml.SelectNodes("//*[@$($attributeName)='$($attributeValue)']") | %{
				$removed = RemoveNode -node $_ -level $removeParentLevel
				if ($removed) {
					$modified = $true
				} else {
					Write-Log -Path $LogFile -Message "[$ScriptName] Not Modified ""$filename"" [Failed to find parent: $attributeName, $attributeValue, $removeParentLevel]." -Level Warn
					$hasError = $true
				}
			}
			if ($hasError) {++$errorTotal}
			if ($modified) {
				Out-XmlUtf8 -xml $xml -filename $filename -removeComment $true
				Write-Log -Path $LogFile -Message "[$ScriptName] Modified ""$filename""" -Level Debug
				++$processedTotal
			} else {
				Write-Log -Path $LogFile -Message "[$ScriptName] Not Modified ""$filename""" -Level Debug
				++$unprocessedTotal
			}
		}
		Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processedTotal, Total Unprocessed: $unprocessedTotal, Total Error: $errorTotal" -Level Info
} else {
	Write-Log -Path $LogFile -Message "[$ScriptName] No mapping defined." -Level Warn
}

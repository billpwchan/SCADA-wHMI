# Parameters
param (
	[string]$mapping = "mapping.csv"
)

# Directory containing the current script, only need when running under PowerShell v2
if ($PSVersionTable.PSVersion.Major -le 2) {
	$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}

# Source common utilities
. "$PSScriptRoot\common_util.ps1"

# Log File
$ScriptName = 'modify_mwt_symbol_highlight'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

# Main Logic
function ApplySize() {
	Param (
		[Parameter(Mandatory=$true, ValueFromPipelineByPropertyName=$true)]
		[ValidateNotNullOrEmpty()]
		[string]$filePath,
		[Parameter(Mandatory=$true, ValueFromPipelineByPropertyName=$true)]
		[ValidateNotNullOrEmpty()]
		$rect
	)
	Write-Log -Path $LogFile -Message "[$ScriptName] $filePath (width: $($rect.width), height: $($rect.height))" -Level Debug
	[xml]$xml = Get-Content $filePath -Encoding UTF8
	$xml.symbolConfiguration.entitySymbol.width = [convert]::ToString($rect.width)
	$xml.symbolConfiguration.entitySymbol.height = [convert]::ToString($rect.height)
	Out-XmlUtf8 -xml $xml -filename $filePath -removeComment $true
}

Write-Log -Path $LogFile -Message "[$ScriptName] Started" -Level Debug
$situationViewDir = $Env:SITUATIONVIEW_DIR
$processed = 0
$skipped = 0
$errored = 0
$appliedList = @()
Import-Csv $mapping | Where-Object {
	![string]::IsNullOrEmpty($_.path)
} | ForEach-Object {
	$dirPath = "$($situationViewDir)\$($_.path)"
	$filePattern = $_.filePattern
	$width = $_.width
	$height = $_.height
	$matchFilePatterns = $_.matchFilePatterns.Split(' ')

	Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern""..." -Level Debug
	if (![string]::IsNullOrEmpty($width) -and ![string]::IsNullOrEmpty($height)) {
		Get-ChildItem -Path $dirPath -Recurse | Where-Object {
			$appliedList -notcontains $_.FullName -and
			$_.Name -match $filePattern
		} | ForEach-Object {
			$fileFullName = $_.FullName
			ApplySize -filePath $fileFullName -rect @{'width' = $width; 'height' = $height}
			++$processed
			$appliedList += $fileFullName
		}
	} else {
		Get-ChildItem -Path $dirPath -Recurse | Where-Object {
			$appliedList -notcontains $_.FullName -and
			$_.Name -match $filePattern
		} | ForEach-Object {
			$fileName = $_.Name
			$fileFullName = $_.FullName

			$applied = $false
			for ($i = 0; $i -lt $matchFilePatterns.Length; ++$i) {
				$matchedTarget = $fileName -replace $filePattern, $matchFilePatterns[$i]
				$matchedFullPath = "$($situationViewDir)\$($matchedTarget)"
				if (Test-Path "$matchedFullPath") {
					$svgRect = &"$PSScriptRoot\get_mwt_svg_rect.ps1" -svg "$matchedFullPath"
					if (
						![string]::IsNullOrEmpty($svgRect.width) -and
						![string]::IsNullOrEmpty($svgRect.height)
					) {
						ApplySize -filePath $fileFullName -rect $svgRect
						++$processed
					} else {
						Write-Log -Path $LogFile -Message "[$ScriptName] Failed in parsing symbol rectangle: $fileFullName" -Level Error
						++$errored
					}
					$applied = $true
					$appliedList += $fileFullName
					break
				}
			}
			if (!$applied) {
				Write-Log -Path $LogFile -Message "[$ScriptName] No matching SVG: $fileFullName" -Level Warn
				++$skipped
			}
		}
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
}
Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processed, Total Skipped: $skipped, Total Error: $errored" -Level Info
Write-Log -Path $LogFile -Message "[$ScriptName] Completed" -Level Debug

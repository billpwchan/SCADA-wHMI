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
$ScriptName = 'modify_mwt_view_resolution'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

$situationViewDir = $Env:SITUATIONVIEW_DIR

function GenerateZoomResoluations {
	param(
		[float]$width,
		[float]$height,
		[float]$extentWidth,
		[float]$extentHeight,
		$zoomLevels
	)
	$maxRatio = (($extentWidth / $width), ($extentHeight / $height) | Measure-Object -Maximum).Maximum
	$resolutions = ""
	$zoomLevels | %{
		$level = [float]$_
		if (! $resolutions -eq "") {$resolutions += " "}
		$resolutions += [string]($maxRatio / $level)
	}
	$resolutions
}

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
		$width = [float]$_.width
		$height = [float]$_.height
		$zoomLevels = $_.zoomLevels.Split(" ")
		$displayLayerSwitcher = $_.displayLayerSwitcher.ToLower()
		$displayPanZoomBar = $_.displayPanZoomBar.ToLower()
		$forceZoomLevel = $_.forceZoomLevel.ToLower()

		if ($excludeFilePattern) {
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern"", exclude ""$excludeFilePattern""..." -Level Debug
			$matchedFiles = Get-ChildItem -Path $dirPath -Include $filePattern -Exclude $excludeFilePattern.Split(",") -Recurse
		} else {
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern""..." -Level Debug
			$matchedFiles = Get-ChildItem -Path $dirPath -Include $filePattern -Recurse
		}
		if (![string]::IsNullOrEmpty($matchedFiles)) {
			$matchedFiles | %{
				$fileName = $_.Fullname
				$found = [array]::IndexOf($blacklistSet, $fileName)
				if ($found -ge 0) {
					Write-Log -Path $LogFile -Message "[$ScriptName] [BLACKLIST] Skipped ""$fileName""" -Level Debug
					++$blacklistedTotal
				} else {
					Write-Log -Path $LogFile -Message "[$ScriptName] Modifying ""$fileName""..." -Level Debug
					[xml]$xml = Get-Content $fileName -Encoding UTF8
					$view = $xml.viewConfiguration.view
					if (!$view) {
						Write-Log -Path $LogFile -Message "[$ScriptName] Not modified ""$fileName"" [view not found]." -Level Warn
						++$errorTotal
					} else {
						$control = $view.controls
						if (!$control) {
							Write-Log -Path $LogFile -Message "[$ScriptName] Not modified ""$fileName"" [control not found]." -Level Warn
							++$errorTotal
						}
					}
					if ($view -and $control) {
						$modified = $false

						$extentWidth = [float]$view.maxExtent.split()[2] - [float]$view.maxExtent.split()[0]
						$extentHeight = [float]$view.maxExtent.split()[3] - [float]$view.maxExtent.split()[1]

						Write-Log -Path $LogFile -Message "[$ScriptName] ""$fileName"", width: ""$($width)"", height: ""$($height)"", extentWidth: ""$($extentWidth)"", extentHeight: ""$($extentHeight)""" -Level Debug
						if (
							($forceZoomLevel -and ($forceZoomLevel.ToLower() -eq 'true')) -or
							($control.displayPanZoomBar.ToLower() -eq "true")
						) {
							$resolutions = GenerateZoomResoluations -width $width -height $height -extentWidth $extentWidth -extentHeight $extentHeight -zoomLevels $zoomLevels
						} else {
							$resolutions = GenerateZoomResoluations -width $width -height $height -extentWidth $extentWidth -extentHeight $extentHeight -zoomLevels $zoomLevels[0]
						}
						Write-Log -Path $LogFile -Message "[$ScriptName] ""$fileName"", Resolutions: ""$($resolutions)""" -Level Debug
						if (!($view.resolution -eq $resolutions)){
							$view.resolution = $resolutions
							$modified = $true
						}
						if ($displayLayerSwitcher -and !($control.displayLayerSwitcher.ToLower() -eq $displayLayerSwitcher)) {
							$control.displayLayerSwitcher = $displayLayerSwitcher
							$modified = $true
						}
						if ($displayPanZoomBar -and !($control.displayPanZoomBar.ToLower() -eq $displayPanZoomBar)) {
							$control.displayPanZoomBar = $displayPanZoomBar
							$modified = $true
						}

						if ($modified){
							Out-XmlUtf8 -xml $xml -filename $filename -removeComment $true
							Write-Log -Path $LogFile -Message "[$ScriptName] Modified ""$fileName""." -Level Debug
							++$processedTotal
						} else {
							Write-Log -Path $LogFile -Message "[$ScriptName] Not modified ""$fileName"" [already added]." -Level Debug
							++$alreadyAddedTotal
						}
					}
				}
			}
		} else {
			Write-Log -Path $LogFile -Message "[$ScriptName] No file found in ""$dirPath"" for file pattern ""$filePattern""..." -Level Debug
		}
		Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processedTotal, Total Blacklisted: $blacklistedTotal, Total Already Added: $alreadyAddedTotal, Total Error: $errorTotal" -Level Info
} else {
	Write-Log -Path $LogFile -Message "[$ScriptName] No whitelist items defined." -Level Warn
}

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
$ScriptName = 'insert_mwt_symbol_point_mgmt'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

# Main Logic
function GetMOTag() {
	Param (
		[string]$id,
		[int]$x,
		[int]$y,
		[float]$scale
	)
	@("$($id)_Tag_MO", "Tag_MO_Scale$($scale)_X$($x)_Y$($y)")
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
	$excludeFilePattern = $_.excludeFilePattern
	$x = $_.x
	$y = $_.y
	$scale = $_.scale
	$matchFilePatterns = $_.matchFilePatterns.Split(' ')

	Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$dirPath"" for file pattern ""$filePattern"", excludeFilePatterns ""$excludeFilePatterns""..." -Level Debug
	$matchedFiles = @()
	Get-ChildItem -Path $dirPath | Where-Object {
		$appliedList -notcontains $_.FullName -and
		$_.Name -match $filePattern -and
		(
			(!$excludeFilePattern) -or
			(!($excludeFilePattern.Split(',') | Where-Object {$_.Name -match $_}))
		)
	} | ForEach-Object {
		$matchedFiles += $_.FullName
		$appliedList += $_.FullName
	}

	if (![string]::IsNullOrEmpty($matchedFiles)) {
		if (([string]::IsNullOrEmpty($scale)) -or ($scale -le 0)) {
			Write-Log -Path $logFile -Message "[$ScriptName] Skipped ""$matchedFiles""." -Level Debug
		} else {
			if ([string]::IsNullOrEmpty($x) -or [string]::IsNullOrEmpty($y)) {
				# extract x, y from the matched files
				$matchedFiles | ForEach-Object {
					$tagMO = GetMOTag -id $_ -x $x -y $y -scale $scale
					Write-Log -Path $logFile -Message "[$ScriptName] TagMo ""$tagMo""." -Level Debug
				}
			}
			# use the specified x, y
			Write-Log -Path $logFile -Message "[$ScriptName] Using (x, y) = (""$x"", ""$y"")." -Level Debug

			Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath""." -Level Debug
		}
	} else {
		Write-Log -Path $LogFile -Message "[$ScriptName] No matches found." -Level Debug
	}
}
Write-Log -Path $LogFile -Message "[$ScriptName] Total Processed: $processed, Total Skipped: $skipped, Total Error: $errored" -Level Info
Write-Log -Path $LogFile -Message "[$ScriptName] Completed" -Level Debug

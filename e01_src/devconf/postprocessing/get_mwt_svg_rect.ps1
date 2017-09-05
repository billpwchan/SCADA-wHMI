# Parameters
param (
	[string]$svg
)

if ([string]::IsNullOrEmpty($svg)) {
	Write-Output 'Usage:'
	Write-Output '	get_mwt_svg_rect.ps1 -svg <path to svg fiel>'
	return
}

# Directory containing the current script, only need when running under PowerShell v2
if ($PSVersionTable.PSVersion.Major -le 2) {
	$PSScriptRoot = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}

# Source common utilities
. "$PSScriptRoot\common_util.ps1"

# Log File
$LogFile = "$PSScriptRoot\get_mwt_svg_rect.log"

# Main Logic
function GetTransformScale() {
	Param (
		[Parameter(Mandatory=$true)]
		[ValidateNotNullOrEmpty()]
		$node
	)
	$transform = $node.transform
	if (
		(![string]::IsNullOrEmpty($transform)) -and
		($transform -match '.*scale\s*\(\s*(\d+\.?\d*)\s*,?\s*(\d+\.?\d*)\s*\).*')
	) {
		try {
			$scaleX = [convert]::ToDouble($matches[1])
			$scaleY = [convert]::ToDouble($matches[2])
			@{'width' = $scaleX; 'height' = $scaleY}
			return
		} catch {
		}
	}
	@{'width' = 1; 'height' = 1}
	return
}

function GetTransformRotate() {
	Param (
		[Parameter(Mandatory=$true)]
		[ValidateNotNullOrEmpty()]
		$node
	)
	$transform = $node.transform
	if (
		(![string]::IsNullOrEmpty($transform)) -and
		($transform -match '.*rotate\s*\(\s*(-?\d+\.?\d*).*\)')
	) {
		try {
			[convert]::ToDouble($matches[1])
			return
		} catch {
		}
	}
	0
	return
}

function ApplyTransformRotate() {
	Param (
		[Parameter(Mandatory=$true)]
		[ValidateNotNullOrEmpty()]
		$rotate,
		[Parameter(Mandatory=$true)]
		[ValidateNotNullOrEmpty()]
		$rect
	)
	$rotateAbs = [Math]::Abs($rotate)
	if (($rotateAbs -eq 0) -or ($rotateAbs -eq 180)) {
		$rect
	} elseif (($rotateAbs -eq 90) -or ($rotateAbs -eq 270)) {
		@{'width' = $rect.height; 'height' = $rect.width}
	} else {
		$rect
	}
	return
}

function GetRect() {
	Param (
		[Parameter(Mandatory=$true)]
		[ValidateNotNullOrEmpty()]
		$node
	)
	$name = $node.name.ToLower()
	if (('svg' -eq $name) -or ('g' -eq $name)) {
		$maxWidth = 0
		$maxHeight = 0
		$scale = GetTransformScale($node)
		$rotate = GetTransformRotate($node)
		$node.ChildNodes | ForEach-Object {
			$childRect = GetRect($_)
			$maxWidth = ($maxWidth, $childRect.width | Measure-Object -Maximum).Maximum
			$maxHeight = ($maxHeight, $childRect.height | Measure-Object -Maximum).Maximum
		}
		ApplyTransformRotate -rotate $rotate -rect @{'width' = $maxWidth * $scale.width; 'height' = $maxHeight * $scale.height}
	} elseif ('rect' -eq $name) {
		if (
			![string]::IsNullOrEmpty($node.width) -and
			![string]::IsNullOrEmpty($node.height)
		) {
			try {
				$width = [convert]::ToDouble(($node.width -replace "px",""))
				$height = [convert]::ToDouble(($node.height -replace "px",""))
				@{'width' = $width; 'height' = $height}
			} catch {
				Write-Log -Path $LogFile -Message "Error in parsing 'width' ($($node.width)) or 'height' ($($node.height)) value(s)" -Level Error
				@{}
			}
		} else {
			Write-Log -Path $LogFile -Message 'No "width" or "height" attribute(s) found in "rect" element'
			@{}
		}
	} elseif ('text' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} elseif ('line' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} elseif ('circle' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} elseif ('ellipse' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} elseif ('path' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} elseif ('polygon' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} elseif ('animate' -eq $name) {
		@{'width' = 0; 'height' = 0}
	} else {
		Write-Log -Path $LogFile -Message "Unexpected element type: $name"
		@{}
	}
	return
}

if ($true -ne (Test-Path "$svg")) {
	Write-Log -Path $LogFile -Message "$svg is not found" -Level Error
	return
}
[xml]$xml = Get-Content "$svg" -Encoding UTF8
$value = GetRect($xml.svg)
Write-Output $value
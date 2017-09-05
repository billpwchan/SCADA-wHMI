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
$ScriptName = 'transform_symbol_by_view'
$LogFile = "$PSScriptRoot\$($ScriptName).log"

$situationViewDir = $Env:SITUATIONVIEW_DIR

$valueNodeBlacklistPatterns = @(
	  "inhibsynthesis.value_node"
)

function GetRotationTemplate {
	param([float]$rotate, [string]$nodeId)
	[xml]"
		<rotationAnimation defaultValue=""$($rotate)"" nodeId=""$($nodeId)"">
			<rangeBinding>
				<range from=""0"" to=""2147483647"" value=""$($rotate)""></range>
			</rangeBinding>
		</rotationAnimation>"
}

function GetVFlipTemplate {
	param([string]$nodeId)
	[xml]"
	<vFlipAnimation defaultValue=""true"" nodeId=""$($nodeId)"">
		<rangeBinding>
			<range from=""0"" to=""2147483647"" value=""true""></range>
		</rangeBinding>
	</vFlipAnimation>"
}

function GetHFlipTemplate {
	param([string]$nodeId)
	[xml]"
	<hFlipAnimation defaultValue=""true"" nodeId=""$($nodeId)"">
		<rangeBinding>
			<range from=""0"" to=""2147483647"" value=""true""></range>
		</rangeBinding>
	</hFlipAnimation>"
}

function CreateTransformedSymbol {
	param(
		[string]$symbolId,
		[float]$rotate,
		[boolean]$vFlip,
		[boolean]$hFlip,
		[string]$newSymbolId
	)
	Write-Log -Path $LogFile -Message "[$ScriptName] Creating transformed symbol ""$($newSymbolId)"" from ""$($symbolId)""..." -Level Debug
	if (
		(!(Test-Path "$($situationViewDir)\symbols\$($symbolId)_symbol.xml")) -or
		(!(Test-Path "$($situationViewDir)\symbols\$($symbolId)_graphic.xml")) -or
		(!(Test-Path "$($situationViewDir)\symbols\$($symbolId)_animation.xml"))
	) {
		Write-Log -Path $LogFile -Message "[$ScriptName] Failed in creating transformed symbol ""$($newSymbolId)""." -Level Warn
		@($symbolId, $rotate, $vFlip, $hFlip)
		return
	}

	$newGraphicId = "$($newSymbolId)_graphic"
	$newAnimationId = "$($newSymbolId)_animation"
	# update the symbol definition
	[xml]$symbol = Get-Content "$($situationViewDir)\symbols\$($symbolId)_symbol.xml" -Encoding UTF8
	$symbol.symbolConfiguration.entitySymbol.id = $newSymbolId
	$symbol.symbolConfiguration.entitySymbol.animationId = $newAnimationId
	$symbol.symbolConfiguration.entitySymbol.graphicId = $newGraphicId

	# Target
	#   To transform (rotate / flip) the the symbol itself, so as to control the rotation required at the schematics to be under +-45 degree
	# Quirks
	# 1. Symbol exported always flip, if applied, before rotate
	# 2. vFlip & hFlip in MC is swapped, but the exported xml are correct
	# Logic
	# 1. Calculate the rotation of symbol itself ($symbolRotate) that will make the rotation of symbol in view ($viewRotate) to be within +-45 degree
	# 2. if (45 <= $symbolRotate < 135) or (225 <= $symbolRotate < 315), the aspect ratio of the resuling symbol is swapped [$aspectSwapped]
	# 3. apply symbol aspect ratio to the tags
	# 4. apply symbol rotation
	$aspectSwapped = $false
	$symbolRotate = 0
	$viewRotate = 0

	# 1. Calculate the rotation of symbol itself ($symbolRotate) that will make the rotation of symbol in view ($viewRotate) to be within +-45 degree
	$symbolRotate = [Math]::Floor($rotate / 90) * 90
	$viewRotate = $rotate - $symbolRotate
	if ($viewRotate -gt 45) {
		$symbolRotate += 90
		$viewRotate = (($rotate - $symbolRotate) + 360) % 360
	}
	$symbolRotate %= 360

	# 2. if (45 <= $symbolRotate < 135) or (225 <= $symbolRotate < 315), the aspect ratio of the resuling symbol is swapped [$aspectSwapped]
	if (
		(($symbolRotate -ge 45) -and ($symbolRotate -lt 135)) -or
		(($symbolRotate -ge 225) -and ($symbolRotate -lt 315))
	) {
		$aspectSwapped = $true
	}

	if (($symbolRotate -eq 0) -and (!$vFlip) -and (!$hFlip)) {
		# no change to the symbol and the view
		@($symbolId, $rotate, $false, $false)
		return
	} elseif ($aspectSwapped) {
		# 3. apply symbol aspect ratio to the tags
		$width = $symbol.symbolConfiguration.entitySymbol.width
		$symbol.symbolConfiguration.entitySymbol.width = $symbol.symbolConfiguration.entitySymbol.height
		$symbol.symbolConfiguration.entitySymbol.height = $width
	}

	# 4. apply symbol rotation
	# update the graphic definition
	[xml]$graphic = Get-Content "$($situationViewDir)\symbols\$($symbolId)_graphic.xml" -Encoding UTF8
	$graphic.graphicConfiguration.graphic.id = $newGraphicId
	$graphic.graphicConfiguration.graphic.root.svgGroup | Where-Object {
		![string]::IsNullOrEmpty($_.id)
	} | ForEach-Object {
		$_.id = $_.id -Replace $symbolId, $newSymbolId
	}

	# update the animation definition
	[xml]$animation = Get-Content "$($situationViewDir)\symbols\$($symbolId)_animation.xml" -Encoding UTF8
	$animation.animationConfiguration.animationRules.id = $newAnimationId
	$animation.animationConfiguration.animationRules.animationRule | Where-Object {
		(![string]::IsNullOrEmpty($_.svgRefAnimation)) -and
		([array]::IndexOf($valueNodeBlacklistPatterns, $_.svgRefAnimation.nodeId) -lt 0)
	} | ForEach-Object {
		if ($symbolRotate -ne 0) {
			$template = GetRotationTemplate -rotate $symbolRotate -nodeId $_.svgRefAnimation.nodeId
			$child = $animation.ImportNode($template.get_DocumentElement(), $true)
			$null = $_.AppendChild($child)
		}
		if ($vFlip) {
			$template = GetVFlipTemplate -nodeId $_.svgRefAnimation.nodeId
			$child = $animation.ImportNode($template.get_DocumentElement(), $true)
			$null = $_.AppendChild($child)
		}
		if ($hFlip) {
			$template = GetHFlipTemplate -nodeId $_.svgRefAnimation.nodeId
			$child = $animation.ImportNode($template.get_DocumentElement(), $true)
			$null = $_.AppendChild($child)
		}
	}
	
	# FIXME: handle aspect ratio change to tag layer - START
	if ($aspectSwapped) {
		$animationTagNodeId = "inhibsynthesis.value_node"
		$animation.animationConfiguration.animationRules.animationRule | Where-Object {
			(![string]::IsNullOrEmpty($_.svgRefAnimation)) -and
			($_.svgRefAnimation.nodeId -eq $animationTagNodeId)
		} | ForEach-Object {
			$_.svgRefAnimation.rangeBinding.range.value = $_.svgRefAnimation.rangeBinding.range.value + '_R270'
		}
	}
	# FIXME: handle aspect ratio change to tag layer - END

	# save the revised files with new names
	Out-XmlUtf8 -xml $symbol -filename "$($situationViewDir)\symbols\$($newSymbolId)_symbol.xml" -removeComment $true
	Out-XmlUtf8 -xml $graphic -filename "$($situationViewDir)\symbols\$($newSymbolId)_graphic.xml" -removeComment $true
	Out-XmlUtf8 -xml $animation -filename "$($situationViewDir)\symbols\$($newSymbolId)_animation.xml" -removeComment $true

	Write-Log -Path $LogFile -Message "[$ScriptName] Created transformed symbol ""$($newSymbolId)"" from ""$($symbolId)""" -Level Debug
	@($newSymbolId, $viewRotate, $false, $false)
}

$GlobalCachedTransformedSymbols = @{}
function GetTransformedSymbol {
	param(
		[string]$symbolId,
		[float]$rotate,
		[boolean]$vFlip,
		[boolean]$hFlip
	)
	$newSymbolId = $symbolId
	if ($rotate -ne 0) {
		$newSymbolId = "$($newSymbolId)_R$($rotate)"
	}
	if ($vFlip -eq $true) {
		$newSymbolId = "$($newSymbolId)_vFlip"
	}
	if ($hFlip -eq $true) {
		$newSymbolId = "$($newSymbolId)_hFlip"
	}

	$cached = $GlobalCachedTransformedSymbols[$newSymbolId]
	if ([string]::IsNullOrEmpty($cached)) {
		# create the transformed symbol from the original symbol
		$transformedSymbol = CreateTransformedSymbol -symbolId $symbolId -rotate $rotate -vFlip $vFlip -hFlip $hFlip -newSymbolId $newSymbolId
		$GlobalCachedTransformedSymbols.Add($newSymbolId, $transformedSymbol)
		$transformedSymbol
		return
	} else {
		# transformed symbol already created
		$cached
		return
	}
}

function ProcessSymbolInstances {
	param(
		$instances
	)
	$modified = 0
	$instances | Where-Object {
		(![string]::IsNullOrEmpty($_.symbolId)) -and (
			((![string]::IsNullOrEmpty($_.rotate)) -and (([float]($_.rotate) % 360) -ne 0)) -or
			((![string]::IsNullOrEmpty($_.vFlip)) -and ($_.vFlip.ToLower() -eq "true")) -or
			((![string]::IsNullOrEmpty($_.hFlip)) -and ($_.hFlip.ToLower() -eq "true"))
		)
	} | ForEach-Object {
		$symbolId = $_.symbolId
		$rotate = ([float]$_.rotate + 360) % 360
		$vFlip = $_.vFlip.ToLower() -eq "true"
		$hFlip = $_.hFlip.ToLower() -eq "true"
		Write-Log -Path $LogFile -Message "[$ScriptName] Processing symbol ""$($symbolId)"", rotate: $($rotate), vFile: $($vFlip), hFlip: $($hFlip)..." -Level Debug
		$transformedSymbol = GetTransformedSymbol -symbolId $symbolId -rotate $rotate -vFlip $vFlip -hFlip $hFlip
		if (
			($symbolId -ne $transformedSymbol[0]) -or
			($_.rotate -ne $transformedSymbol[1]) -or
			($_.vFlip -ne $transformedSymbol[2]) -or
			($_.hFlip -ne $transformedSymbol[3])
		) {
			Write-Log -Path $LogFile -Message "[$ScriptName] Replacing with new symbol ""$($transformedSymbol[0])"", rotate: ""$($transformedSymbol[1])"", vFlip: ""$($transformedSymbol[2])"", hFlip: ""$($transformedSymbol[3])""..." -Level Debug
			# a new symbol id is generated for the transformation
			$_.symbolId = $transformedSymbol[0]
			$_.rotate = [string]$transformedSymbol[1]
			$_.vFlip = ([string]$transformedSymbol[2]).ToLower()
			$_.hFlip = ([string]$transformedSymbol[3]).ToLower()
			++$modified
		} # else... same symbol id, no need to modify the layer
	}
	$modified
}

function ProcessSituationViews {
	param($views)
	if ([string]::IsNullOrEmpty($views)) {
		@(0, 0, 0)
		return
	}

	$modifiedViews = 0
	$modifiedLayers = 0
	$modifiedInstances = 0
	$views | ForEach-Object {
		$fileName = $_
		[xml]$xml = Get-Content "$($filename)" -Encoding UTF8
		$modified = $false
		Write-Log -Path $LogFile -Message "[$ScriptName] Processing ""$($filename)""..." -Level Debug
		# loop all layer(s) in the situation view
		$xml.viewConfiguration.view.symbolLayer | ForEach-Object {
			# process all instances in the layer
			Write-Log -Path $LogFile -Message "[$ScriptName] Processing layer ""$($_.name)""..." -Level Debug
			$modifiedCount = ProcessSymbolInstances -instances $_.entityEntry
			if ($modifiedCount -gt 0) {
				Write-Log -Path $LogFile -Message "[$ScriptName] Updated layer ""$($_.name)""" -Level Debug
				$modifiedInstances += $modifiedCount
				++$modifiedLayers
				$modified = $true
			} else {
				Write-Log -Path $LogFile -Message "[$ScriptName] Not modifying layer ""$($_.name)""" -Level Debug
			}
		}
		if ($modified) {
			Out-XmlUtf8 -xml $xml -filename "$($filename)" -removeComment $true
			Write-Log -Path $LogFile -Message "[$ScriptName] Updated ""$($filename)""" -Level Debug
			++$modifiedViews
		} else {
			Write-Log -Path $LogFile -Message "[$ScriptName] Not modifying ""$($filename)""." -Level Debug
		}
	}
	@($modifiedViews, $modifiedLayers, $modifiedInstances)
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
	$viewsCount = 0
	$layersCount = 0
	$instancesCount = 0
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

		$modified = ProcessSituationViews -views $matchedFiles
		$viewsCount += $modified[0]
		$layersCount += $modified[1]
		$instancesCount += $modified[2]

		Write-Log -Path $LogFile -Message "[$ScriptName] Processed ""$dirPath"", viewsCount: ""$($modified[0])"", layersCount: ""$($modified[1])"", instancesCount: ""$($modified[2])""." -Level Debug
	}
	Write-Log -Path $LogFile -Message "[$ScriptName] Total Views Count: $viewsCount, Total Layers Count: $layersCount, Total Instances: $instancesCount" -Level Info
} else {
	Write-Log -Path $LogFile -Message "[$ScriptName] No whitelist items defined." -Level Warn
}

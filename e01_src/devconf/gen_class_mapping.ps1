# Parameters
param (
	[string]$animatorSymbolClassDir = $Env:ANIMATORSYMBOLCLASS_DIR,
	[string]$outputFile = (Split-Path -Parent -Path $MyInvocation.MyCommand.Definition) + "\classmapping.csv"
)

$directorySet = @()
$directorySet += 'ActiveBackdrop'
$directorySet += 'ActiveNumber'
$directorySet += 'ActiveSymbol'
$directorySet += 'ActiveText'

$filePattern = '*.anc'

$result = @()
$directorySet | %{
	$dirItem = $_
	$dirPath = "$animatorSymbolClassDir\$dirItem"
	Get-ChildItem -Path "$dirPath" -Include "$filePattern" -Recurse | %{
		$filePath = $_.Fullname
		$fileName = $_.Name.Replace('.anc', '')
		$fileRelativePath = $filePath.Replace("$($dirPath)\", '').Replace('\', '/').Replace('.anc', '')

		$anClassType = "$($dirItem)Instance:$fileRelativePath"
		#Write-Output "$anClassType, $fileName"
		$result += "$anClassType, $fileName"
	}
}
#Write-Output $result

'AnClassType, MC_GraphicRep' | Out-File -Encoding ASCII "$outputFile"
$result | %{ $_ | Out-File -Encoding ASCII -Append "$outputFile" }
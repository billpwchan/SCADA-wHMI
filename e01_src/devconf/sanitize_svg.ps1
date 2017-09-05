param(
	[string]$path,
	[string]$pattern="*.svg",
	[string]$postfix=""
)

if (!$path) {
	Write-Output "Usage:
	-path     Path to the SVG directory
	-pattern  Pattern to search for (default: ""$($pattern)"")
	-postfix  Postfix to add to the modified files (default: ""$($postfix)"")
"
	exit 1
}

$mapping = @{
	"style="".*(fill:none; stroke:#FF00FF; stroke-width:\dpx; ){2,}.*"""="style=""fill:none; stroke:#FF00FF; stroke-width:3px;""";
	"style="".*(fill:none; stroke:#FF0000; stroke-width:\dpx; ){2,}.*"""="style=""fill:none; stroke:#FF0000; stroke-width:3px;"""
}

Get-ChildItem -Path $path -Include $pattern -Recurse | %{
	$fullname = $_.FullName
	$content = Get-Content $fullname
	$mapping.GetEnumerator() | %{
		$key = $_.key
		$value = $_.value
		$content = ($content) -Replace $key, $value
	}
	$content | Set-Content "$($fullname)$($postfix)"
}

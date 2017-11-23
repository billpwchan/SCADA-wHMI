function Out-XmlUtf8 {
	param(
		[xml]$xml,
		[string]$filename,
		[boolean]$removeComment=$false,
		[boolean]$omitXmlDeclaration=$false
	)

	if ($removeComment) {
		$xml.SelectNodes("//comment()") | ForEach-Object{
			$null = $_.ParentNode.RemoveChild($_)
		}
	}

	$setting = New-Object System.Xml.XmlWriterSettings
	$setting.CloseOutput = $true
	$setting.Indent = $true
	$setting.Encoding = New-Object System.Text.UTF8Encoding($false)
	$setting.OmitXmlDeclaration = $omitXmlDeclaration
	$memoryStream = New-Object System.IO.MemoryStream
	$writer = [System.Xml.XmlWriter]::Create($memoryStream, $setting)
	try {
		$xml.Save($writer)
		$memoryStream.Position = 0
		$fileContent = ([System.Text.Encoding]::UTF8).GetString($memoryStream.ToArray())
		$fileContent -Replace " xmlns=""""", "" | Out-FileUtf8NoBom $filename
	} finally {
		$writer.Close()
		$memoryStream.Dispose()
	}
}

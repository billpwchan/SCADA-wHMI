/********** callback API to receive thalesex_scrcap screen cap image **********/
function thalesex_scrcap_on_captured(dataURI) {

	const divScrCap = document.createElement('div');
	divScrCap.id = 'thalesex_scrcap-popup';
	divScrCap.innerHTML =
	'<div class="ctrl-container">' +
		'<div class="img-container">' +
			'<img id="thalesex_scrcap-img" src=""/>' +
		'</div>' +
		'<div class="btn-container">' +
			'<input type="button" class="bottom-right" value="print" onclick="thalesex_scrcap_print()" />' +
			'<input type="button" class="bottom-right" value="save" onclick="thalesex_scrcap_save()" />' +
			'<input type="button" class="bottom-right" value="close" onclick="thalesex_scrcap_close()" />' +
		'</div>' +
	'</div>'
	document.body.appendChild(divScrCap);
	document.getElementById("thalesex_scrcap-img").src = dataURI;
}

/********** API required to invoke thalesex_scrcap chrome extension **********/
function thalesex_scrcap_capture() {
	document.documentElement.setAttribute('thalesex_scrcap_capture', 1);
}

/********** UI logics **********/
function thalesex_scrcap_print() {
	var img = document.getElementById("thalesex_scrcap-img");
	var dataURI = img.src;
	if (dataURI == "") return;

	popup = window.open()
	popup.document.write("<!DOCTYPE html><html><body><img src='");
	popup.document.write(dataURI);
	popup.document.write("'/></body></html>");
	popup.print();
	popup.close();
}

function thalesex_scrcap_save() {
	var img = document.getElementById("thalesex_scrcap-img");
	var dataURI = img.src;
	if (dataURI == "") return;

	var link = document.createElement("a");
	link.download = "screenshot.png";
	link.href = dataURI;
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
	delete link;
}

function thalesex_scrcap_close() {
	var popup = document.getElementById("thalesex_scrcap-popup");
	popup.parentElement.removeChild(popup);
}


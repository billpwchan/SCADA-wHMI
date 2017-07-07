window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.DOWNLOAD = (function(){
    'use strict';

	return {
		
		'Csv' : function(jsdata) {

		var json = JSON.parse(jsdata);
	
		var jsonstring = json.OperationString3;

		var json = JSON.parse(jsonstring);

		var debugId = json.PrintDataDebugId;
		var dataColumns = json.PrintDataColumns;
		var dataIndexs = json.PrintDataIndexs;
		var dataAttachement = json.PrintDataAttachement;

	console.log('window.SCADAGEN.DOWNLOAD.Csv Reading table Start debugId', debugId);
	var divPanel = document.getElementById(debugId);

	var div1 = divPanel.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[1];
//console.log('window.SCADAGEN.DOWNLOAD.Csv Reading table Start div1', div1);

	var div2 = div1.childNodes[0].childNodes[0];
//console.log('window.SCADAGEN.DOWNLOAD.Csv Reading table Start div2', div2);

	var table = div2.childNodes[2];
		var tbody = table.tBodies[0];

		console.log('window.SCADAGEN.DOWNLOAD.Csv tbody.childNodes.length', tbody.childNodes.length);
		var lines = [];
		for ( var x = 0 ; x < tbody.childNodes.length ; ++x ) {
			var tr = tbody.childNodes[x];
//			console.log('window.SCADAGEN.DOWNLOAD.Csv tr.childNodes.length', tr.childNodes.length);
			var line = [];

			var dataIndexsLength = dataIndexs.length;
			for ( var y = 0 ; y < dataIndexsLength ; ++y ) {
//				console.log('window.SCADAGEN.DOWNLOAD.Csv y dataIndexs[y]', y, dataIndexs[y]);
				var td = tr.childNodes[dataIndexs[y]];
				var tddiv1 = td.childNodes[0];
				if ( tddiv1.childNodes.length == 1 ) {
					var tddiv2 = tddiv1.childNodes[0];
					var value = tddiv2.textContent;
					line[y] = value;
//					console.log('window.SCADAGEN.DOWNLOAD.Csv tddiv1 value', value);
				}
			}
			var data = line.join(',');
			lines[x] = data;
		}
		console.log('window.SCADAGEN.DOWNLOAD.Csv Reading table End');

		console.log('window.SCADAGEN.DOWNLOAD.Csv Create CSV Begin');
		var header = dataColumns.join(',');
		var dataString = header + "\n" + lines.join("\n");
		console.log('window.SCADAGEN.DOWNLOAD.Csv Create CSV End');
		
		console.log('window.SCADAGEN.DOWNLOAD.Csv Create Download Begin');
	//	var dataAttachement = 'data.csv';
		var a = document.createElement('a');
		a.href = 'data:attachment/csv;charset=utf-8,' + encodeURIComponent(dataString);
		a.target = '_blank';
		a.download = dataAttachement;
		document.body.appendChild(a);
		a.click();
		console.log('window.SCADAGEN.DOWNLOAD.Csv Create Download End');	

		}
	}
	
}());

window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.DOWNLOAD = (function(){
    'use strict';

	return {
		
		'Csv' : function(jsdata) {

		var json = JSON.parse(jsdata);
			
//console.log('window.SCADAGEN.DOWNLOAD.Csv json', json);
			
		var jsonstring = json.OperationString3;
			
//console.log('window.SCADAGEN.DOWNLOAD.Csv csvString', csvString);
			
//	var jsonstring = `{
//		"PrintDataDebugId": "gwt-debug-UILayoutEventSummaryViewerScsOlsListPanel"
//		, "PrintDataColumns":["scsalarmList_datetime_name", "scsalarmList_area_name", "scsalarmList_funccat_name", "scsalarmList_sourceID_name", "scsalarmList_alarmText_name", "scsalarmList_priority_name"]
//		, "PrintDataIndexs":[0,1,2,3,4,5]
//	}`;

		var json = JSON.parse(jsonstring);

		var debugId = json.PrintDataDebugId;
		var dataColumns = json.PrintDataColumns;
		var dataIndexs = json.PrintDataIndexs;
		var dataAttachement = json.PrintDataAttachement;

		console.log('window.SCADAGEN.DOWNLOAD.Csv Reading table Start debugId', debugId);
		//var div = document.getElementById('GB222GVDBB');
		var divPanel = document.getElementById(debugId);
		var div = divPanel.childNodes[1].childNodes[0].childNodes[1].childNodes[0].childNodes[2].childNodes[0].childNodes[2].childNodes[0].childNodes[1].childNodes[0].childNodes[0];

		var table = div.childNodes[2];
		var tbody = table.tBodies[0];

		console.log('window.SCADAGEN.DOWNLOAD.Csv tbody.childNodes.length', tbody.childNodes.length);
		var lines = [];
		for ( var x = 0 ; x < tbody.childNodes.length ; ++x ) {
			var tr = tbody.childNodes[x];
			console.log('window.SCADAGEN.DOWNLOAD.Csv tr.childNodes.length', tr.childNodes.length);
			var line = [];

			var dataIndexsLength = dataIndexs.length;
			for ( var y = 0 ; y < dataIndexsLength ; ++y ) {
				console.log('window.SCADAGEN.DOWNLOAD.Csv y dataIndexs[y]', y, dataIndexs[y]);
				var td = tr.childNodes[dataIndexs[y]];
				var tddiv1 = td.childNodes[0];
				if ( tddiv1.childNodes.length == 1 ) {
					var tddiv2 = tddiv1.childNodes[0];
					var value = tddiv2.textContent;
					line[y] = value;
					console.log('window.SCADAGEN.DOWNLOAD.Csv tddiv1 value', value);
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

			
/*
//console.log('window.SCADAGEN.DOWNLOAD.Csv jsdata', jsdata);
			
var json = JSON.parse(jsdata);
			
//console.log('window.SCADAGEN.DOWNLOAD.Csv json', json);
			
var csvString = json.OperationString3;
			
//console.log('window.SCADAGEN.DOWNLOAD.Csv csvString', csvString);
			
var csvJson = JSON.parse(csvString);
			
var header = csvJson.Header;
var data = csvJson.Contents;
			
//	console.log('window.SCADAGEN.DOWNLOAD.Csv header', header);
//console.log('window.SCADAGEN.DOWNLOAD.Csv data', data);

var dataString = header + "\n" + data.join("\n");

var csvFile = 'data.csv';
var a = document.createElement('a');
a.href = 'data:attachment/csv;charset=utf-8,' + encodeURIComponent(dataString);
a.target = '_blank';
a.download = csvFile;
document.body.appendChild(a);
a.click();
*/
		}
	}
	
}());

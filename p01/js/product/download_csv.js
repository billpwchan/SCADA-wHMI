window.SCADAGEN = window.SCADAGEN || {};
window.SCADAGEN.DOWNLOAD = (function(){
    'use strict';

	return {
		
		'Csv' : function(jsdata) {

var logPrefix = 'window.SCADAGEN.DOWNLOAD.Csv';

var json = JSON.parse(jsdata);

var jsonstring = json.OperationString3;

var json = JSON.parse(jsonstring);

var debugId = json.PrintDataDebugId;
var dataColumns = json.PrintDataColumns;
var dataIndexs = json.PrintDataIndexs;
var dataAttachement = json.PrintDataAttachement;

// Navigator to the table
var indexes = [1,0,1,0,2,0,1,0,0,2]; // SCADAgen
//var indexes = [1,0,2,0,2,0,1,0,0,2]; // ISCS
for ( var index = 0 ; index < indexes.length ; ++index ) {
	var indexNext = index+1;
//	console.log(logPrefix, ' Reading index[', index, '], indexNext[', indexNext, ']');
	var ci = indexes[index];
//	console.log(logPrefix, ' Reading index[', index, '], ci', ci, ']');
	div[indexNext]=div[index].childNodes[ci];
//	console.log(logPrefix, ' div[', indexNext, ']', div[indexNext]);
}
//console.log(logPrefix, ' Reading div[', index, '][', div[index], ']');

// Extract the table for data extract
var tbody = div[index].tBodies[0];
console.log(logPrefix, ' tbody.childNodes.length', tbody.childNodes.length);

var lines = [];
for ( var x = 0 ; x < tbody.childNodes.length ; ++x ) {
	var tr = tbody.childNodes[x];
//			console.log(logPrefix, ' tr.childNodes.length', tr.childNodes.length);
	var line = [];
	if ( tr ) {
		if ( tr.childNodes ) {
			if ( tr.childNodes.length > 0 ) {
				var dataIndexsLength = dataIndexs.length;
				for ( var y = 0 ; y < dataIndexsLength ; ++y ) {
//						console.log(logPrefix, ' y dataIndexs[y]', y, dataIndexs[y]);
					var td = tr.childNodes[dataIndexs[y]];
					if ( td ) {
						var tddiv1 = td.childNodes[0];
						if ( tddiv1.childNodes.length == 1 ) {
							var tddiv2 = tddiv1.childNodes[0];
							var value = tddiv2.textContent;
							line[y] = value;
//								console.log(logPrefix, ' tddiv1 value', value);
						}
					} else {
						console.log(logPrefix, ' td IS INVALID');
					}
				}
			} else {
				console.log(logPrefix, ' tr.childNodes.length IS ZERO');
			}
		} else {
			console.log(logPrefix, ' tr.childNodes IS INVALID');
		}
	} else {
		console.log(logPrefix, ' td IS INVALID');
	}

	var data = line.join(',');
	lines[x] = data;
}
console.log(logPrefix, ' Reading table End');

console.log(logPrefix, ' Create CSV Begin');
var header = dataColumns.join(',');
var dataString = header + "\n" + lines.join("\n");
console.log(logPrefix, ' Create CSV End');

console.log(logPrefix, ' Create Download Begin');
//	var dataAttachement = 'data.csv';
var a = document.createElement('a');
a.href = 'data:attachment/csv;charset=utf-8,' + encodeURIComponent(dataString);
a.target = '_blank';
a.download = dataAttachement;
document.body.appendChild(a);
a.click();
console.log(logPrefix, ' Create Download End');	

		}
	}
	
}());

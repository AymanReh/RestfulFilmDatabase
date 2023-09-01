function getFilmTable(heading, row){
	var table = "<table border='1' class='filmTable'>" + getHeadings(heading) +getBody(row) + "</table>";
	return(table);
}

function getHeadings(heading){
	var heading = "<tr>";
	for(var i = 0 ; i <heading.length; i++){
		heading += "<th>" + heading[i] + "</th>";
	}
	heading += "</tr>\n";
	return(heading);
}


function getBody(row){
	var filmBody ="";
	for(var i=0; i <row.length;i++){
		filmBody += "<tr>";
		var row=row[i];
		for(var x=0;x<row.length;x++){
			filmBody += "<td>" + row[x] + "</td>";
		}
		filmBody += "</tr>\n";
	}
	return(filmBody);
}


function filmTable(row){
	var filmHeadings = ["id", "title", "year", "director", "stars", "review"];
	return (getTable(filmHeadings));
}

function xmlFilmTable(location, data1){
	var address=filmsAPIController;
	var filmData = makeParamString(data1, "xml");
	filmRequest(address, filmData, function(request) { showXmlFilms(request, location);});
}

function jsonFilmTable(format, location) {
  var address = "filmsAPIController";
  var data = getValue(format);
  var finalAddress = address + "?Accept=" + data;
  filmRequest(finalAddress, data, function(req) { 
	showJsonFilms(req, location, data); });
}
function getRequestObject() {
  if (window.XMLHttpRequest) {
    return(new XMLHttpRequest());
  } else if (window.ActiveXObject) { 
    return(new ActiveXObject("Microsoft.XMLHTTP"));
  } else {
    return(null); 
  }
}

function showJsonFilms(req, location, format){
	if ((req.readyState == 4) && (req.status == 200)){
		var rawJson = req.responseText;
		var json = JSON.parse(rawJson);
		//var film = json[0];
		//console.log(film.id);
		//console.log(film.title);
		///var jsonTable = getFilmTable(json.heading, json.allFilm);

		
			
		var filmResult="";
		var filmFormat= format;
		
		$.each(json, function(i, films){
			filmResult +="<br/>"+ "Id: " + films.id+ "<br/>";
			filmResult +="Title: " + films.title + "<br/>";
			filmResult +="Year: " + films.year + "<br/>";
			filmResult +="Director: " + films.director + "<br/>";
			filmResult +="Stars: " + films.stars + "<br/>";
			filmResult +="Review: " + films.review + "<br/>";
			filmResult += "<input type='button' value='Update' id='update' onClick='sendPut("+films.id+ ",\""+format+"\")'>"
			filmResult +=" " +"<br/>";
			filmResult += "<input type='button' value='Delete' id='delete' onClick='sendDelete("+films.id+ ",\""+format+"\")'>"
			filmResult +=" " +"<br/>";
		});

		$('#'+location).html(filmResult);
		
	}
}
function sendPut(ids, format){
	jsonPut(ids,format);

}

function sendDelete(ids, format){
	jsonDelete(ids,format);

}



function filmRequest(fAddress,location, dataResponse){
	var req = getRequestObject();
	console.log(dataResponse);
	req.onreadystatechange = function(){ dataResponse(req)}
	req.open("GET",fAddress,true);
	req.send(null);
}

function showRT(req, location){
	if((req.readyState==4) &&
	(req.status == 200)) {
		insert(location, req.responseText);
	}
}


function insert(filmId, filmData){
	document.getElementById(filmId).innerHTML = filmData;
}


function stateChange(req, location){
	showRT(req, location);
}


function getValue(format){
	return (escape(document.getElementById(format).value))
}



//This functions role is to retrieve the id and format from getFilms so that the user does not
// have to input the id and format themselves and to convert the id into a json object using json
//stringify and send it to showJsonFilmsDelete function and filmRequestDelete.
function jsonDelete(getId, format){
	var address = "filmsAPIController";
	console.log(format);
	//var finalAddress = address + "?Content-Type=" + data;
	var id = getId;

	var jsonFilm ={
		id: id,

    };
    var jsonData = JSON.stringify(jsonFilm);
	filmRequestDelete(address, format,function(req) { 
	showJsonFilmsDelete(req); }, jsonData);
	
}


//This functions job is to retrieve the jsonData from jsonDelete, initialise req to allow 
//this function to work on different web browsers. the onreadystatechange to make sure a connection
//is made with database. req.open is used to open a connection with the database and to tell it to
//send a delete request to the address given in the variable fAddress. Next the content-type is changed to
//the format which is application/json and finally the data is sent to the restful webservice.
	function filmRequestDelete(fAddress,data, dataResponse, jsonData){
	var req = getRequestObject();
	req.onreadystatechange = function(){ dataResponse(req); };
	req.open("DELETE", fAddress, true);
	req.setRequestHeader("Content-Type", data);
	console.log(jsonData);
	req.send(jsonData);
}

// This function job is to make sure that a connection is made to the database though making sure that the readystate
//is set to 4 and the status is set to 200. Next the data is retrieved from the req variable whichw as sent from
//jsonDelete and is then stringifyied into a json object and placed into teh jsonConvert variable and is finally returned
//to the jsonDelete function which sends it to filmRequestDelete.
function showJsonFilmsDelete (req) {
if ((req.readyState == 4) && (req.status == 200)) {
	var data = req.responseText;
	var jsonData = eval("(" + data + ")");
	var jsonFilm ={
		id: jsonData.id,
    };
    var jsonConvert = JSON.stringify(data);
    console.log(jsonFilm);
    console.log(jsonConvert);
	return(jsonConvert);
	
	
	
	}
	}
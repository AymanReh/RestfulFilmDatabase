

//This function is meant to get the id and format from the getFilms.js page and put
// them into their own variable (id). And the title, year, director, stars and review
// are retrieved through a prompt funvction which is similar to an alert inwhic a popup appears
// which allows users to enter the details they wish to update, but must enter all details except
// for the id. Once they are all put into their own variables they arte put into 1 larger variable
// called jsonFilm and it is then stringified to make it into a json object. Next its sent to 
//show JsonFilmsPut and after that all the data is sent to filmRequestPut.
function jsonPut(getId, format){
	var address = "filmsAPIController";
	console.log(format);
	//var finalAddress = address + "?Content-Type=" + data;
	var id = getId;
	var title = prompt("Title:");
	var year = prompt("Year:");
	var dir = prompt("Director:");
	var stars = prompt("Stars:");
	var review = prompt("Review:");
	
	console.log(title);
	var jsonFilm ={
		id: id,
        title: title,
        year: year ,
        director: dir,
        stars: stars,
        review: review
    };
    var jsonData = JSON.stringify(jsonFilm);
	filmRequestPut(address, format,function(req) { 
	showJsonFilmsPut(req); }, jsonData);
	
}


	//this functions job is to first get the request object so the data can
	// be used in a variety of different web browsers. Next it checks the
	//onreadystatechange to make sure a connection is made with the
	//server. After that a put request is sent to the server which is specified in the
	//fAddress variable. Next the request header. Content-type is set to the format (application/json)
	// finally the data is sent to the restful Webservice.
	function filmRequestPut(fAddress,data, dataResponse, jsonData){
	var req = getRequestObject();
	req.onreadystatechange = function(){ dataResponse(req); };
	req.open("PUT", fAddress, true);
	req.setRequestHeader("Content-Type", data);
	console.log(jsonData);
	req.send(jsonData);
}

// This functions job is to first make sure a connection is made to teh database by checking if
// the readystate is 4 and the status is 200. Next it puts the updated data into a singular variable called data
// by using the responseText function to get the films data. Next it turns it into a JSON object using the JSON
// stringify function. Finally it returns the stringified function in a variable called jsonConvert.
function showJsonFilmsPut (req) {
if ((req.readyState == 4) && (req.status == 200)) {
	var data = req.responseText;
	var jsonData = eval("(" + data + ")");
	var jsonFilm ={
		id: jsonData.id,
        title: jsonData.title,
        year: jsonData.year ,
        director: jsonData.dir,
        stars: jsonData.stars,
        review: jsonData.review
    };
    var jsonConvert = JSON.stringify(data);
    console.log(jsonFilm);
    console.log(jsonConvert);
	return(jsonConvert);
	
	
	
	}
	}
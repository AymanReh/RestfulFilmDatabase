
//The data from create.html arrives here first. The data is first put into variables, 
//then is put into 1 large variable called jsonFilm. It is then stringifed into the
// variable called jsonData, this gets raw data aand converts it to a json object.
// Next the address, data which is the format being used and the data response 
//which is the responsejavascript makes when asked to create the json object
// it is explained further in the showJsonFilmsPost function.
function jsonPostFilm(format) {
	var address = "filmsAPIController";
	var data = getValue(format);
	//var finalAddress = address + "?Content-Type=" + data;
	var title =document.getElementById("title").value;
	var year = document.getElementById("year").value;
	var dir = document.getElementById("dir").value;
	var stars = document.getElementById("star").value;
	var review = document.getElementById("review").value;
	var jsonFilm ={
        title: title,
        year: year ,
        director: dir,
        stars: stars,
        review: review
    };
    var jsonData = JSON.stringify(jsonFilm);
	filmRequestPost(address, data,function(req) { 
	showJsonFilmsPost(req); }, jsonData);
	}
	
//This functions job is to send the json object to the restful web service
//using a POST request it does this by first making req a variable to allow
// the javascript to work on browsers. Next it runs the onreadstatechange function
// to retrieve all the json object data. After that is opens a connection to the 
// restful webservice and makes a post request, it sends the address it wants to connect to
// and sets the state to true to open the connection. Now it changes the Content-type header
// to the format that is being sent (application/json). Finally the data is sent.
	function filmRequestPost(fAddress,data, dataResponse, jsonData){
	var req = getRequestObject();
	req.onreadystatechange = function(){ dataResponse(req); };
	req.open("POST", fAddress, true);
	req.setRequestHeader("Content-Type", data);
	req.send(jsonData);
}

//This functions job is to first see if there is a connection to the server due to the
// readystate needing to be 4 and the status needing to be 200. Then the data is retrieved
// and put into the data variable. Next i used the eval function to return the json object
// as a completed string. After that the title, year, director, stars and review is put into
// a larger variable called jsonFilm and is stringified to make it into a json object
// finally the json object is put into a variable called jsonConvert and is returned.
function showJsonFilmsPost (req) {
if ((req.readyState == 4) && (req.status == 200)) {
	var data = req.responseText;
	var jsonData = eval("(" + data + ")");
	var jsonFilm ={
        title: jsonData.title,
        year: jsonData.year ,
        director: jsonData.dir,
        stars: jsonData.stars,
        review: jsonData.review
    };
    var jsonConvert = JSON.stringify(data);
	return(jsonConvert);
	
	
	
	}
	}
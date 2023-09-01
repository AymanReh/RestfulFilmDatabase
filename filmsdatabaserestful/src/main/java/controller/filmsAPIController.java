package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.FilmDAO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import model.Film;
import model.filmsList;

@WebServlet("/filmsAPIController")
public class filmsAPIController extends HttpServlet {
	private static final long serialVersionUID = 1L;
//This get method is used to get every film from the films database and output it in the format the user is request to output it in. 
//If the user enters application/json then the method would use gson to convert the data from raw information to raw json data and output it to the user.
//Next if the user enters application/xml the method would retrieve the films database data and convert it to raw xml data 
//and print it to the console using a stringwriter
//Finally if the user wishes to output the data in text/plain then the data would be treieved from the database and split using the split function inside of java
//the id is separated using a # so that it can be accessed later on and each piece of data afterwards is split using a | symbol. I also used a for each loop to
//loop through every film and convert it to text plain .
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		String format = request.getParameter("Accept");
		PrintWriter out = response.getWriter();
		FilmDAO dao = new FilmDAO();
		ArrayList<Film> allFilms = dao.getAllFilms();
		filmsList filmL = new filmsList(allFilms);
		StringWriter stringW = new StringWriter();
		request.setAttribute("allFilms", allFilms);
		if (format.equals("application/json")) {
			response.setContentType("application/json");
			Gson gson = new Gson();
			String json = gson.toJson(allFilms);
			out.write(json);
			
			
			
		} else if (format.equals("application/xml")) {
			response.setContentType("application/xml");
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(filmsList.class);
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(filmL, stringW);
				System.out.println(stringW.toString());
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} else if (format.equals("text/plain")) {
			response.setContentType("text/plain");
			String data ="";
			for (Film i : allFilms) {
				data += "#" + i.getId() + "|" + i.getTitle() + "|" + i.getYear()+ "|" + i.getDirector()+ "|" + i.getStars()+ "|" + i.getReview() + "\n";

			}
			
			out.write(data);
		}
		out.close();
	}
//This it the post method and it is used to insert films into the database it does this by first retrieving the title, year, director, stars and review
//and then inserting it in whatever format it is submitted in. either json, xml or text. The method finds out which format it is submitted in by checking
//the content type header. If it is application/json the data is retrieved and converted from json to raw data using the fromjson command inside of gson and
//then submitted into the database using the insertFilm method inside of FilmDAO.java. If the data is XML data it converts it from xml data using an unmarshaller
// and jaxb unmarshaller library and then finally inserts the data into the insertFilm method inside of FilmDAO, the data that is inserted is raw data which the method
// understands. Finally if the user inserts text/plain data then the post method splits the data using the \\| and gets the raw data and then finally inserting it to
//the insertFilm method. i also used a printwriter to inform the user if the data has been inserted successfully.
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		FilmDAO dao = new FilmDAO();
		PrintWriter out = response.getWriter();
		String content = request.getHeader("Content-type");
		int id;
		String title;
		int year;
		String dir;
		String stars;
		String review;
		Film f = new Film();
		if (content.equals("application/json")) {
			response.setContentType("application/json");
			Gson gson = new Gson();
			Film jsonF = gson.fromJson(data, Film.class);
			id = (jsonF.getId());
			title = (jsonF.getTitle());
			year = (jsonF.getYear());
			dir = (jsonF.getDirector());
			stars = (jsonF.getStars());
			review = (jsonF.getReview());
			f = new Film(id, title, year, dir, stars, review);
		} else if (content.equals("application/xml")) {
			response.setContentType("application/xml");
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film XMLf = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				title = (XMLf.getTitle());
				year = (XMLf.getYear());
				dir = (XMLf.getDirector());
				stars = (XMLf.getStars());
				review = (XMLf.getReview());
				f = new Film(0, title, year, dir, stars, review);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} else if (content.equals("text/plain")) {
			response.setContentType("text/plain");
			String[] textF = data.split("\\|");
			title = textF[0];
			year = Integer.valueOf(textF[1]);
			dir = textF[2];
			stars = textF[3];
			review = textF[4];
			f = new Film(0, title, year, dir, stars, review);
		}
		try {
			dao.insertFilm(f);
			out.write("Successfully inserted film");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.close();
	}
// This function is used to update an existing film in the films database, it does this by specifying what content type is being submitted and then
// if the criteria meets the if statement it runs the data inside of them. inside of application/json it does this by using the fromJson command
// from the gson library it retrieves the id, title, year, director, stars and review and converts it to readable data.For application/xml it
// retrieve the data and convert it from xml data to readable data which the film database can read. Finally text/plain also retrieves the data and 
// converts the data from raw text/plain data to readable film database data using the split function. All the data is then sent to the updateFilm method
// and a printwriter is used to inform the user the film has been updated.
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String data = req.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		String Contentype = req.getHeader("Content-type");
		FilmDAO dao = new FilmDAO();
		PrintWriter out = resp.getWriter();
		int id;
		String title;
		int year;
		String dir;
		String stars;
		String review;
		Film f = new Film();
		if (Contentype.equals("application/json")) {
			resp.setContentType("application/json");
			Gson gson = new Gson();
			Film jsonF = gson.fromJson(data, Film.class);
			id = (jsonF.getId());
			title = (jsonF.getTitle());
			year = (jsonF.getYear());
			dir = (jsonF.getDirector());
			stars = (jsonF.getStars());
			review = (jsonF.getReview());
			f = new Film(id, title, year, dir, stars, review);
			f.setId(id);
		}else if (Contentype.equals("application/xml")) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film XMLf = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				id = (XMLf.getId());
				title = (XMLf.getTitle());
				year = (XMLf.getYear());
				dir = (XMLf.getDirector());
				stars = (XMLf.getStars());
				review = (XMLf.getReview());
				f = new Film(id, title, year, dir, stars, review);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} else if (Contentype.equals("text/plain")) {
			resp.setContentType("text/plain");
			String[] textF = data.split("\\|");
			id = Integer.valueOf(textF[0]);
			title = textF[1];
			year = Integer.valueOf(textF[2]);
			dir = textF[3];
			stars = textF[4];
			review = textF[5];
			f = new Film(id, title, year, dir, stars, review);
		}
		try {
			dao.updateFilm(f);
			out.write("Successfully updated films");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.close();
	}

// The doDelete method is used for when the user wants to delete a film from the database. It only requires the id since the id is unique
// and if finds the format the user wants to enter through the content-type header. If application/json is entered the json is
// Retrieved from data string and then is converted from json to readable data then sent to the database. If application/xml is
// used then the data is retrieved and converted from xml to readable data, finally is then sent to the database and deleted from it.
// Finally if text/plain is inserted as the content-type then the id data is retrieved and converted from text/plain to readable text
// and finally is sent to the database using the filmDAO method deleteFilm and the object f is sent which contains the id. Finally
// a print writer is used to inform the user the film is deleted.
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
		String Contentype = request.getHeader("Content-type");
		FilmDAO dao = new FilmDAO();
		PrintWriter out = response.getWriter();
		int id;
		Film f = new Film();
		if (Contentype.equals("application/json")) {
			response.setContentType("application/json");
			Gson gson = new Gson();
			Film jsonF = gson.fromJson(data, Film.class);
			id = (jsonF.getId());
			f = new Film();
			f.setId(id);
		}

		if (Contentype.equals("application/xml")) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(Film.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Film XMLf = (Film) jaxbUnmarshaller.unmarshal(new StringReader(data));
				id = (XMLf.getId());
				f = new Film();
				f.setId(id);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}else if (Contentype.equals("text/plain")) {
			response.setContentType("text/plain");
			String[] textF = data.split("#");
			id = Integer.valueOf(textF[0]);
			f = new Film();
			f.setId(id);
		}
		try {
			dao.deleteFilm(f);
			out.write("Successfully delete films");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.close();

	}
}

package database;



import java.util.ArrayList;

import model.Film;

import java.sql.*;


public class FilmDAO {
	
	
	Film getId;
	Film oneFilm = null;
	Connection conn = null;
    Statement stmt = null;
	String user = "rehmanay";
    String password = "prugseRp9";
    // Note none default port used, 6306 not 3306
    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

	public FilmDAO() {}

	
	private void openConnection(){
		// loading jdbc driver for mysql
		try{
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch(Exception e) { System.out.println(e); }

		// connecting to database
		try{
			// connection string for demos database, username demos, password demos
 			conn = DriverManager.getConnection(url, user, password);
		    stmt = conn.createStatement();
		} catch(SQLException se) { System.out.println(se); }
		//return stmt;	   
    }
	private void closeConnection(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	private Film getNextFilm(ResultSet rs){
    	Film thisFilm=null;
		try {
			thisFilm = new Film(
					rs.getInt("id"),
					rs.getString("title"),
					rs.getInt("year"),
					rs.getString("director"),
					rs.getString("stars"),
					rs.getString("review"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return thisFilm;		
	}
	
	
//This method is used to get all the films from the database and loops until it is done
   public ArrayList<Film> getAllFilms(){
	   
		ArrayList<Film> allFilms = new ArrayList<Film>();
		openConnection();
		
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films";
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	allFilms.add(oneFilm);
		   }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return allFilms;
   }

   public ArrayList<Film> getFilmByID(int id){
	   
	   ArrayList<Film> specFilm = new ArrayList<Film>();
		openConnection();
		oneFilm=null;
	    // Create select statement and execute it
		try{
		    String selectSQL = "select * from films where id="+id;
		    ResultSet rs1 = stmt.executeQuery(selectSQL);
	    // Retrieve the results
		    while(rs1.next()){
		    	oneFilm = getNextFilm(rs1);
		    	specFilm.add(oneFilm);
		    }

		    stmt.close();
		    closeConnection();
		} catch(SQLException se) { System.out.println(se); }

	   return specFilm;
   }
//This method is used to insert a new film into the database it does this by using the Insert into statement and inserts each part
//of the film into the database.
   public boolean insertFilm(Film f) throws SQLException {

	   openConnection();
		boolean b = false;
		try {
			String sql = "insert into films (title, year, director, stars, review) values ('"+ f.getTitle() + "','" + f.getYear() + "','" + f.getDirector() + "','" + f.getStars() +"','" + f.getReview() + "');";
			System.out.println(sql);
			b = stmt.execute(sql);
			closeConnection();
			b = true;
		} catch (SQLException se) {
			throw new SQLException("Film Not Added");
		}
		return b;
	}

//This method is used to update an existing film into the films database it get the 
//id, title, year director, stars and review and then updates the specific film
public boolean updateFilm(Film f) throws SQLException{
	
	openConnection();
		boolean b = false;
		try {
		String sql = "UPDATE films SET id = '"+ f.getId() +"', title = '"+ f.getTitle() +"', year = '"+ f.getYear() + "', director = '"+ f.getDirector() + "', review = '"+ f.getReview() + "', stars = '"+ f.getStars() +"' WHERE id = '"+ f.getId() + "';";
		System.out.println(sql);
		b = stmt.execute(sql);
		closeConnection();
		b = true;
	} catch (SQLException s) {
		throw new SQLException("Films Not Updated");
	}
		return b;
}
//This is used to delete a film from the database it uses a sql statement with 
//the delete from function and where to specify which id it is since it is unique
public boolean deleteFilm(Film f) throws SQLException{
	
	openConnection();
		boolean b = false;
		try {
		String sql = "DELETE FROM films WHERE (id = '" + f.getId() + "');";
		System.out.println(sql);
		b = stmt.execute(sql);
		closeConnection();
		b = true;
	} catch (SQLException s) {
		throw new SQLException("Films Not Updated");
	}
		return b;
}
}

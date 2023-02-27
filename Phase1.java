package comp440project1;
import java.sql.*;

public class Phase1 {
	  private static final String DB_URL = "jdbc:mysql://localhost:3306/project1";
	  private static final String DB_USER = "root";
	  private static final String DB_PASSWORD = "mysql";
	  
	  public static void createTables() throws SQLException {
		  try {
			  // create database connection
			  Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			  // sql
		      Statement stmt = conn.createStatement();
		      String createTableSQL = "CREATE TABLE IF NOT EXISTS user ("
		              + "username VARCHAR(50) PRIMARY KEY,"
		              + "password VARCHAR(255),"
		              + "firstName VARCHAR(50),"
		              + "lastName VARCHAR(50),"
		              + "email VARCHAR(100) UNIQUE"
		              + ")";
		      stmt.executeUpdate(createTableSQL);
		      stmt.close();
		  } catch (SQLException ex) {
		      	System.out.println("SQL error: " + ex.getMessage());
		    }
	  }
	  public static void clearTables() throws SQLException {
	    try {
		    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		    
	        Statement stmt = conn.createStatement();
	        String clearTableSQL = "DELETE FROM user";
	        stmt.executeUpdate(clearTableSQL);
	        stmt.close();
	    } catch (SQLException ex) {
		      System.out.println("SQL error: " + ex.getMessage());
	      }
	  }
	  public static void initializeDatabase() throws SQLException {
		  clearTables();
		  createTables();
	  }
	  
	  public static void registerUser(String username, String password, String firstName, String lastName, String email) {
		  try {
		      Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		      
		      PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)");
		      stmt.setString(1, username);
		      stmt.setString(2, password);
		      stmt.setString(3, firstName);
		      stmt.setString(4, lastName);
		      stmt.setString(5, email);
		      
		      int affectedRows = stmt.executeUpdate();
	
		      if (affectedRows == 1) 
		    	  System.out.println("User registered successfully.");
		      else 
		    	  System.out.println("Failed to register user.");
		      
		      conn.close(); 
	    } catch (SQLException ex) {
	    	  System.out.println("SQL error: " + ex.getMessage());
	    }
	  }
	  
	  public static void loginUser(String username, String password) {
		  try {
		      Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		      
		      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
		      stmt.setString(1, username);
		      stmt.setString(2, password);
		      
		      ResultSet rs = stmt.executeQuery();
		      
		      if (rs.next()) 
		    	  System.out.println("User logged in successfully.");
		      else 
		    	  System.out.println("Invalid username or password.");
		      
		      conn.close();  
	    } catch (SQLException ex) {
	    	 System.out.println("SQL error: " + ex.getMessage());
	    }
	  }
	  
	  public static void main(String[] args) throws SQLException {
		  
		//create database (also re-creates)
		initializeDatabase();  
	    
		// register a new user
	    registerUser("johnsmith", "password123", "John", "Smith", "johnsmith@example.com");
	    
	    // try to register a user with the same username
	    registerUser("johnsmith", "password456", "John", "Doe", "johndoe@example.com");
	    
	    // test login correct username and password
	    loginUser("johnsmith", "password123");
	    
	    // test login incorrect username and password
	    loginUser("johnsmith", "wrongpassword");
	    
	    //initializeDatabase();
	  }
}

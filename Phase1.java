package comp440project1;
import java.sql.*;
import java.time.LocalDate;

import javax.swing.JOptionPane;

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
		      String createItemTable = "CREATE TABLE IF NOT EXISTS Item ("
                      + "id INT AUTO_INCREMENT PRIMARY KEY,"
                      + "title VARCHAR(255),"
                      + "description TEXT,"
                      + "category VARCHAR(255),"
                      + "price DECIMAL(10,2),"
                      + "username VARCHAR(255),"
                      + "FOREIGN KEY (username) REFERENCES user(username))";
		      stmt.executeUpdate(createItemTable);
		      String createReviewTable = "CREATE TABLE IF NOT EXISTS Review ("
                      + "id INT AUTO_INCREMENT PRIMARY KEY,"
                      + "rating ENUM('excellent', 'good', 'fair', 'poor'),"
                      + "description TEXT,"
                      + "username VARCHAR(255),"
                      + "itemId INT,"
                      + "FOREIGN KEY (username) REFERENCES user(username),"
                      + "FOREIGN KEY (itemId) REFERENCES Item(id))";
		      stmt.executeUpdate(createReviewTable);
		      String createPostLimitTable = "CREATE TABLE IF NOT EXISTS PostLimit ("
                      + "id INT AUTO_INCREMENT PRIMARY KEY,"
                      + "username VARCHAR(255),"
                      + "postCount INT DEFAULT 0,"
                      + "lastPostDate DATE,"
                      + "FOREIGN KEY (username) REFERENCES user(username))";
		      stmt.executeUpdate(createPostLimitTable);
		      String createReviewPostingTable = "CREATE TABLE IF NOT EXISTS ReviewPosting ("
                      + "id INT AUTO_INCREMENT PRIMARY KEY,"
                      + "username VARCHAR(255),"
                      + "itemId INT,"
                      + "reviewId INT,"
                      + "postingDate DATE,"
                      + "FOREIGN KEY (username) REFERENCES user(username),"
                      + "FOREIGN KEY (itemId) REFERENCES Item(id),"
                      + "FOREIGN KEY (reviewId) REFERENCES Review(id))";
		      stmt.executeUpdate(createReviewPostingTable);
		      stmt.close();
		  } catch (SQLException ex) {
		      	System.out.println("SQL error: " + ex.getMessage());
		    }
	  }
	  public static void clearTables() throws SQLException {
	    try {
		    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
      
	     // Disable foreign key constraints
	        Statement disableFKStmt = conn.createStatement();
	        disableFKStmt.execute("SET foreign_key_checks = 0");
	        disableFKStmt.close();
	        
	        // Delete data from tables
	        Statement stmt = conn.createStatement();
	        String clearTableSQL = "DELETE FROM user";
	        stmt.executeUpdate(clearTableSQL);
	        String clearItemTable = "DELETE FROM Item";
	        stmt.executeUpdate(clearItemTable);
	        String clearReviewTable = "DELETE FROM Review";
	        stmt.executeUpdate(clearReviewTable);
	        String clearPostLimitTable = "DELETE FROM PostLimit";
	        stmt.executeUpdate(clearPostLimitTable);
	        String clearReviewLimitTable = "DELETE FROM ReviewPosting";
	        stmt.executeUpdate(clearReviewLimitTable);
	        stmt.close();
	        
	        // Re-enable foreign key constraints
	        Statement enableFKStmt = conn.createStatement();
	        enableFKStmt.execute("SET foreign_key_checks = 1");
	        enableFKStmt.close();
	        
	        conn.close();
	    } catch (SQLException ex) {
		      System.out.println("SQL error: " + ex.getMessage());
	      }
	  }
	  public static void initializeDatabase() throws SQLException {
		  clearTables();
		  createTables();
	  }
	  
	  public static int registerUser(String username, String password, String firstName, String lastName, String email) {
		  try {
		      Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		      
		      // prepared statements with parameterized queries prevents SQL Injection
		      PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (username, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)");
		      stmt.setString(1, username);
		      stmt.setString(2, password);
		      stmt.setString(3, firstName);
		      stmt.setString(4, lastName);
		      stmt.setString(5, email);
		      
		      int affectedRows = stmt.executeUpdate();
	
		      if (affectedRows == 1) {
		    	  //User registered successfully
		    	  conn.close();
		    	  return 1;
		      }
		      else {
		    	  //Failed to register user
		    	  conn.close();
		    	  return 0;
		      }
	    } catch (SQLException ex) {
	    	  System.out.println("SQL error: " + ex.getMessage());
	    }
		return 0;
	  }
	  public static String loggedUser;
	  public static int loginUser(String username, String password) {
		  try {
		      Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		      
		   // prepared statements with parameterized queries prevents SQL Injection
		      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
		      stmt.setString(1, username);
		      stmt.setString(2, password);
		      
		      ResultSet rs = stmt.executeQuery();
		      
		      if (rs.next()) {
		    	 // User logged in successfully
		    	  conn.close(); 
		    	  loggedUser = username;
		    	  return 1;
		      }
		      else {
		    	  //Invalid username or password
		    	  conn.close(); 
		    	  return 0;
		      }
	    } catch (SQLException ex) {
	    	 System.out.println("SQL error: " + ex.getMessage());
	    }
		return 0;
	  }
	//-----------------------------------------PHASE 2 METHODS--------------------------------------------------------------------------------
	  public static int insertItem(String title, String description, String category, double price, String username) {
		    try {
		        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

		        // Check if the user has reached the limit of 3 posts for the day
		        PreparedStatement limitStmt = conn.prepareStatement("SELECT SUM(postCount) as postCount FROM PostLimit WHERE username = ? AND lastPostDate = ?");
		        limitStmt.setString(1, username);
		        limitStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
		        ResultSet limitRs = limitStmt.executeQuery();

		        int postCount = 0;
		        if (limitRs.next()) {
		            postCount = limitRs.getInt("postCount");
		        }

		        if (postCount >= 3) {
		            // User has reached the limit of 3 posts for the day
		            conn.close();
		            return 0;
		        }

		        // Insert the item into the Item table
		        PreparedStatement itemStmt = conn.prepareStatement("INSERT INTO Item (title, description, category, price, username) VALUES (?, ?, ?, ?, ?)");
		        itemStmt.setString(1, title);
		        itemStmt.setString(2, description);
		        itemStmt.setString(3, category);
		        itemStmt.setDouble(4, price);
		        itemStmt.setString(5, username);
		        int itemAffectedRows = itemStmt.executeUpdate();

		        if (itemAffectedRows == 1) {
		            // Update the post count in the PostLimit table
		            PreparedStatement limitUpdateStmt = conn.prepareStatement("INSERT INTO PostLimit (username, postCount, lastPostDate) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE postCount = ?");
		            limitUpdateStmt.setString(1, username);
		            limitUpdateStmt.setInt(2, 1);
		            limitUpdateStmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
		            limitUpdateStmt.setInt(4, postCount + 1);
		            int limitAffectedRows = limitUpdateStmt.executeUpdate();

		            if (limitAffectedRows == 1) {
		                // Item inserted successfully and post count updated
		                conn.close();
		                return 1;
		            }
		        }

		        // Failed to insert item or update post count
		        conn.close();
		        return 0;
		    } catch (SQLException ex) {
		        System.out.println("SQL error: " + ex.getMessage());
		    }
		    return 0;
		}
	  public static String searchItem(String searchQuery) {
		    String result = "";
		    try {
		        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

		        // Use INSTR function to search for searchQuery in each column
		        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Item WHERE INSTR(title, ?) > 0 OR INSTR(description, ?) > 0 OR INSTR(category, ?) > 0");
		        stmt.setString(1, searchQuery);
		        stmt.setString(2, searchQuery);
		        stmt.setString(3, searchQuery);

		        ResultSet rs = stmt.executeQuery();
		        while (rs.next()) {
		            result += "Title: " + rs.getString("title") + "\n";
		            result += "Description: " + rs.getString("description") + "\n";
		            result += "Category: " + rs.getString("category") + "\n";
		            result += "Price: " + rs.getDouble("price") + "\n";
		            result += "ID: " + rs.getInt("id")+ "\n\n";
		        }
		        conn.close();
		        if (result.equals("")) {
		            result = "No results found.";
		        }
		        return result;
		    } catch (SQLException ex) {
		        System.out.println("SQL error: " + ex.getMessage());
		        result = "No results found.";
		        return result;
		    }
		}
	  public static int giveReview(String username, int itemId, String rating, String description) {
		    try {
		        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

		        // check if user has already posted 3 reviews for the day
		        LocalDate currentDate = LocalDate.now();
		        PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM ReviewPosting WHERE username = ? AND postingDate = ?");
		        checkStmt.setString(1, username);
		        checkStmt.setDate(2, Date.valueOf(currentDate));
		        ResultSet rs = checkStmt.executeQuery();
		        rs.next();
		        int postCount = rs.getInt(1);
		        if (postCount >= 3) {
		            conn.close();
		            return -1; // user has already reached the daily limit
		        }

		        // check if user is trying to review their own item
		        PreparedStatement selfReviewStmt = conn.prepareStatement("SELECT COUNT(*) FROM Item WHERE id = ? AND username = ?");
		        selfReviewStmt.setInt(1, itemId);
		        selfReviewStmt.setString(2, username);
		        ResultSet selfReviewRs = selfReviewStmt.executeQuery();
		        selfReviewRs.next();
		        int selfReviewCount = selfReviewRs.getInt(1);
		        if (selfReviewCount >= 1) {
		            conn.close();
		            return -2; // user cannot review their own item
		        }
		        
		        // start a transaction
		        conn.setAutoCommit(false);

		        // insert review into Review table
		        PreparedStatement reviewStmt = conn.prepareStatement("INSERT INTO Review (rating, description, username, itemId) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		        reviewStmt.setString(1, rating);
		        reviewStmt.setString(2, description);
		        reviewStmt.setString(3, username);
		        reviewStmt.setInt(4, itemId);
		        int reviewResult = reviewStmt.executeUpdate();

		        if (reviewResult == 1) {
		            // get the generated review ID
		            ResultSet generatedKeys = reviewStmt.getGeneratedKeys();
		            generatedKeys.next();
		            int reviewId = generatedKeys.getInt(1);

		            // update post count for user
		            PreparedStatement updateStmt = conn.prepareStatement("INSERT INTO PostLimit (username, postCount, lastPostDate) \r\n"
		            		+ "VALUES (?, 1, ?) \r\n"
		            		+ "ON DUPLICATE KEY UPDATE postCount = CASE \r\n"
		            		+ "                                        WHEN postCount < 3 THEN postCount + 1 \r\n"
		            		+ "                                        ELSE postCount \r\n"
		            		+ "                                    END, \r\n"
		            		+ "                                    lastPostDate = ?");
		            updateStmt.setString(1, username);
		            updateStmt.setDate(2, Date.valueOf(currentDate));
		            updateStmt.setDate(3, Date.valueOf(currentDate));
		            updateStmt.executeUpdate();

		            // insert review posting record
		            PreparedStatement postingStmt = conn.prepareStatement("INSERT INTO ReviewPosting (username, itemId, reviewId, postingDate) VALUES (?, ?, ?, ?)");
		            postingStmt.setString(1, username);
		            postingStmt.setInt(2, itemId);
		            postingStmt.setInt(3, reviewId);
		            postingStmt.setDate(4, Date.valueOf(currentDate));
		            postingStmt.executeUpdate();

		            // commit the transaction
		            conn.commit();
		            conn.close();
		            return 1; // review added successfully
		        }

		        // rollback the transaction if any step fails
		        conn.rollback();
		        conn.close();
		        return 0; // review failed to add
		    } catch (SQLException ex) {
		        System.out.println("SQL error: " + ex.getMessage());
		    }

		    return 0;
		}
	  
	  
	  
	  // -----------------------------------TESTING------------------------------------------
	  public static void main(String[] args) throws SQLException {
		//create database (also re-creates)
		//initializeDatabase();  
	    
		// register a new user
	    //registerUser("johnsmith", "password123", "John", "Smith", "johnsmith@example.com");
	    
	    // try to register a user with the same username
	    //registerUser("johnsmith", "password456", "John", "Doe", "johndoe@example.com");
	    
	    // test login correct username and password
	    //loginUser("johnsmith", "password123");
	    
	    // test login incorrect username and password
	    //loginUser("johnsmith", "wrongpassword");
	    //insertItem("Smartphone", "new iphone X", "electronic, cellphone, apple", 1000, loggedUser); //AND CODE THAT STOPS AFTER 3 POSTS A DAY
	    //insertItem("Smartphone2", "new iphone X", "electronic, cellphone, apple", 1000, loggedUser);//HAVE TO ADD CODE THAT SAVES LAST LOGGED IN USERNAME 
	    //searchItem("electronic");
	    //giveReview(loggedUser,16, "excellent", "perfect. exactly what i want");
	    //initializeDatabase();
	  }
}

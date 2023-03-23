package comp440project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Phase1GUI extends JFrame implements ActionListener {
    private JTextField usernameFieldLogin, passwordFieldLogin, firstNameField, lastNameField, emailField,usernameFieldRegister,passwordFieldRegister,confirmPasswordFieldRegister;
    private JButton loginButton, registerButton, initializeButton;
    private JLabel loginHeading, usernameLabelLogin, passwordLabelLogin, usernameLabelRegister, passwordLabelRegister, confirmPasswordLabelRegister, firstNameLabel, lastNameLabel, emailLabel, registerHeading, searchHeading;
    private JLabel insertHeading, titleLabel, descriptionLabel, categoryLabel, priceLabel;
    
    private JTextField titleField, descriptionField, categoryField, priceField, searchField;
    private JButton insertButton, searchButton;
    
    private JLabel reviewHeading, reviewLabel, reviewDescription, rating;
    private JTextField reviewSearch, description;
    private JButton reviewButton;
    private JComboBox<String> dropdown;
    
    private JLabel spacer;

    public Phase1GUI() {
        // Set up the GUI components
        setTitle("Phase1 GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(21, 3));
        
        //login
         loginHeading = new JLabel("Login", SwingConstants.CENTER);
        
         usernameLabelLogin = new JLabel("Username: ");
        usernameFieldLogin = new JTextField(20);

         passwordLabelLogin = new JLabel("Password: ");
        passwordFieldLogin = new JTextField(20);
        
        //register
         usernameLabelRegister = new JLabel("Username: ");
        usernameFieldRegister = new JTextField(20);

         passwordLabelRegister = new JLabel("Password: ");
        passwordFieldRegister = new JTextField(20);
        
         confirmPasswordLabelRegister = new JLabel("Confirm Password: ");
        confirmPasswordFieldRegister = new JTextField(20);

         firstNameLabel = new JLabel("First Name: ");
        firstNameField = new JTextField(20);
        
         lastNameLabel = new JLabel("Last Name: ");
        lastNameField = new JTextField(20);
        
         emailLabel = new JLabel("Email: ");
        emailField = new JTextField(20);
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        registerHeading = new JLabel("Register", SwingConstants.CENTER);
        registerButton = new JButton("Register User");
        registerButton.addActionListener(this);

        initializeButton = new JButton("Initialize Database");
        initializeButton.addActionListener(this);

        // Add the components to the frame
        
        //login
        add(loginHeading);
        add(usernameLabelLogin);
        add(usernameFieldLogin);
        add(passwordLabelLogin);
        add(passwordFieldLogin);
        
        //register
        add(registerHeading);
        add(usernameLabelRegister);
        add(usernameFieldRegister);
        add(passwordLabelRegister);
        add(passwordFieldRegister);
        add(confirmPasswordLabelRegister);
        add(confirmPasswordFieldRegister);
        add(firstNameLabel);
        add(firstNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(emailLabel);
        add(emailField);
        add(loginButton);
        add(registerButton);
        add(initializeButton);

        pack();
        setVisible(true);
        
       // ---------------------------------------INSERT ITEM-----------------------------------
         insertHeading = new JLabel("SELLING", SwingConstants.CENTER);

         titleLabel = new JLabel("Title: ");
        titleField = new JTextField(20);

         descriptionLabel = new JLabel("Description: ");
        descriptionField = new JTextField(20);

         categoryLabel = new JLabel("Category: ");
        categoryField = new JTextField(20);

         priceLabel = new JLabel("Price: ");
        priceField = new JTextField(20);

        insertButton = new JButton("Insert Item");
        insertButton.addActionListener(this);
        
        searchButton = new JButton("Search Item");
        searchButton.addActionListener(this);
        
       reviewButton = new JButton("Review Item");
       reviewHeading = new JLabel("Reviewing", SwingConstants.CENTER);
       reviewSearch = new JTextField(20);
       reviewLabel = new JLabel("Item ID: ");
       reviewDescription = new JLabel("Description:");
       description = new JTextField(20);
       rating = new JLabel("Rating:");
       reviewButton.addActionListener(this);
    }
    public String loggedUser;
    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	//login button
        if (e.getSource() == loginButton) {
            String username = usernameFieldLogin.getText();
            String password = passwordFieldLogin.getText();
            int x = Phase1.loginUser(username, password);
            if (x == 1) {
            	JOptionPane.showMessageDialog(this, "Logged in successfully.");
            	loggedUser = username;
            	// Create a new JPanel
                JPanel newPanel = new JPanel(new GridLayout(2, 2));

                // Add UI elements for new menu to new JPanel
                //JLabel welcomeLabel = new JLabel("Welcome " + username + "!", SwingConstants.CENTER);
               // JButton logoutButton = new JButton("Logout");
               // logoutButton.addActionListener(this);

               // newPanel.add(welcomeLabel);
                //newPanel.add(logoutButton);

                // Remove UI elements for login from the current panel
                remove(loginHeading);
                remove(usernameLabelLogin);
                remove(usernameFieldLogin);
                remove(passwordLabelLogin);
                remove(passwordFieldLogin);
                remove(loginButton);
                remove(usernameLabelRegister);
                remove(passwordLabelRegister);
                remove(usernameFieldRegister);
                remove(passwordFieldRegister);
                remove(confirmPasswordLabelRegister);
                remove(confirmPasswordFieldRegister);
                remove(firstNameLabel);
               	remove(firstNameField);
               	remove(lastNameLabel);
               	remove(lastNameField);
               	remove(emailLabel);
               	remove(emailField);
                remove(loginButton);
                remove(registerHeading);
                remove(registerButton);
                remove(initializeButton);
                
                
                // Add the new JPanel to the current panel
                add(newPanel);

                // Update the current panel
                revalidate();
                repaint();
                add(insertHeading);
                add(titleLabel);
                add(titleField);
                add(descriptionLabel);
                add(descriptionField);
                add(categoryLabel);
                add(categoryField);
                add(priceLabel);
                add(priceField);
                add(insertButton);
                
                searchHeading = new JLabel("Searching", SwingConstants.CENTER);
                JLabel searchLabel = new JLabel("Category: ");
                searchField = new JTextField(20);
                
                add(spacer = new JLabel(" "),"span, grow");
                
                add(searchHeading);
                add(searchLabel);
                add(searchField);
                add(searchButton);
                
                add(spacer = new JLabel(" "),"span, grow");
                
                add(reviewHeading);
                add(reviewLabel);
                add(reviewSearch);
                add(rating);
                String[] options = {"Excellent", "Good", "Fair", "Poor"};
                dropdown = new JComboBox<>(options);
                add(dropdown);
                add(reviewDescription);
                add(description);
                add(reviewButton);
                
                setLayout(new GridLayout(25, 3));
                
                pack();
                setVisible(true);
            }
            else
            	JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
        //register button
        else if (e.getSource() == registerButton) {
            String username = usernameFieldRegister.getText();
            String password = passwordFieldRegister.getText();
            String confirmPassword = confirmPasswordFieldRegister.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            
            int x = 0; 
            //confirm password check
            if (!password.equals(confirmPassword)) {
            	JOptionPane.showMessageDialog(this, "Passwords do not match. Failed to register.");
            	return;
            }
            else {
            	//register normally
            	x = Phase1.registerUser(username, password, firstName, lastName, email);
            }
            if ( x == 1)
            	JOptionPane.showMessageDialog(this, "Registered successfully.");
            else
            	JOptionPane.showMessageDialog(this, "Failed to register.");
        } 
        //initialize database button
        else if (e.getSource() == initializeButton) {
            try {
                Phase1.initializeDatabase();
                JOptionPane.showMessageDialog(this, "Database initialized successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error initializing database: " + ex.getMessage());
            }
        }
        //insert item button
        else if (e.getSource() == insertButton) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String priceStr = priceField.getText();
            double price = Double.parseDouble(priceStr);
            int x = Phase1.insertItem(title, description, category, price, loggedUser );
            if (x == 1) {
            	JOptionPane.showMessageDialog(this, "Item inserted successfully.");
            }
            else 
            	JOptionPane.showMessageDialog(this, "You have reached your post cap of 3 for today.");
        }
      //search item button
        else if (e.getSource() == searchButton) {
            String searched = searchField.getText();
            String searchResults = Phase1.searchItem(searched);
            createResultWindow(searchResults);
        }
        //review item button
        else if (e.getSource() == reviewButton) {
            String itemIdString = reviewSearch.getText();
            int itemID = Integer.parseInt(itemIdString);
            String descriptionText = description.getText();
            String ratingOption = (String) dropdown.getSelectedItem();
            int x = Phase1.giveReview(loggedUser, itemID, ratingOption, descriptionText);
            if (x == 1) {
            	JOptionPane.showMessageDialog(this, "Review sent successfully.");
            }
            else if (x == -1 ){
            	JOptionPane.showMessageDialog(this, "You have reached your review cap of 3 for today.");
            }
            else if (x == -2 ){
            	JOptionPane.showMessageDialog(this, "You cannot review your own post.");
            }
            else
            	System.out.println(x);
        }
    }
    //for printing search results
    public void createResultWindow(String results) {
        JFrame resultFrame = new JFrame("Search Results");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea resultTextArea = new JTextArea(results);
        resultTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        resultFrame.getContentPane().add(scrollPane);

        
        
        resultFrame.pack();
        resultFrame.setVisible(true);
    }
    public static void main(String[] args) {
        new Phase1GUI();
    }
}
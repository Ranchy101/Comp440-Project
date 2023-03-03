package comp440project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Phase1GUI extends JFrame implements ActionListener {
    private JTextField usernameFieldLogin, passwordFieldLogin, firstNameField, lastNameField, emailField,usernameFieldRegister,passwordFieldRegister;
    private JButton loginButton, registerButton, initializeButton;

    public Phase1GUI() {
        // Set up the GUI components
        setTitle("Phase1 GUI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(19, 3));

        JLabel loginField = new JLabel("Login", SwingConstants.CENTER);
        
        JLabel usernameLabelLogin = new JLabel("Username: ");
        usernameFieldLogin = new JTextField(20);

        JLabel passwordLabelLogin = new JLabel("Password: ");
        passwordFieldLogin = new JTextField(20);
        
        JLabel usernameLabelRegister = new JLabel("Username: ");
        usernameFieldRegister = new JTextField(20);

        JLabel passwordLabelRegister = new JLabel("Password: ");
        passwordFieldRegister = new JTextField(20);

        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameField = new JTextField(20);
        
        JLabel lastNameLabel = new JLabel("Last Name: ");
        lastNameField = new JTextField(20);
        
        JLabel emailLabel = new JLabel("Email: ");
        emailField = new JTextField(20);
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        JLabel registerField = new JLabel("Register", SwingConstants.CENTER);
        registerButton = new JButton("Register User");
        registerButton.addActionListener(this);

        initializeButton = new JButton("Initialize Database");
        initializeButton.addActionListener(this);

        // Add the components to the frame
        add(loginField);
        add(usernameLabelLogin);
        add(usernameFieldLogin);
        add(passwordLabelLogin);
        add(passwordFieldLogin);
        
        add(registerField);
        add(usernameLabelRegister);
        add(usernameFieldRegister);
        add(passwordLabelRegister);
        add(passwordFieldRegister);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameFieldLogin.getText();
            String password = passwordFieldLogin.getText();
            int x = Phase1.loginUser(username, password);
            if (x==1)
            	JOptionPane.showMessageDialog(this, "Logged in successfully.");
            else
            	JOptionPane.showMessageDialog(this, "Invalid username or password.");
        } else if (e.getSource() == registerButton) {
            String username = usernameFieldRegister.getText();
            String password = passwordFieldRegister.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            int x = Phase1.registerUser(username, password, firstName, lastName, email);
            if (x==1)
            	JOptionPane.showMessageDialog(this, "Registered successfully.");
            else
            	JOptionPane.showMessageDialog(this, "Failed to register.");
        } else if (e.getSource() == initializeButton) {
            try {
                Phase1.initializeDatabase();
                JOptionPane.showMessageDialog(this, "Database initialized successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error initializing database: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Phase1GUI();
    }
}
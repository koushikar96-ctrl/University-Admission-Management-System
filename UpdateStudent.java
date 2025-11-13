import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UpdateStudent extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField rollField, contactField, emailField, addressField;
    private JButton updateBtn, searchBtn, backBtn;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/admissionsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UpdateStudent frame = new UpdateStudent();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UpdateStudent() {
        setTitle("Update Student Details");
        setBounds(100, 100, 600, 450);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel titleLabel = new JLabel("UPDATE STUDENT DETAILS");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));
        titleLabel.setBounds(130, 20, 400, 30);
        contentPane.add(titleLabel);

        JLabel rollLabel = new JLabel("Enter Roll Number:");
        rollLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        rollLabel.setBounds(50, 90, 180, 25);
        contentPane.add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(230, 90, 200, 25);
        contentPane.add(rollField);

        searchBtn = new JButton("SEARCH");
        searchBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        searchBtn.setBounds(450, 90, 100, 25);
        searchBtn.addActionListener(e -> searchStudent());
        contentPane.add(searchBtn);

        JLabel contactLabel = new JLabel("New Contact:");
        contactLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        contactLabel.setBounds(50, 160, 180, 25);
        contentPane.add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(230, 160, 200, 25);
        contentPane.add(contactField);

        JLabel emailLabel = new JLabel("New Email:");
        emailLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        emailLabel.setBounds(50, 210, 180, 25);
        contentPane.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(230, 210, 200, 25);
        contentPane.add(emailField);

        JLabel addressLabel = new JLabel("New Address:");
        addressLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        addressLabel.setBounds(50, 260, 180, 25);
        contentPane.add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(230, 260, 200, 25);
        contentPane.add(addressField);

        updateBtn = new JButton("UPDATE");
        updateBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        updateBtn.setBounds(230, 330, 120, 30);
        updateBtn.addActionListener(e -> updateStudent());
        contentPane.add(updateBtn);

        backBtn = new JButton("BACK");
        backBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        backBtn.setBounds(370, 330, 100, 30);
        backBtn.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        contentPane.add(backBtn);
    }

    private void searchStudent() {
        String rollNo = rollField.getText().trim();
        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a Roll Number.");
            return;
        }

        String query = "SELECT * FROM studentpersonaldetails WHERE rollno = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                contactField.setText(rs.getString("contact"));
                emailField.setText(rs.getString("email"));
                addressField.setText(rs.getString("address"));
            } else {
                JOptionPane.showMessageDialog(null, "No student found with Roll No: " + rollNo);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching student details: " + e.getMessage());
        }
    }

    private void updateStudent() {
        String rollNo = rollField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (rollNo.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!");
            return;
        }

        if (!contact.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "Invalid contact number!");
            return;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            JOptionPane.showMessageDialog(null, "Invalid email format!");
            return;
        }

        String query = "UPDATE studentpersonaldetails SET contact=?, email=?, address=? WHERE rollno=?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, contact);
            stmt.setString(2, email);
            stmt.setString(3, address);
            stmt.setString(4, rollNo);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Student details updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No student found with Roll No: " + rollNo);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating student: " + e.getMessage());
        }
    }
}

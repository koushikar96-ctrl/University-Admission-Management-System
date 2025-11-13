import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeleteStudents extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField rollField;
    private JTextArea resultArea;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/admissionsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DeleteStudents frame = new DeleteStudents();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DeleteStudents() {
        setTitle("Delete Student Record");
        setBounds(100, 100, 600, 400);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("DELETE STUDENT RECORD");
        title.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));
        title.setBounds(150, 20, 350, 30);
        contentPane.add(title);

        JLabel rollLabel = new JLabel("Enter Roll Number:");
        rollLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        rollLabel.setBounds(70, 90, 160, 25);
        contentPane.add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(230, 90, 200, 25);
        contentPane.add(rollField);

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        searchBtn.setBounds(450, 90, 100, 25);
        searchBtn.addActionListener(e -> searchStudent());
        contentPane.add(searchBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(70, 150, 480, 120);
        contentPane.add(scrollPane);

        JButton deleteBtn = new JButton("DELETE");
        deleteBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        deleteBtn.setBounds(180, 300, 120, 30);
        deleteBtn.addActionListener(e -> deleteStudent());
        contentPane.add(deleteBtn);

        JButton backBtn = new JButton("BACK");
        backBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        backBtn.setBounds(340, 300, 120, 30);
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
                resultArea.setText("Name: " + rs.getString("name") + "\n" +
                        "Department: " + rs.getString("department") + "\n" +
                        "Contact: " + rs.getString("contact") + "\n" +
                        "Email: " + rs.getString("email"));
            } else {
                resultArea.setText("No student found with Roll No: " + rollNo);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching student details: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        String rollNo = rollField.getText().trim();
        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a Roll Number.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete Roll No: " + rollNo + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        String deletePersonal = "DELETE FROM studentpersonaldetails WHERE rollno = ?";
        String deleteEducational = "DELETE FROM studenteducationaldetails WHERE rollno = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt1 = conn.prepareStatement(deletePersonal);
             PreparedStatement stmt2 = conn.prepareStatement(deleteEducational)) {

            stmt1.setString(1, rollNo);
            stmt2.setString(1, rollNo);

            int rows1 = stmt1.executeUpdate();
            int rows2 = stmt2.executeUpdate();

            if (rows1 > 0 || rows2 > 0) {
                JOptionPane.showMessageDialog(null, "Student record deleted successfully!");
                resultArea.setText("");
                rollField.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "No record found for Roll No: " + rollNo);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting student: " + e.getMessage());
        }
    }
}

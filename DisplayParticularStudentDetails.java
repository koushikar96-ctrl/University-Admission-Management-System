import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DisplayParticularStudentDetails extends JFrame {

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
                DisplayParticularStudentDetails frame = new DisplayParticularStudentDetails();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DisplayParticularStudentDetails() {
        setTitle("View Student Details");
        setBounds(100, 100, 650, 500);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("VIEW STUDENT DETAILS");
        title.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));
        title.setBounds(190, 25, 350, 25);
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
        searchBtn.addActionListener(e -> displayStudentDetails());
        contentPane.add(searchBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(70, 150, 500, 230);
        contentPane.add(scrollPane);

        JButton backBtn = new JButton("BACK");
        backBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        backBtn.setBounds(260, 410, 120, 30);
        backBtn.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        contentPane.add(backBtn);
    }

    private void displayStudentDetails() {
        String rollNo = rollField.getText().trim();
        if (rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a Roll Number.");
            return;
        }

        String queryPersonal = "SELECT * FROM studentpersonaldetails WHERE rollno = ?";
        String queryEducation = "SELECT * FROM studenteducationaldetails WHERE rollno = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt1 = conn.prepareStatement(queryPersonal);
             PreparedStatement stmt2 = conn.prepareStatement(queryEducation)) {

            stmt1.setString(1, rollNo);
            ResultSet rs1 = stmt1.executeQuery();

            if (!rs1.next()) {
                resultArea.setText("❌ No student found with Roll No: " + rollNo);
                return;
            }

            StringBuilder details = new StringBuilder();
            details.append("=== PERSONAL DETAILS ===\n");
            details.append("Name: ").append(rs1.getString("name")).append("\n");
            details.append("Age: ").append(rs1.getInt("age")).append("\n");
            details.append("Gender: ").append(rs1.getString("gender")).append("\n");
            details.append("Department: ").append(rs1.getString("department")).append("\n");
            details.append("Contact: ").append(rs1.getString("contact")).append("\n");
            details.append("Email: ").append(rs1.getString("email")).append("\n");
            details.append("City: ").append(rs1.getString("city_of_residence")).append("\n");
            details.append("Nationality: ").append(rs1.getString("nationality")).append("\n\n");

            stmt2.setString(1, rollNo);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                details.append("=== EDUCATIONAL DETAILS ===\n");
                details.append("SSLC Institution: ").append(rs2.getString("sslc_institute")).append("\n");
                details.append("HSC Institution: ").append(rs2.getString("hsc_institute")).append("\n");
                details.append("SSLC Marks: ").append(rs2.getInt("sslc_marks")).append("%\n");
                details.append("HSC Marks: ").append(rs2.getInt("hsc_marks")).append("%\n");
                details.append("Cutoff: ").append(rs2.getInt("cutoff")).append("\n");
                details.append("Accommodation: ").append(rs2.getString("accomodation_type")).append("\n");
                details.append("Expected Graduation: ").append(rs2.getInt("graduation_year")).append("\n");
            }

            resultArea.setText(details.toString());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching details: " + e.getMessage());
        }
    }
}

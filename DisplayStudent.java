import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DisplayStudent extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/admissionsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DisplayStudent frame = new DisplayStudent();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public DisplayStudent() {
        setTitle("Display All Students");
        setBounds(100, 100, 1000, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("ALL STUDENT DETAILS", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));
        contentPane.add(title, BorderLayout.NORTH);

        String[] columns = {
                "Name", "Age", "Gender", "Department", "Roll No",
                "Contact", "Email", "City", "Nationality"
        };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
        refreshBtn.addActionListener(e -> loadStudentData());
        buttonPanel.add(refreshBtn);

        JButton backBtn = new JButton("BACK");
        backBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
        backBtn.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        buttonPanel.add(backBtn);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadStudentData();
    }

    private void loadStudentData() {
        String query = "SELECT name, age, gender, department, rollno, contact, email, city_of_residence, nationality FROM studentpersonaldetails";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // clear old data

            while (rs.next()) {
                Object[] row = {
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("department"),
                        rs.getString("rollno"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("city_of_residence"),
                        rs.getString("nationality")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading student data: " + e.getMessage());
        }
    }
}

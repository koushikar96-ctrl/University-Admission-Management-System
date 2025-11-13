import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class HomePage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/admissionsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HomePage frame = new HomePage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HomePage() {
        setBounds(100, 100, 900, 542);
        contentPane = new JPanel();
        setResizable(false);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel header = new JPanel();
        header.setBackground(SystemColor.controlShadow);
        header.setBounds(0, 0, 886, 107);
        contentPane.add(header);
        header.setLayout(null);

        JLabel title = new JLabel("UNIVERSITY ADMISSION MANAGEMENT SYSTEM");
        title.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
        title.setBounds(144, 20, 580, 50);
        header.add(title);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(SystemColor.scrollbar);
        mainPanel.setBounds(0, 117, 886, 320);
        mainPanel.setLayout(null);
        contentPane.add(mainPanel);

        JButton addBtn = new JButton("Add Student");
        addBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        addBtn.setBounds(39, 46, 159, 66);
        addBtn.addActionListener(e -> {
            dispose();
            new frame().setVisible(true);
        });
        mainPanel.add(addBtn);

        JButton updateBtn = new JButton("Update Student");
        updateBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        updateBtn.setBounds(238, 133, 166, 66);
        updateBtn.addActionListener(e -> {
            dispose();
            new UpdateStudent().setVisible(true);
        });
        mainPanel.add(updateBtn);

        JButton removeBtn = new JButton("Remove Student");
        removeBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        removeBtn.setBounds(447, 46, 159, 66);
        removeBtn.addActionListener(e -> {
            dispose();
            new DeleteStudents().setVisible(true);
        });
        mainPanel.add(removeBtn);

        JButton displayBtn = new JButton("Display All Student");
        displayBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        displayBtn.setBounds(657, 128, 187, 66);
        displayBtn.addActionListener(e -> {
            dispose();
            new DisplayStudent().setVisible(true);
        });
        mainPanel.add(displayBtn);

        JButton viewBtn = new JButton("View A Student");
        viewBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        viewBtn.setBounds(447, 228, 159, 66);
        viewBtn.addActionListener(e -> {
            dispose();
            new DisplayParticularStudentDetails().setVisible(true);
        });
        mainPanel.add(viewBtn);

        JButton seatBtn = new JButton("View Seat Availability");
        seatBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        seatBtn.setBounds(35, 228, 199, 66);
        seatBtn.addActionListener(e -> displaySeatAvailability());
        mainPanel.add(seatBtn);
    }

    public void displaySeatAvailability() {
        String query = "SELECT * FROM seat_availability";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            JFrame seatFrame = new JFrame("Seat Availability");
            seatFrame.setSize(400, 300);
            seatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            seatFrame.setLocationRelativeTo(null);

            String[] columns = {"Department", "Total Seats", "Available Seats"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("department"),
                        rs.getInt("total_seats"),
                        rs.getInt("available_seats")
                });
            }

            JTable table = new JTable(model);
            seatFrame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
            seatFrame.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching seat availability: " + e.getMessage());
        }
    }
}

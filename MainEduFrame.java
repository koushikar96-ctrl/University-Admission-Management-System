import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainEduFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField, textField_1, textField_2, textField_3, textField_4, textField_5,
            textField_6, textField_7, textField_8;
    private JComboBox<String> branchCombo, accomodationCombo;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/admissionsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainEduFrame frame = new MainEduFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainEduFrame() {
        setBounds(0, 0, 1200, 1009);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel title = new JLabel("STUDENT EDUCATIONAL DETAILS");
        title.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        title.setBounds(430, 42, 400, 30);
        contentPane.add(title);

        addLabel("Institution Studied (SSLC)", 100, 139);
        addLabel("Institution Studied (HSC)", 100, 230);
        addLabel("Year of Passing (SSLC)", 100, 331);
        addLabel("Year of Passing (HSC)", 100, 401);
        addLabel("Marks in Percentage (SSLC)", 639, 323);
        addLabel("Marks in Percentage (HSC)", 639, 406);
        addLabel("Cut Off (HSC)", 100, 510);
        addLabel("Branch Preferred", 100, 595);
        addLabel("Accommodation Type", 644, 505);
        addLabel("Expected Graduation (In Year)", 639, 588);
        addLabel("Roll Number", 100, 662);

        textField = addTextField(321, 147);
        textField_1 = addTextField(321, 239);
        textField_2 = addTextField(321, 330);
        textField_3 = addTextField(321, 410);
        textField_4 = addTextField(879, 330);
        textField_5 = addTextField(879, 415);
        textField_6 = addTextField(321, 509);
        textField_7 = addTextField(879, 594);
        textField_8 = addTextField(321, 662);

        branchCombo = new JComboBox<>(new String[] {
                "BE CIVIL", "BE MECH", "BE CSE", "BTECH IT", "BTECH AIDS", "BE BME", "BE ECE", "BE EEE"
        });
        branchCombo.setFont(new Font("Times New Roman", Font.ITALIC, 15));
        branchCombo.setBounds(321, 587, 260, 21);
        contentPane.add(branchCombo);

        accomodationCombo = new JComboBox<>(new String[] { "Hosteller", "Dayscholar" });
        accomodationCombo.setFont(new Font("Times New Roman", Font.ITALIC, 15));
        accomodationCombo.setBounds(879, 500, 182, 32);
        contentPane.add(accomodationCombo);

        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        submitBtn.setBounds(1016, 697, 118, 32);
        submitBtn.addActionListener(e -> handleSubmit());
        contentPane.add(submitBtn);

        JButton backBtn = new JButton("BACK");
        backBtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        backBtn.setBounds(857, 697, 111, 32);
        backBtn.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        contentPane.add(backBtn);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
        lbl.setBounds(x, y, 250, 25);
        contentPane.add(lbl);
    }

    private JTextField addTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 260, 25);
        contentPane.add(tf);
        return tf;
    }

    private void handleSubmit() {
        String sslcInstitute = textField.getText();
        String hscInstitute = textField_1.getText();
        String sslcYear = textField_2.getText();
        String hscYear = textField_3.getText();
        String sslcMarks = textField_4.getText();
        String hscMarks = textField_5.getText();
        String cutoff = textField_6.getText();
        String graduationYear = textField_7.getText();
        String rollNo = textField_8.getText();
        String accomodation = (String) accomodationCombo.getSelectedItem();

        if (sslcInstitute.isEmpty() || hscInstitute.isEmpty() || sslcYear.isEmpty() || hscYear.isEmpty()
                || sslcMarks.isEmpty() || hscMarks.isEmpty() || cutoff.isEmpty() || graduationYear.isEmpty()
                || rollNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields!");
            return;
        }

        // Validation checks
        if (!sslcInstitute.matches("[a-zA-Z0-9&\\-\\., ]+")
                || !hscInstitute.matches("[a-zA-Z0-9&\\-\\., ]+")) {
            JOptionPane.showMessageDialog(null, "Invalid Institution Name!");
            return;
        }
        if (!sslcYear.matches("\\d{4}") || !hscYear.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Year of Passing must be 4 digits.");
            return;
        }
        if (!sslcMarks.matches("\\d{2}") || !hscMarks.matches("\\d{2}")) {
            JOptionPane.showMessageDialog(null, "Marks must be 2 digits.");
            return;
        }
        if (!cutoff.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(null, "Cutoff must be a 3-digit number.");
            return;
        }
        if (!graduationYear.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Expected graduation year must be 4 digits.");
            return;
        }
        if (!isValidRollNumber(rollNo)) {
            JOptionPane.showMessageDialog(null, "Invalid Roll Number format. (Ex: 24CS01)");
            return;
        }

        // Database insert
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = String.format(
                    "INSERT INTO studenteducationaldetails (sslc_institute, hsc_institute, sslc_year, hsc_year, sslc_marks, hsc_marks, cutoff, accomodation_type, graduation_year, rollno) "
                            + "VALUES ('%s', '%s', %s, %s, %s, %s, %s, '%s', %s, '%s')",
                    sslcInstitute, hscInstitute, sslcYear, hscYear, sslcMarks, hscMarks, cutoff, accomodation,
                    graduationYear, rollNo);

            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "Educational details added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        }
    }

    private boolean isValidRollNumber(String rollNo) {
        Pattern pattern = Pattern.compile("^[0-9]{2}[A-Za-z]{2}[0-9]{2}$");
        Matcher matcher = pattern.matcher(rollNo);
        return matcher.matches();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}

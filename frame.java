import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

public class frame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Map<String, Integer> departmentCounters = new HashMap<>();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/admissionsystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public JPanel contentPane;
    public JTextField textField, textField_1, textField_2, textField_3, textField_4, textField_5, textField_6, textField_7,
            textField_8, textField_9, textField_10, textField_11, textField_12, textField_13, textField_14,
            textField_15, textField_16;
    public JComboBox<String> comboBox_1, comboBox_2;
    private JEditorPane editorPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                frame frame = new frame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public frame() {
        setBounds(0, 0, 1202, 990);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.scrollbar);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("STUDENT PERSONAL DETAILS");
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblTitle.setBounds(400, 10, 400, 40);
        contentPane.add(lblTitle);

        addLabel("Name", 56, 97);
        addLabel("Age", 56, 155);
        addLabel("D.O.B", 56, 220);
        addLabel("Father Name", 56, 285);
        addLabel("Mother Name", 56, 359);
        addLabel("Father Occupation", 45, 416);
        addLabel("Mother Occupation", 45, 496);
        addLabel("Blood Group", 56, 575);
        addLabel("City of Residence", 56, 643);
        addLabel("Roll Number", 56, 719);
        addLabel("Department", 535, 708);

        // TextFields (Left side)
        textField = addTextField(187, 94);
        textField_1 = addTextField(187, 155);
        textField_2 =_

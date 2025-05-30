package program;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

// Main GUI application for student management system
public class Main extends JFrame {

    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JComboBox<String> courseDropdownEnroll;
    private JComboBox<String> studentDropdownEnroll;
    private JComboBox<String> studentDropdownGrade;
    private JComboBox<String> courseDropdownGrade;
    private JTextArea displayArea;

    // Constructor to initialize the GUI
    public Main() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        StudentManagementSystem.addStudent("0001", "Adam Benzema");
        StudentManagementSystem.addStudent("0002", "Amira Bennani");
        StudentManagementSystem.addCourse("CS 1102", "Programming 1", 40);
        StudentManagementSystem.addCourse("CS 1103", "Programming 2", 40);

        initComponents();
        refreshStudentTable();
        refreshCourseDropdowns();
        refreshStudentDropdowns();

        setVisible(true);
    }

    // Initialize all GUI components
    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        JMenu studentMenu = new JMenu("Student");
        JMenuItem addStudentItem = new JMenuItem("Add Student");
        JMenuItem updateStudentItem = new JMenuItem("Update Student");
        JMenuItem viewStudentsItem = new JMenuItem("View All Students");

        addStudentItem.addActionListener(_ -> showAddStudentDialog());
        updateStudentItem.addActionListener(_ -> showUpdateStudentDialog());
        viewStudentsItem.addActionListener(_ -> refreshStudentTable());

        studentMenu.add(addStudentItem);
        studentMenu.add(updateStudentItem);
        studentMenu.add(viewStudentsItem);
        menuBar.add(studentMenu);

        JMenu courseMenu = new JMenu("Course");
        JMenuItem addCourseItem = new JMenuItem("Add Course");
        addCourseItem.addActionListener(_ -> showAddCourseDialog());
        courseMenu.add(addCourseItem);
        menuBar.add(courseMenu);

        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        studentTableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Enrolled Courses" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(studentTableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Manage Enrollment & Grades"));

        JPanel enrollPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrollPanel.setBorder(BorderFactory.createTitledBorder("Enroll Student"));
        courseDropdownEnroll = new JComboBox<>();
        studentDropdownEnroll = new JComboBox<>();
        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(_ -> enrollStudentAction());
        enrollPanel.add(new JLabel("Student:"));
        enrollPanel.add(studentDropdownEnroll);
        enrollPanel.add(new JLabel("Course:"));
        enrollPanel.add(courseDropdownEnroll);
        enrollPanel.add(enrollButton);
        controlPanel.add(enrollPanel);

        JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradePanel.setBorder(BorderFactory.createTitledBorder("Assign Grade"));
        studentDropdownGrade = new JComboBox<>();
        courseDropdownGrade = new JComboBox<>();
        JTextField gradeField = new JTextField(5);
        JButton assignGradeButton = new JButton("Assign Grade");

        studentDropdownGrade.addActionListener(_ -> updateCourseDropdownForGrading());

        assignGradeButton.addActionListener(_ -> assignGradeAction(gradeField.getText()));
        gradePanel.add(new JLabel("Student:"));
        gradePanel.add(studentDropdownGrade);
        gradePanel.add(new JLabel("Course:"));
        gradePanel.add(courseDropdownGrade);
        gradePanel.add(new JLabel("Grade:"));
        gradePanel.add(gradeField);
        gradePanel.add(assignGradeButton);
        controlPanel.add(gradePanel);

        displayArea = new JTextArea(5, 30);
        displayArea.setEditable(false);
        JScrollPane displayScrollPane = new JScrollPane(displayArea);
        displayScrollPane.setBorder(BorderFactory.createTitledBorder("System Log / Details"));
        controlPanel.add(displayScrollPane);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    // Show dialog to add a new student
    private void showAddStudentDialog() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name can't be blank.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (StudentManagementSystem.addStudent(id, name)) {
                logMessage("Student " + name + " added.");
                refreshStudentTable();
                refreshStudentDropdowns();
            } else {
                JOptionPane.showMessageDialog(this, "Student not added – ID " + id + " is taken or input is incorrect.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Show dialog to add a new course
    private void showAddCourseDialog() {
        JTextField codeField = new JTextField(10);
        JTextField nameField = new JTextField(20);
        JTextField capacityField = new JTextField(5);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Course Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Course Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Max Capacity:"));
        panel.add(capacityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Course",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String capacityStr = capacityField.getText().trim();
            if (code.isEmpty() || name.isEmpty() || capacityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) {
                    JOptionPane.showMessageDialog(this, "Capacity must be a positive number.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (StudentManagementSystem.addCourse(code, name, capacity)) {
                    logMessage("Course " + name + " added.");
                    refreshCourseDropdowns();
                } else {
                    JOptionPane.showMessageDialog(this, "Course with code " + code + " already exists.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid capacity format. Please enter a number.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Show dialog to update student information
    private void showUpdateStudentDialog() {
        String studentIdToUpdate = JOptionPane.showInputDialog(this, "Enter ID of student to update:");
        if (studentIdToUpdate == null || studentIdToUpdate.trim().isEmpty())
            return;

        Optional<Student> studentOpt = StudentManagementSystem.findStudentById(studentIdToUpdate.trim());
        if (studentOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student with ID " + studentIdToUpdate + " not found.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Student student = studentOpt.get();
        JTextField nameField = new JTextField(student.getName(), 20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Updating Student ID: " + student.getId()));
        panel.add(new JLabel("New Name:"));
        panel.add(nameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Student Information",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            StudentManagementSystem.updateStudent(student.getId(), newName);
            logMessage("Student " + student.getId() + " updated.");
            refreshStudentTable();
            refreshStudentDropdowns();
        }
    }

    // Handle student enrollment in course
    private void enrollStudentAction() {
        String selectedStudentStr = (String) studentDropdownEnroll.getSelectedItem();
        String selectedCourseStr = (String) courseDropdownEnroll.getSelectedItem();

        if (selectedStudentStr == null || selectedCourseStr == null ||
                selectedStudentStr.equals("Select Student") || selectedCourseStr.equals("Select Course")) {
            JOptionPane.showMessageDialog(this, "Please select a student and a course.", "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String studentId = selectedStudentStr.split(" - ")[0];
        String courseCode = selectedCourseStr.split(" - ")[0];

        if (StudentManagementSystem.enrollStudentInCourse(studentId, courseCode)) {
            logMessage("Student " + studentId + " enrolled in " + courseCode + ".");
            refreshStudentTable();
            refreshCourseDropdowns();
            updateCourseDropdownForGrading();
        } else {
            Optional<Course> cOpt = StudentManagementSystem.findCourseByCode(courseCode);
            String reason = "Enrollment failed. Student may already be enrolled or course might be full.";
            if (cOpt.isPresent() && cOpt.get().isFull()) {
                reason = "Enrollment failed. Course " + courseCode + " is full.";
            }
            JOptionPane.showMessageDialog(this, reason, "Enrollment Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle grade assignment to student
    private void assignGradeAction(String gradeStr) {
        String selectedStudentStr = (String) studentDropdownGrade.getSelectedItem();
        String selectedCourseStr = (String) courseDropdownGrade.getSelectedItem();

        if (selectedStudentStr == null || selectedCourseStr == null || gradeStr.trim().isEmpty() ||
                selectedStudentStr.equals("Select Student") || selectedCourseStr.equals("Select Course")) {
            JOptionPane.showMessageDialog(this, "Please select a student, a course, and enter a grade.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String studentId = selectedStudentStr.split(" - ")[0];
        String courseCode = selectedCourseStr.split(" - ")[0];

        try {
            double grade = Double.parseDouble(gradeStr.trim());
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (StudentManagementSystem.assignGrade(studentId, courseCode, grade)) {
                logMessage("Grade " + grade + " assigned to " + studentId + " for " + courseCode + ".");
                refreshStudentTable();
                updateCourseDropdownForGrading();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to assign grade. Student might not be enrolled in this course.", "Grading Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid grade format. Please enter a number.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update course dropdown based on selected student
    private void updateCourseDropdownForGrading() {
        courseDropdownGrade.removeAllItems();
        courseDropdownGrade.addItem("Select Course");
        String selectedStudentStr = (String) studentDropdownGrade.getSelectedItem();
        if (selectedStudentStr != null && !selectedStudentStr.equals("Select Student")) {
            String studentId = selectedStudentStr.split(" - ")[0];
            Optional<Student> studentOpt = StudentManagementSystem.findStudentById(studentId);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                for (Course course : student.getEnrolledCourses()) {
                    Double grade = student.getGradeForCourse(course.getCourseCode());
                    String gradeDisplay = (grade != null) ? String.format("%.2f", grade) : "N/A";
                    courseDropdownGrade.addItem(
                            course.getCourseCode() + " - " + course.getCourseName() + " (Grade: " + gradeDisplay + ")");
                }
            }
        }
    }

    // Refresh the student table with current data
    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        List<Student> students = StudentManagementSystem.getAllStudents();
        for (Student s : students) {
            studentTableModel.addRow(
                    new Object[] { s.getId(), s.getName(), s.getCoursesAndGradesString().replace("\n", ", ") });
        }
    }

    // Refresh course dropdown with current courses
    private void refreshCourseDropdowns() {
        courseDropdownEnroll.removeAllItems();
        courseDropdownEnroll.addItem("Select Course");
        List<Course> courses = StudentManagementSystem.getAllCourses();
        for (Course c : courses) {
            courseDropdownEnroll.addItem(c.toString());
        }
    }

    // Refresh student dropdowns with current students
    private void refreshStudentDropdowns() {
        studentDropdownEnroll.removeAllItems();
        studentDropdownGrade.removeAllItems();
        studentDropdownEnroll.addItem("Select Student");
        studentDropdownGrade.addItem("Select Student");

        List<Student> students = StudentManagementSystem.getAllStudents();
        for (Student s : students) {
            studentDropdownEnroll.addItem(s.toString());
            studentDropdownGrade.addItem(s.toString());
        }
    }

    // Log message to display area
    private void logMessage(String message) {
        displayArea.append(message + "\n");
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
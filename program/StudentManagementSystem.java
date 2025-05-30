package program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// University system managing students and courses
public class StudentManagementSystem {
    private static List<Student> students = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();

    // Private constructor to prevent instantiation
    private StudentManagementSystem() {
    }

    // Add a new student to the system
    public static boolean addStudent(String id, String name) {
        if (findStudentById(id).isPresent()) {
            return false;
        }
        students.add(new Student(id, name));
        return true;
    }

    // Find student by ID
    public static Optional<Student> findStudentById(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
    }

    // Update student name
    public static boolean updateStudent(String id, String newName) {
        Optional<Student> studentOpt = findStudentById(id);
        if (studentOpt.isPresent()) {
            studentOpt.get().setName(newName);
            return true;
        }
        return false;
    }

    // Get all students as unmodifiable list
    public static List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    // Add a new course to the system
    public static boolean addCourse(String code, String name, int capacity) {
        if (findCourseByCode(code).isPresent()) {
            return false;
        }
        courses.add(new Course(code, name, capacity));
        return true;
    }

    // Find course by code
    public static Optional<Course> findCourseByCode(String code) {
        return courses.stream().filter(c -> c.getCourseCode().equalsIgnoreCase(code)).findFirst();
    }

    // Get all courses as unmodifiable list
    public static List<Course> getAllCourses() {
        return Collections.unmodifiableList(courses);
    }

    // Enroll student in a course
    public static boolean enrollStudentInCourse(String studentId, String courseCode) {
        Optional<Student> studentOpt = findStudentById(studentId);
        Optional<Course> courseOpt = findCourseByCode(courseCode);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();
            if (!course.isFull()) {
                if (student.enrollInCourse(course)) {
                    course.enrollStudent();
                    return true;
                }
            }
        }
        return false;
    }

    // Assign grade to student for a course
    public static boolean assignGrade(String studentId, String courseCode, double grade) {
        Optional<Student> studentOpt = findStudentById(studentId);
        if (studentOpt.isPresent()) {
            return studentOpt.get().assignGrade(courseCode, grade);
        }
        return false;
    }
}
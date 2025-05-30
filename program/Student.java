package program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// Student class representing a student with enrollment and grade tracking
public class Student {
    private String id;
    private String name;
    private List<Course> enrolledCourses;
    private Map<String, Double> grades;

    // Constructor to create a new student
    public Student(String studentId, String name) {
        this.id = studentId;
        this.name = name;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Course> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses);
    }

    public Map<String, Double> getGrades() {
        return new HashMap<>(grades);
    }

    public void setName(String name) {
        this.name = name;
    }

    // Enroll student in a course
    public boolean enrollInCourse(Course course) {
        if (course != null && !enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            return true;
        }
        return false;
    }

    // Assign a grade to student for a specific course
    public boolean assignGrade(String courseCode, double grade) {
        if (grade < 0 || grade > 100)
            return false;
        boolean isEnrolled = enrolledCourses.stream().anyMatch(c -> c.getCourseCode().equals(courseCode));
        if (isEnrolled) {
            grades.put(courseCode, grade);
            return true;
        }
        return false;
    }

    // Get grade for a specific course
    public Double getGradeForCourse(String courseCode) {
        return grades.get(courseCode);
    }

    // Get formatted string of courses and grades
    public String getCoursesAndGradesString() {
        if (enrolledCourses.isEmpty()) {
            return "Not enrolled in any courses.";
        }
        return enrolledCourses.stream()
                .map(course -> course.getCourseName() + " (" + course.getCourseCode() + "): " +
                        (grades.containsKey(course.getCourseCode()) ? grades.get(course.getCourseCode()) : "N/A"))
                .collect(Collectors.joining("\n  "));
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
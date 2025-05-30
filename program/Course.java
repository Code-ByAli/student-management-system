package program;

import java.util.Objects;

// Course class representing a university course with enrollment management
public class Course {
    private String courseCode;
    private String courseName;
    private int maximumCapacity;
    private int totalEnrolledStudents;

    // Constructor to initialize a new course
    public Course(String courseCode, String courseName, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maximumCapacity = maxCapacity;
        this.totalEnrolledStudents = 0;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setMaximumCapacity(int maxCapacity) {
        this.maximumCapacity = maxCapacity;
    }

    // Check if course has reached maximum capacity
    public boolean isFull() {
        return totalEnrolledStudents >= maximumCapacity;
    }

    // Enroll a student in this course
    public boolean enrollStudent() {
        if (!isFull()) {
            totalEnrolledStudents++;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName + " (" + totalEnrolledStudents + "/" + maximumCapacity + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
}
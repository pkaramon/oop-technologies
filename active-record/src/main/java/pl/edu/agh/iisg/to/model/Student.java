package pl.edu.agh.iisg.to.model;

import pl.edu.agh.iisg.to.executor.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class Student {
    private final int id;

    private final String firstName;

    private final String lastName;

    private final int indexNumber;

    Student(final int id, final String firstName, final String lastName, final int indexNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public static Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        // TODO
        String sql = "INSERT INTO student (first_name, last_name, index_number) VALUES(?, ?, ?)";


        // TODO
        // it is important to maintain the correct order of the variables
        Object[] args = {firstName, lastName, indexNumber};

        try {
            int id = QueryExecutor.createAndObtainId(sql, args);
            return Student.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<Student> findByIndexNumber(final int indexNumber) {
        // TODO
        return find(indexNumber, "SELECT * FROM student where index_number = ?");
    }

    public static Optional<Student> findById(final int id) {
        String sql = "SELECT * FROM student WHERE id = (?)";
        return find(id, sql);
    }

    private static Optional<Student> find(int value, String sql) {
        Object[] args = {value};
        try (ResultSet rs = QueryExecutor.read(sql, args)) {
            if (rs.next()) {
                return Optional.of(new Student(
                        rs.getInt(Columns.ID),
                        rs.getString(Columns.FIRST_NAME),
                        rs.getString(Columns.LAST_NAME),
                        rs.getInt(Columns.INDEX_NUMBER)
                ));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Map<Course, Float> createReport() {
        String sql = "SELECT course.id, course.name, AVG(grade) as avg_grade FROM grade " +
                "JOIN course ON course.id = grade.course_id " +
                "WHERE grade.student_id = ? " +
                "GROUP BY course.id, course.name";

        Object[] args = {id()};

        try (ResultSet rs = QueryExecutor.read(sql, args)) {
            Map<Course, Float> report = new HashMap<>();
            while (rs.next()) {
                Course key = new Course(
                        rs.getInt(Course.Columns.ID),
                        rs.getString(Course.Columns.NAME)
                );
                Float value = rs.getFloat("avg_grade");
                report.put(key, value);
            }
            return report;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public int id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public int indexNumber() {
        return indexNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Student student = (Student) o;

        if (id != student.id)
            return false;
        if (indexNumber != student.indexNumber)
            return false;
        if (!firstName.equals(student.firstName))
            return false;
        return lastName.equals(student.lastName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + indexNumber;
        return result;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String FIRST_NAME = "first_name";

        public static final String LAST_NAME = "last_name";

        public static final String INDEX_NUMBER = "index_number";
    }
}

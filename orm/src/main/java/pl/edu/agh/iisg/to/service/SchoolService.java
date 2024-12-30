package pl.edu.agh.iisg.to.service;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchoolService {

    private final TransactionService transactionService;

    private final StudentRepository studentRepository;

    private final CourseDao courseDao;

    private final GradeDao gradeDao;

    public SchoolService(TransactionService transactionService,
                         StudentRepository studentRepository,
                         CourseDao courseDao,
                         GradeDao gradeDao) {
        this.transactionService = transactionService;
        this.studentRepository = studentRepository;
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
    }

    public boolean enrollStudent(final Course course, final Student student) {
        return transactionService.doAsTransaction(() -> {
            if (course.studentSet().contains(student) || student.courseSet().contains(course)) {
                return false;
            }
            course.studentSet().add(student);
            student.courseSet().add(course);
            return true;
        }).orElse(false);
    }

    public boolean removeStudent(int indexNumber) {
        return transactionService.doAsTransaction(() ->
                        studentRepository.getByIndexNumber(indexNumber).map(
                                student -> {
                                    studentRepository.remove(student);
                                    return true;
                                }
                        ).orElse(false))
                .orElse(false);
    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        return transactionService.doAsTransaction(() -> {
            Grade grade = new Grade(student, course, gradeValue);
            student.gradeSet().add(grade);
            course.gradeSet().add(grade);
            gradeDao.save(grade);
            return true;
        }).orElse(false);
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        List<Student> students = studentRepository.findAllByCourseName(courseName);
        return students.stream().collect(
                Collectors.toMap(Student::fullName,
                        student -> student.gradeSet().stream()
                                .filter(grade -> grade.course().name().equals(courseName))
                                .map(Grade::grade)
                                .collect(Collectors.toList())
                ));
    }
}

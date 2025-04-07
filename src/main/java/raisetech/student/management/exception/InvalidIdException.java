package raisetech.student.management.exception;

import lombok.Getter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Getter
public class InvalidIdException extends Exception {

  private String field;
  private String message;

  public InvalidIdException(int studentId) {
    this.field = "student.studentId：" + studentId;
    this.message = "受講生IDが不正か、存在しません。";
  }

  public InvalidIdException(Student student) {
    this.field = "student.studentId：" + student.getStudentId();
    this.message = "受講生IDが不正か、存在しません。";
  }

  public InvalidIdException(StudentCourse course) {
    this.field = "studentCourse.courseId：" + course.getCourseId();
    this.message = "コースIDが不正です。受講生IDに紐づけられたコースIDを入力してください。";
  }

}


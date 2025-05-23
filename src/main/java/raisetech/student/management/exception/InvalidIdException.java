package raisetech.student.management.exception;

import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.value.Id;

/**
 * 入力された受講生IDあるいは受講生コースIDがテーブルに存在しないか、受講生コースIDが受講生IDに紐づかないときに投げられる例外クラスです。
 */
public class InvalidIdException extends StudentException {

  public InvalidIdException(Id id) {
    this.field = "argument" + id;
    this.message = "受講生IDが存在しません。";
  }

  public InvalidIdException(StudentCourse course) {
    this.field = "studentCourse.courseId：" + course.getCourseId();
    this.message = "コースIDが不正です。受講生IDに紐づけられたコースIDを入力してください。";
  }

}


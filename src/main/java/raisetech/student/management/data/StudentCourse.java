package raisetech.student.management.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import raisetech.student.management.data.value.Id;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class StudentCourse {

  private final Id courseId;

  private final String courseName;

  private final Id studentId;

  private final LocalDate courseStartAt;

  private final LocalDate courseEndAt;


}

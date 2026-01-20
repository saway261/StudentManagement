package raisetech.student.management.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.validation.CreateGroup;
import raisetech.student.management.validation.UpdateGroup;

@Getter
@Setter
public class StudentCourse {

  @Positive(groups = UpdateGroup.class)
  private int courseId;

  private int studentId;

  @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
  @Size(max = 10, groups = {CreateGroup.class, UpdateGroup.class})
  private String courseName;

  private LocalDate courseStartAt;
  private LocalDate courseEndAt;

}

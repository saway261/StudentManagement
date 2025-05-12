package raisetech.student.management.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.student.management.data.domain.validation.CourseName;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Data
@NoArgsConstructor
public class StudentCourseForm {

  @NotNull(groups = OnUpdate.class)
  @Positive(groups = OnUpdate.class)
  private Integer courseId;

  @NotNull(groups = {OnRegister.class, OnUpdate.class})
  @CourseName(groups = {OnRegister.class, OnUpdate.class})
  private String courseName;

  private Integer studentId;

  private LocalDate courseStartAt;

  @NotNull(groups = OnUpdate.class)
  private LocalDate courseEndAt;

}


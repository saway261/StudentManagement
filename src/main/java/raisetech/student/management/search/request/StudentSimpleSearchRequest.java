package raisetech.student.management.search.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentSimpleSearchRequest {
  private String fullNameContains;
  private String kanaNameContains;
  private String areaContains;
  private Integer ageMin;
  private Integer ageMax;
  private String sexEq;
  private String courseCode;
  private List<Integer> statusId;
  private LocalDate applyFrom;
  private LocalDate applyTo;
  private LocalDate startFrom;
  private LocalDate startTo;
  private Boolean isDeleted;
}

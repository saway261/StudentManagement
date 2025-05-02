package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Positive;
import java.util.Objects;
import lombok.Getter;
import raisetech.student.management.data.domain.validation.OnRegister;
import raisetech.student.management.data.domain.validation.OnUpdate;

@Getter
public class Id {

  @Positive(groups = {OnRegister.class, OnUpdate.class})
  private final Integer value;

  @JsonCreator
  public Id(Integer value) {
    if (value != null && value < 1) {
      throw new IllegalArgumentException("IDはnullまたは1以上でなければなりません");
    }
    this.value = value;
  }

  public boolean isNull() {
    return value == null;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Id)) {
      return false;
    }
    Id other = (Id) o;
    return Objects.equals(value, other.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}


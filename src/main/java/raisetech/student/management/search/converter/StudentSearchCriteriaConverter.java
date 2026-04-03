package raisetech.student.management.search.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.student.management.search.criteria.StudentSearchCriteria;
import raisetech.student.management.search.request.SearchFilter;

@Component
public class StudentSearchCriteriaConverter {

  public StudentSearchCriteria toCriteria(List<SearchFilter> filters) {
    StudentSearchCriteria criteriaBox = new StudentSearchCriteria();

    for (SearchFilter filter : filters) {
      String field = filter.getField();

      switch (field) {
        case "fullName" -> criteriaBox.applyFullNameFilter(filter);
        case "courseCode" -> criteriaBox.applyCourseCodeFilter(filter);
        case "statusId" -> criteriaBox.applyStatusIdFilter(filter);
        case "age" -> criteriaBox.applyAgeFilter(filter);
        case "courseApplyAt" -> criteriaBox.applyCourseApplyAtFilter(filter);
        case "isDeleted" -> criteriaBox.applyIsDeletedFilter(filter);
        default -> throw new IllegalArgumentException("未対応の検索項目です: " + field);
      }
    }
    
    return criteriaBox;
  }

}
package raisetech.student.management.search.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.student.management.search.criteria.StudentSearchCriteria;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchableField;

@Component
public class StudentSearchCriteriaConverter {

  public StudentSearchCriteria toCriteria(List<SearchFilter> filters) {
    StudentSearchCriteria criteria = new StudentSearchCriteria();

    for (SearchFilter filter : filters) {
      SearchableField field = SearchableField.getFromFieldName(filter.getField());

      switch (field) {
        case FULL_NAME -> criteria.applyFullNameFilter(filter);
        case COURSE_CODE -> criteria.applyCourseCodeFilter(filter);
        case STATUS_ID -> criteria.applyStatusIdFilter(filter);
        case AGE -> criteria.applyAgeFilter(filter);
        case COURSE_APPLY_AT -> criteria.applyCourseApplyAtFilter(filter);
        case IS_DELETED -> criteria.applyIsDeletedFilter(filter);
      }
    }
    
    return criteria;
  }

}
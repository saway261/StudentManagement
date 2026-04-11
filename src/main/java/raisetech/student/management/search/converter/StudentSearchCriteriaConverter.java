package raisetech.student.management.search.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.student.management.search.criteria.StudentSearchCriteria;
import raisetech.student.management.search.request.SearchFilter;
import raisetech.student.management.search.request.SearchableField;
import raisetech.student.management.search.request.StudentAdvancedSearchRequest;
import raisetech.student.management.search.request.StudentSimpleSearchRequest;

@Component
public class StudentSearchCriteriaConverter {

  public StudentSearchCriteria toCriteria(StudentAdvancedSearchRequest request) {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    List<SearchFilter> filters = request.getFilters();

    for (SearchFilter filter : filters) {
      SearchableField field = SearchableField.fromFieldName(filter.getField());

      switch (field) {
        case FULL_NAME -> criteria.applyFullNameFilter(filter);
        case KANA_NAME -> criteria.applyKanaNameFilter(filter);
        case NICKNAME -> criteria.applyNicknameFilter(filter);
        case EMAIL -> criteria.applyEmailFilter(filter);
        case AREA -> criteria.applyAreaFilter(filter);
        case TELEPHONE -> criteria.applyTelephoneFilter(filter);
        case AGE -> criteria.applyAgeFilter(filter);
        case SEX -> criteria.applySexFilter(filter);
        case REMARK -> criteria.applyRemarkFilter(filter);
        case IS_DELETED -> criteria.applyIsDeletedFilter(filter);
        case COURSE_CODE -> criteria.applyCourseCodeFilter(filter);
        case STATUS_ID -> criteria.applyStatusIdFilter(filter);
        case COURSE_APPLY_AT -> criteria.applyCourseApplyAtFilter(filter);
        case COURSE_START_AT -> criteria.applyCourseStartAtFilter(filter);
        case COURSE_PLANNED_END_AT -> criteria.applyCoursePlannedEndAtFilter(filter);
        case COURSE_FINISHED_AT -> criteria.applyCourseFinishedAtFilter(filter);
      }
    }
    
    return criteria;
  }

  public StudentSearchCriteria toCriteria(StudentSimpleSearchRequest request) {
    return new StudentSearchCriteria(request);
  }

}
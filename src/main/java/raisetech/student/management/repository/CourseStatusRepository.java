package raisetech.student.management.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseStatusRepository {

  /**
   * あるステータスから別のステータスへ遷移可能かを判定します。
   * 元ステータスが終端でなく、かつ遷移マスタに定義がある場合にtrueを返します。
   *
   * @param fromStatusId 現在のステータスID
   * @param toStatusId 更新先のステータスID
   * @return 遷移可能ならtrue
   */
  @Select("""
      SELECT COUNT(*) > 0
      FROM status_master sm
      JOIN status_transition_master stm
        ON sm.status_id = stm.from_status_id
      WHERE sm.status_id = #{fromStatusId}
        AND stm.to_status_id = #{toStatusId}
        AND sm.is_terminal = false
      """)
  boolean canTransition(int fromStatusId, int toStatusId);

}

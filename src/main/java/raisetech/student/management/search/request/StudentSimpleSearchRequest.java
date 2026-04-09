package raisetech.student.management.search.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生簡易検索条件")
@Getter
@Setter
@NoArgsConstructor
public class StudentSimpleSearchRequest {

  @Schema(description = "受講生氏名の部分一致条件", example = "田中")
  private String fullNameContains;

  @Schema(description = "受講生かな氏名の部分一致条件", example = "たなか")
  private String kanaNameContains;

  @Schema(description = "居住地の部分一致条件", example = "東京")
  private String areaContains;

  @Schema(description = "年齢の下限", example = "20")
  private Integer ageMin;

  @Schema(description = "年齢の上限", example = "35")
  private Integer ageMax;

  @Schema(description = "性別の完全一致条件", example = "女")
  private String sexEq;

  @Schema(description = "コースコードの完全一致条件", example = "JA")
  private String courseCode;

  @Schema(description = "ステータスIDの一致条件。複数指定可", example = "[1,2]")
  private List<Integer> statusId;

  @Schema(description = "受講申込日の開始日（以上）", example = "2026-01-01")
  private LocalDate applyFrom;

  @Schema(description = "受講申込日の終了日（以下）", example = "2026-03-31")
  private LocalDate applyTo;

  @Schema(description = "受講開始日の開始日（以上）", example = "2026-02-01")
  private LocalDate startFrom;

  @Schema(description = "受講開始日の終了日（以下）", example = "2026-04-30")
  private LocalDate startTo;

  @Schema(description = "削除フラグの一致条件", example = "false")
  private Boolean isDeleted;
}

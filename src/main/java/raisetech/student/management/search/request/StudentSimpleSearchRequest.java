package raisetech.student.management.search.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
  @Size(max = 50)
  private String fullNameContains;

  @Schema(description = "受講生かな氏名の部分一致条件", example = "たなか")
  @Size(max = 50)
  private String kanaNameContains;

  @Schema(description = "居住地の部分一致条件", example = "東京")
  @Size(max = 50)
  private String areaContains;

  @Schema(description = "年齢の下限", example = "20")
  private Integer ageMin;

  @Schema(description = "年齢の上限", example = "35")
  private Integer ageMax;

  @Schema(description = "性別の完全一致条件'男''女''その他'のみが入力可能", example = "女")
  @Pattern(
      regexp = "^(男|女|その他)$",
      message = "性別は「男」「女」「その他」から選択できます。"
  )
  private String sexEq;

  @Schema(description = "コースコードの完全一致条件", example = "JA")
  private String courseCode;

  @Schema(description = "ステータスIDの一致条件。1~5の整数値を複数指定可", example = "[1,2]")
  private List<@Min(1) @Max(5)Integer> statusId;

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

  @AssertTrue(message = "年齢は 下限値<上限値 となるように指定してください")
  public boolean isAgeRangeValid() {
    return ageMin == null || ageMax == null || ageMin < ageMax;
  }

  // --- 申込日のチェック ---
  @AssertTrue(message = "申込日の開始日は、終了日より前の日付を指定してください")
  public boolean isApplyDateValid() {
    return isValidRange(applyFrom, applyTo);
  }

  // --- 受講開始日のチェック ---
  @AssertTrue(message = "受講開始日の開始日は、終了日より前の日付を指定してください")
  public boolean isStartDateValid() {
    return isValidRange(startFrom, startTo);
  }

  /**
   * 日付の前後関係をチェックする共通ロジック
   */
  private boolean isValidRange(LocalDate from, LocalDate to) {
    return from == null || to == null || from.isBefore(to);
  }
}

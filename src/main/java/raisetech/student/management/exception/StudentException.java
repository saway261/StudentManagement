package raisetech.student.management.exception;

import lombok.Getter;

/**
 * 受講生管理システムのカスタム例外クラスの抽象クラスです。カスタム例外クラスはこのクラスを継承します。
 */
@Getter
public abstract class StudentException extends Exception {

  /**
   * 例外が発生した箇所をStringで持ちます。
   */
  protected String field;
  /**
   * クライアント側に返すエラーメッセージを持ちます。
   */
  protected String message;

}

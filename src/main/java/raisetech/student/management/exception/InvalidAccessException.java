package raisetech.student.management.exception;

/**
 * 無効なURIへのアクセスがされた際に投げられる例外クラスです。
 */
public class InvalidAccessException extends StudentException {

  public InvalidAccessException() {
    this.field = "URI";
    this.message = "現在無効なURIです。受講生一覧を見るには /students にアクセスしてください。";
  }

}

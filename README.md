# 受講生管理システム

***

## 概要

---
所属しているスクール（RaiseTech）の課題で作成しています。  
架空のプログラミングスクールの受講生の運営スタッフがプロフィールと受講コース情報を管理するためのREST
APIです。このAPIは、以下の処理が可能です。

・受講生詳細の全件検索  
・受講生詳細の個別検索(受講生ID指定)  
・受講生詳細の新規登録  
・受講生詳細の更新（論理削除を含む）

詳細なAPI仕様は、以下のリンクから確認できます。  
[受講生管理システム(Swagger UI)](https://saway261.github.io/StudentManagement/)

## 詳細

---

### 開発環境

**使用技術**  
<img src="https://img.shields.io/badge/language-Java 21-007396.svg">
<img src="https://img.shields.io/badge/framework-springboot 3.4.3-6DB33F.svg?logo=springboot&logoColor=#000000">
<img src="https://img.shields.io/badge/-MySQL-4479A1.svg?logo=mysql&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-MyBatis-990000.svg">
<img src="https://img.shields.io/badge/-JUnit5-25A162.svg?logo=JUnit5&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-h2database-09476B.svg?logo=h2database&logoColor=FFFFFF">

**使用ツール**  
<img src="https://img.shields.io/badge/-IntelliJ IDEA-000000.svg?logo=intellijidea&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-Git-F05032.svg?logo=git&logoColor=F8A899">
<img src="https://img.shields.io/badge/-GitHub-181717.svg?logo=github&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-Postman-FF6C37.svg?logo=postman&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-OpenAPI-6BA539.svg?logo=openapiinitiative&logoColor=FFFFFF">

---

### E-R図(データモデル) Entity-Relation Diagram

受講生と受講生コースは1対多の関係を持つ。受講生と、その受講生のすべてのコースの情報で受講生詳細(
studentDetail)を組み立てる

```mermaid
erDiagram
    STUDENTS ||--|{ STUDENTS_COURSES: "has"
    STUDENTS {
        INT student_id PK
        VARCHAR fullname
        VARCHAR kana_name
        VARCHAR nickname
        VARCHAR email UK
        VARCHAR area
        VARCHAR telephone UK
        INT age
        VARCHAR sex
        VARCHAR remerk
        BOOL is_deleted
    }
    STUDENTS_COURSES {
        INT course_id PK
        VARCHAR course_name PK
        INT student_id FK "外部キー (STUDENTS.student_id)"
        DATE course_start_at
        DATE course_end_at
    }
```

---

### シーケンス図 Sequence Diagram

```mermaid
sequenceDiagram
    actor User
    participant API as API
    participant DB as Database
    Note over User, DB: 全件検索フロー
    User ->> API: GET /students (受講生詳細)
    API ->> DB: SELECT 受講生一覧
    API ->> DB: SELECT 受講生コース一覧
    DB -->> API: 受講生一覧を返す
    DB -->> API: 受講生コース一覧を返す
    API ->> API: 受講生詳細一覧をビルド
    API -->> User: 200 OK (受講生詳細一覧が返る)
    Note over User, DB: 個別検索フロー
    User ->> API: GET /students/{studentId}
    break studentIdの形式が正しくない場合
        API -->> User: 400 BadRequest
    end
    API ->> DB: SELECT 受講生<br>WHERE 受講生ID
    API ->> DB: SELECT 受講生コース<br>WHERE 受講生ID
    DB -->> API: 受講生を返す
    DB -->> API: 受講生コースを返す
    API ->> API: 受講生詳細をビルド
    break 受講生詳細が空の場合
        API -->> User: 404 NotFound:受講生IDが存在しません
    end
    API -->> User: 200 OK (受講生詳細詳細が返る)

```

```mermaid

sequenceDiagram
    actor User
    participant API as API
    participant DB as Database
    Note over User, DB: 受講生詳細の新規登録フロー
    User ->> API: POST /students
    break 受講生詳細の形式が正しくない場合
        API -->> User: 400 BadRequest
    end
    API ->> DB: INSERT 受講生
    API ->> DB: INSERT 受講生コース
    API -->> User: 200 OK (テーブルに登録された受講生詳細が返る)

```

```mermaid
sequenceDiagram
    actor User
    participant API as API
    participant DB as Database
    Note over User, DB: 受講生詳細の更新(論理削除)フロー
    User ->> API: PUT /students
    break 受講生詳細の形式が正しくない場合
        API -->> User: 400 BadRequest
    end
    API ->> DB: SELECT 受講生コースID一覧<br>WHERE 受講生ID
    DB -->> API: 受講生IDに紐づく<br>受講生コースID一覧を返す
    API ->> API: 更新したい受講生コースが<br>受講生と紐づいたものか判定
    break 受講生IDに紐づく<br>受講生コースID一覧が空の場合
        API -->> User: 404 NotFound:受講生IDが存在しません
    end
    break 受講生コースIDが受講生IDと紐づかない場合
        API -->> User: 404 NotFound:受講生コースIDが受講生IDと紐づきません
    end
    API ->> DB: UPDATE 受講生コース<br>WHERE 受講生ID
    API ->> DB: UPDATE 受講生<br>WHERE 受講生コースID
    API -->> User: 200 OK (テーブルで更新された受講生詳細が帰る)
```

---

### クラス図 Class Diagram

概観

```mermaid
---
title: Student Management System(REST API)
---
classDiagram
    class USER {
        <<Conmponent>>
    }
    class student_managementDB {
        <<Component>>
    }
    USER ..> StudentController: request
    direction TB
    StudentController ..> USER: response
    StudentController ..> StudentService: call
    StudentService ..> StudentRepository: call
    StudentRepository ..> student_management DB: query

    class StudentController {
        -StudentServise service
        +getActiveStudentDetailList() List~StudentDetail~
        +viewStudentDetail(int studentId) StudentDetail
        +registerStudentDetail(StudentDetail studentDetail) ResponseEntity~StudentDetail~
        +updateStudentDetail(StudentDetail studentDetail) ResponseEntity~StudentDetail~
    }
    class StudentService {
        -StudentRepository repository
        +searchActiveStudentDetailList() List~StudentDetail~
        +searchstudentDetail(int studentId) StudentDetail
        +registerStudent(StudentDetail studentDetail) StudentDetail
        +updateStudent(StudentDetail studentDetail) StudentDetail
        -buildStudentDetail(int studentId) StudentDetail
        -isLinkedCourseIdWithStudentId(StudentCourse course) boolean
    }
    class StudentRepository {
        +searchActiveStudentList()
        +searchStudent(int studentId) Student
        +searchCourses(int studentId) List~StudentCourses~
        +searchStudentIdList() List~Integer~
        +searchCourseIdListLinkedStudentId(int studentId) List~Integer~
        +registerStudent(Student student)
        +registerCourse(StudentCourse course)
        +updateStudent(Student student)
        +updateCourse(StudentCourse course)
    }
    namespace ExceptionHandling {
        class StudentExceptionHandler {
        }
    }

    StudentController ..> StudentExceptionHandler: throw exception
    USER <.. StudentExceptionHandler: error response


```

例外処理

```mermaid
---
title: Exception Handling
---
classDiagram
    class StudentExceptionHandler {
        +handleInvalidAccessException(InvalidAccessException ex) ResponseEntity~ErrorResponseBody~
        +handleInvalidIdException(InvalidIdException ex) ResponseEntity~ErrorResponseBody~
        +handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) ResponseEntity~ErrorResponseBody~
        +handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) ResponseEntity~ErrorResponseBody~
        +handleConstraintViolationException(ConstraintViolationException ex) ResponseEntity~ErrorResponseBody~
    }

    class ErrorResponseBody {
        - HttpStatus status
        - String message
        - List~Map~ String, String~~ error
    }

    class ErrorDetailBuilder {
        + buildErrorDetails(Exception ex) List~Map~ String, String~~
    }
    class error {
        <<Component>>
    }

    note for StudentExceptionHandler "catch exceptions"
    StudentExceptionHandler ..> ErrorDetailBuilder: uses
    ErrorDetailBuilder ..> error: returns
    StudentExceptionHandler ..> ErrorResponseBody: creates
    ErrorResponseBody *-- error: contains


```

```mermaid
classDiagram
    namespace CustomException {
        class StudentException {
            <<abstract>>
            # String field
            # String message
            + getField()
            + getMessage()
        }
        class InvalidAccessException {
            + getField()
            + getMessage()
        }
        class InvalidIdException {
            + getField()
            + getMessage()
        }
    }
    StudentException ..|> InvalidAccessException
    StudentException ..|> InvalidIdException

```

## 今後の課題

・テストの実施および現時点で想定できていないエラーハンドリングの実装  
・柔軟な検索機能の実装（受講生ID以外による検索を可能にする）


# 受講生管理システム

***

## 概要

---
所属しているスクール（RaiseTech）の課題で作成しています。  
架空のプログラミングスクールの受講生の運営スタッフがプロフィールと受講コース情報を管理するためのREST APIです。このAPIは、以下の操作を行うことができます。

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


**使用ツール**  
<img src="https://img.shields.io/badge/-IntelliJ IDEA-000000.svg?logo=intellijidea&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-Git-F05032.svg?logo=git&logoColor=F8A899">
<img src="https://img.shields.io/badge/-GitHub-181717.svg?logo=github&style={バッチのスタイル}&logoColor=FFFFFF}">
<img src="https://img.shields.io/badge/-Postman-FF6C37.svg?logo=postman&logoColor=FFFFFF">
<img src="https://img.shields.io/badge/-OpenAPI-6BA539.svg?logo=openapiinitiative&logoColor=FFFFFF">

---
### E-R図(データモデル) Entity-Relation Diagram
受講生と受講生コースは1対多の関係を持つ。受講生と、その受講生のすべてのコースの情報で受講生詳細(studentDetail)を組み立てる

```mermaid
---
title: StudentDetail (受講生詳細)
---
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
            INT student_id FK
            DATE course_start_at
            DATE course_end_at
        }
```

---
### シーケンス図 Sequence Diagramm
```mermaid
sequenceDiagram
    actor User
    participant API as API
    participant DB as Database
    Note right of User: 全件検索フロー
        User ->> API: GET /students (受講生詳細)
        API ->> DB: SELECT 受講生一覧
        API ->> DB: SELECT 受講生コース一覧
        DB -->> API: 受講生一覧を返す
        DB -->> API: 受講生コース一覧を返す
        API ->> API: 受講生詳細一覧をビルド
        API -->> User: 200 OK (受講生詳細一覧が返る)

    Note right of User: 個別検索フロー
    User ->> API: GET /students/{studentId}
    alt studentIdの形式が正しくない場合
        API -->> User: 400 BadRequest
    else studentIdの形式が正しい場合
            API ->> DB: SELECT 受講生ID一覧
            DB　-->> API: 受講生ID一覧を返す
            API ->> API: studentIdがテーブルの受講生ID一覧に含まれるか照会
        alt studentIdがテーブルに存在しない場合
            API -->> User: 404 NotFound
        else studentIdがテーブルに存在する場合
            API ->> DB: SELECT 受講生
            API ->> DB: SELECT 受講生コース
            DB -->> API: 受講生を返す
            DB -->> API: 受講生コースを返す
            API ->> API: 受講生詳細をビルド
            API -->> User: 200 OK (受講生詳細詳細が返る)
        end
    end

```

```mermaid

sequenceDiagram
    actor User
    participant API as API
    participant DB as Database
    
    Note right of User: 受講生詳細の新規登録フロー
    User ->> API: POST /students
    alt 受講生詳細の形式が正しくない場合
        API -->> User: 400 BadRequest
    else 受講生詳細の形式が正しい場合
        API ->> DB: INSERT 受講生
        API ->> DB: INSERT 受講生コース
        API -->> User: 200 OK (テーブルに登録された受講生詳細が帰る)
    end
    
```

```mermaid
sequenceDiagram
    actor User
    participant API as API
    participant DB as Database
    
    Note right of User: 受講生詳細の更新(論理削除)フロー
    User ->> API: PUT /students
    alt 受講生詳細の形式が正しくない場合
        API -->> User: 400 BadRequest
    else 受講生詳細の形式が正しい場合
        API ->> DB: UPDATE 受講生
        API ->> DB: UPDATE 受講生コース
        API -->> User: 200 OK (テーブルで更新された受講生詳細が帰る)
    end
```
---

### クラス図 Class Diagram

```mermaid
---
title: Student Management System(REST API)
---
classDiagram
    class USER{
        <<Conmponent>>
    }
    class student_managementDB{
        <<Component>>
 }
    USER ..> StudentController:request
    
    direction TB
    StudentController ..> USER:response
    StudentController ..> StudentService:call
    StudentService ..> StudentRepository:call
    StudentRepository ..> student_management DB:query
    

    class StudentController{
        -StudentServise service
        +getActiveStudentDetailList() List~StudentDetail~
        +viewStudentDetail(int studentId) StudentDetail
        +registerStudent(StudentDetail studentDetail) ResponseEntity~StudentDetail~
        +updateStudent(StudentDetail studentDetail) ResponseEntity~StudentDetail~
    }
    class StudentService{
        -StudentRepository repository
        +searchActiveStudentDetailList() List~StudentDetail~
        +searchstudentDetail(int studentId) StudentDetail
        +registerStudent(StudentDetail studentDetail) StudentDetail
        +updateStudent(StudentDetail studentDetail) StudentDetail
        +isExistStudentId(int studentId) boolean
        +sLinkedCourseIdWithStudentId(int studentId, int courseId) boolean
    }
    class StudentRepository{
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

    class StudentExceptionHandler {
        -errorDetailsBuilder
    }

    StudentController ..> StudentExceptionHandler:throw exception
    StudentExceptionHandler ..> USER:error response
  

```
---
## 力を入れた点
### ・例外の種類のよらないエラーレスポンスフォーマットの形成
```mermaid
 erDiagram
     StudentController 


```

## 今後の課題
・テストの実施および現時点で想定できていないエラーハンドリングの実装  
・柔軟な検索機能の実装（受講生ID以外による検索を可能にする）
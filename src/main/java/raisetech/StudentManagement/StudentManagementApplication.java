package raisetech.StudentManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	private String name = "Enami Kouji";
	private String age = "37";

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo(){
		return name + " " + age + "歳";
	}

	@PostMapping("/studentInfo")
	public void setStudentInfo(String name,String age){
		this.name = name;
		this.age = age;
	}

	@PostMapping("/studentName")
	public void updateStudentName(String name){
		this.name = name;
	}
}

package com.craft.tmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.craft.tmanager")
public class TaskManagerApplication {

//	@Autowired
//    private static PasswordEncryptService passwordEncryptionService;
	
	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
//		passwordEncryptionService.encryptExistingPasswords();
		System.out.println("Task Manager");
	}

}

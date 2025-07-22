package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoData(EmployeeRepository employeeRepository) {
		return args -> {
			// Check if we already have users
			if (employeeRepository.count() == 0) {
				System.out.println("Creating default users...");
				
				// Create Alice (Supervisor)
				Employee alice = new Employee();
				alice.setName("Alice");
				alice.setEmail("alice@example.com");
				alice.setPosition("SUPERVISOR");
				alice.setPassword(passwordEncoder.encode("alice123"));
				alice.setAvailabilityStatus(Employee.AvailabilityStatus.AVAILABLE);
				
				// Save Alice first
				alice = employeeRepository.save(alice);
				
				// Create Bob (Manager)
				Employee bob = new Employee();
				bob.setName("Bob");
				bob.setEmail("bob@example.com");
				bob.setPosition("MANAGER");
				bob.setPassword(passwordEncoder.encode("bob123"));
				bob.setAvailabilityStatus(Employee.AvailabilityStatus.AVAILABLE);
				bob.setManager(alice); // Bob reports to Alice
				
				// Save Bob
				bob = employeeRepository.save(bob);
				
				// Create Charlie (Employee)
				Employee charlie = new Employee();
				charlie.setName("Charlie");
				charlie.setEmail("charlie@example.com");
				charlie.setPosition("EMPLOYEE");
				charlie.setPassword(passwordEncoder.encode("charlie123"));
				charlie.setAvailabilityStatus(Employee.AvailabilityStatus.AVAILABLE);
				charlie.setManager(bob); // Charlie reports to Bob
				
				// Save Charlie
				employeeRepository.save(charlie);
				
				System.out.println("Default users created successfully!");
			}
		};
	}
}

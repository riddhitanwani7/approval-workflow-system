package com.example.demo.controller;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.model.Employee;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/requesters")
    public ResponseEntity<List<Employee>> getPotentialRequesters(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPERVISOR") || a.getAuthority().equals("ROLE_MANAGER"))) {
            // Supervisors and managers can see all employees
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } else if (userDetails != null) {
            // Regular employees can only see themselves
            List<Employee> result = new ArrayList<>();
            employeeService.getEmployeeById(userDetails.getId()).ifPresent(result::add);
            return ResponseEntity.ok(result);
        } else {
            // Fallback for unauthenticated requests
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER') or #id == authentication.principal.id")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.createEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER') or #id == authentication.principal.id")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeRequest);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/supervisor")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<Void> updateSupervisor(@PathVariable Long id, @RequestParam Long supervisorId) {
        boolean updated = employeeService.updateSupervisor(id, supervisorId);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}/toggle-availability")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER') or #id == authentication.principal.id")
    public ResponseEntity<Void> toggleAvailability(@PathVariable Long id) {
        boolean updated = employeeService.toggleAvailability(id);
        if (updated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 
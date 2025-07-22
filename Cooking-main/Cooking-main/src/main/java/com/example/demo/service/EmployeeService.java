package com.example.demo.service;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }
    
    @Transactional
    public Employee createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPosition(employeeRequest.getPosition());
        
        // Encode password
        employee.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
        
        // Set manager if provided
        if (employeeRequest.getManagerId() != null) {
            employeeRepository.findById(employeeRequest.getManagerId())
                .ifPresent(employee::setManager);
        }
        
        return employeeRepository.save(employee);
    }
    
    @Transactional
    public Employee updateEmployee(Long id, EmployeeRequest employeeRequest) {
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setName(employeeRequest.getName());
                employee.setEmail(employeeRequest.getEmail());
                employee.setPosition(employeeRequest.getPosition());
                
                // Update password if provided
                if (employeeRequest.getPassword() != null && !employeeRequest.getPassword().isEmpty()) {
                    employee.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
                }
                
                // Update manager if provided
                if (employeeRequest.getManagerId() != null) {
                    employeeRepository.findById(employeeRequest.getManagerId())
                        .ifPresent(employee::setManager);
                } else {
                    employee.setManager(null);
                }
                
                return employeeRepository.save(employee);
            })
            .orElse(null);
    }
    
    @Transactional
    public boolean deleteEmployee(Long id) {
        return employeeRepository.findById(id)
            .map(employee -> {
                // Find all employees managed by this employee
                List<Employee> subordinates = employeeRepository.findByManager(employee);
                
                // Update subordinates to have the manager of the deleted employee
                subordinates.forEach(subordinate -> {
                    subordinate.setManager(employee.getManager());
                    employeeRepository.save(subordinate);
                });
                
                employeeRepository.delete(employee);
                return true;
            })
            .orElse(false);
    }
    
    @Transactional
    public boolean updateSupervisor(Long employeeId, Long supervisorId) {
        if (employeeId.equals(supervisorId)) {
            return false; // Can't be own supervisor
        }
        
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Optional<Employee> supervisorOpt = employeeRepository.findById(supervisorId);
        
        if (employeeOpt.isPresent() && supervisorOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            Employee supervisor = supervisorOpt.get();
            
            // Check for circular reference
            Employee tempSupervisor = supervisor;
            while (tempSupervisor != null) {
                if (tempSupervisor.getId().equals(employeeId)) {
                    return false; // Circular reference detected
                }
                tempSupervisor = tempSupervisor.getManager();
            }
            
            employee.setManager(supervisor);
            employeeRepository.save(employee);
            return true;
        }
        
        return false;
    }
    
    @Transactional
    public boolean toggleAvailability(Long id) {
        return employeeRepository.findById(id)
            .map(employee -> {
                Employee.AvailabilityStatus newStatus = employee.getAvailabilityStatus() == Employee.AvailabilityStatus.AVAILABLE ? 
                    Employee.AvailabilityStatus.UNAVAILABLE : Employee.AvailabilityStatus.AVAILABLE;
                employee.setAvailabilityStatus(newStatus);
                employeeRepository.save(employee);
                return true;
            })
            .orElse(false);
    }
} 
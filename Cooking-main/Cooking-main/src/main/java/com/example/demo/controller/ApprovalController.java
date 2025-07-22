package com.example.demo.controller;

import com.example.demo.dto.ApprovalRequestDto;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {
    
    @Autowired
    private ApprovalService approvalService;
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<ApprovalRequestDto>> getAllApprovalRequests() {
        List<ApprovalRequestDto> approvalRequests = approvalService.getAllApprovalRequests();
        return ResponseEntity.ok(approvalRequests);
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<List<ApprovalRequestDto>> getPendingApprovalRequests(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ApprovalRequestDto> pendingRequests = approvalService.getPendingApprovalRequestsByApprover(userDetails.getId());
        return ResponseEntity.ok(pendingRequests);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ApprovalRequestDto> getApprovalRequestById(@PathVariable Long id) {
        return approvalService.getApprovalRequestById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER') or #employeeId == authentication.principal.id")
    public ResponseEntity<List<ApprovalRequestDto>> getRequestsByEmployee(@PathVariable Long employeeId) {
        List<ApprovalRequestDto> requests = approvalService.getApprovalRequestsByRequester(employeeId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/approver/{approverId}")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_MANAGER') or #approverId == authentication.principal.id")
    public ResponseEntity<List<ApprovalRequestDto>> getRequestsByApprover(@PathVariable Long approverId) {
        List<ApprovalRequestDto> requests = approvalService.getApprovalRequestsByApprover(approverId);
        return ResponseEntity.ok(requests);
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ApprovalRequestDto> createApprovalRequest(@Valid @RequestBody ApprovalRequestDto approvalRequestDto) {
        ApprovalRequestDto createdRequest = approvalService.createApprovalRequest(approvalRequestDto);
        if (createdRequest != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        }
        return ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_MANAGER')")
    public ResponseEntity<Void> approveRequest(@PathVariable Long id) {
        boolean approved = approvalService.approveRequest(id);
        if (approved) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_MANAGER')")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        boolean rejected = approvalService.rejectRequest(id);
        if (rejected) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/escalate")
    @PreAuthorize("hasAnyRole('ROLE_SUPERVISOR', 'ROLE_MANAGER')")
    public ResponseEntity<Void> escalateRequest(@PathVariable Long id) {
        boolean escalated = approvalService.escalateRequest(id);
        if (escalated) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
} 
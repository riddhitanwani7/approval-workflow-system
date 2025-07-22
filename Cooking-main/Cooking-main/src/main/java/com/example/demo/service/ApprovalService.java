package com.example.demo.service;

import com.example.demo.dto.ApprovalRequestDto;
import com.example.demo.model.ApprovalRequest;
import com.example.demo.model.Employee;
import com.example.demo.repository.ApprovalRequestRepository;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApprovalService {
    
    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public List<ApprovalRequestDto> getAllApprovalRequests() {
        return approvalRequestRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public List<ApprovalRequestDto> getApprovalRequestsByApprover(Long approverId) {
        return employeeRepository.findById(approverId)
            .map(approver -> approvalRequestRepository.findByApprover(approver).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList())
            )
            .orElse(List.of());
    }
    
    public List<ApprovalRequestDto> getPendingApprovalRequestsByApprover(Long approverId) {
        return employeeRepository.findById(approverId)
            .map(approver -> approvalRequestRepository.findByApproverAndStatus(
                approver, ApprovalRequest.RequestStatus.PENDING).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList())
            )
            .orElse(List.of());
    }
    
    public List<ApprovalRequestDto> getApprovalRequestsByRequester(Long requesterId) {
        return employeeRepository.findById(requesterId)
            .map(requester -> approvalRequestRepository.findByRequester(requester).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList())
            )
            .orElse(List.of());
    }
    
    public Optional<ApprovalRequestDto> getApprovalRequestById(Long id) {
        return approvalRequestRepository.findById(id)
            .map(this::convertToDto);
    }
    
    @Transactional
    public ApprovalRequestDto createApprovalRequest(ApprovalRequestDto approvalRequestDto) {
        Optional<Employee> requesterOpt = employeeRepository.findById(approvalRequestDto.getRequesterId());
        
        if (requesterOpt.isEmpty()) {
            return null;
        }
        
        Employee requester = requesterOpt.get();
        
        // Find the direct manager (approver)
        Employee approver = requester.getManager();
        
        // If the manager is unavailable, find the next available manager up the chain
        while (approver != null && approver.getAvailabilityStatus() == Employee.AvailabilityStatus.UNAVAILABLE) {
            approver = approver.getManager();
        }
        
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setRequester(requester);
        approvalRequest.setApprover(approver);
        approvalRequest.setStatus(ApprovalRequest.RequestStatus.PENDING);
        approvalRequest.setDescription(approvalRequestDto.getDescription());
        
        ApprovalRequest savedRequest = approvalRequestRepository.save(approvalRequest);
        return convertToDto(savedRequest);
    }
    
    @Transactional
    public boolean approveRequest(Long id) {
        return updateRequestStatus(id, ApprovalRequest.RequestStatus.APPROVED);
    }
    
    @Transactional
    public boolean rejectRequest(Long id) {
        return updateRequestStatus(id, ApprovalRequest.RequestStatus.REJECTED);
    }
    
    @Transactional
    public boolean escalateRequest(Long id) {
        Optional<ApprovalRequest> requestOpt = approvalRequestRepository.findById(id);
        
        if (requestOpt.isEmpty()) {
            return false;
        }
        
        ApprovalRequest request = requestOpt.get();
        Employee currentApprover = request.getApprover();
        
        if (currentApprover == null || currentApprover.getManager() == null) {
            return false; // No higher approver to escalate to
        }
        
        // Find the next available manager up the chain
        Employee nextApprover = currentApprover.getManager();
        while (nextApprover != null && nextApprover.getAvailabilityStatus() == Employee.AvailabilityStatus.UNAVAILABLE) {
            nextApprover = nextApprover.getManager();
        }
        
        if (nextApprover == null) {
            return false; // No available higher approver
        }
        
        request.setApprover(nextApprover);
        request.setStatus(ApprovalRequest.RequestStatus.ESCALATED);
        approvalRequestRepository.save(request);
        
        return true;
    }
    
    private boolean updateRequestStatus(Long id, ApprovalRequest.RequestStatus status) {
        return approvalRequestRepository.findById(id)
            .map(request -> {
                request.setStatus(status);
                approvalRequestRepository.save(request);
                return true;
            })
            .orElse(false);
    }
    
    private ApprovalRequestDto convertToDto(ApprovalRequest approvalRequest) {
        ApprovalRequestDto dto = new ApprovalRequestDto();
        dto.setId(approvalRequest.getId());
        dto.setRequesterId(approvalRequest.getRequester().getId());
        dto.setRequesterName(approvalRequest.getRequester().getName());
        
        if (approvalRequest.getApprover() != null) {
            dto.setApproverId(approvalRequest.getApprover().getId());
            dto.setApproverName(approvalRequest.getApprover().getName());
        }
        
        dto.setStatus(approvalRequest.getStatus().name());
        dto.setDescription(approvalRequest.getDescription());
        
        if (approvalRequest.getCreatedAt() != null) {
            dto.setCreatedAt(approvalRequest.getCreatedAt().format(formatter));
        }
        
        if (approvalRequest.getUpdatedAt() != null) {
            dto.setUpdatedAt(approvalRequest.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }
} 
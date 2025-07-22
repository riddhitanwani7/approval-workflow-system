package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApprovalRequestDto {
    
    private Long id;
    
    @NotNull(message = "Requester ID is required")
    private Long requesterId;
    
    private Long approverId;
    
    private String status;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private String createdAt;
    
    private String updatedAt;
    
    private String requesterName;
    
    private String approverName;
    
    // Default constructor
    public ApprovalRequestDto() {
    }
    
    // Constructor for creating a new request
    public ApprovalRequestDto(Long requesterId, Long approverId, String description) {
        this.requesterId = requesterId;
        this.approverId = approverId;
        this.description = description;
    }
    
    // Full constructor for response
    public ApprovalRequestDto(Long id, Long requesterId, Long approverId, String status, 
                             String description, String createdAt, String updatedAt,
                             String requesterName, String approverName) {
        this.id = id;
        this.requesterId = requesterId;
        this.approverId = approverId;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.requesterName = requesterName;
        this.approverName = approverName;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRequesterId() {
        return requesterId;
    }
    
    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }
    
    public Long getApproverId() {
        return approverId;
    }
    
    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getRequesterName() {
        return requesterName;
    }
    
    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }
    
    public String getApproverName() {
        return approverName;
    }
    
    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }
} 
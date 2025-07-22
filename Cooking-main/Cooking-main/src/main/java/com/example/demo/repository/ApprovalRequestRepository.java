package com.example.demo.repository;

import com.example.demo.model.ApprovalRequest;
import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    
    List<ApprovalRequest> findByApproverAndStatus(Employee approver, ApprovalRequest.RequestStatus status);
    
    List<ApprovalRequest> findByApprover(Employee approver);
    
    List<ApprovalRequest> findByRequester(Employee requester);
    
    List<ApprovalRequest> findByRequesterAndStatus(Employee requester, ApprovalRequest.RequestStatus status);
} 
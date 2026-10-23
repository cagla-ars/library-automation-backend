package com.cagla.library_automation_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cagla.library_automation_backend.entity.BorrowRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByIsReturnedFalseOrderByBorrowDateDesc();
     List<BorrowRecord> findByStudentNameAndIsReturnedFalse(String studentName);

    List<BorrowRecord> findByStudentNameAndBookIdAndBorrowDateAfter(
            String studentName,
            Long bookId,
            LocalDateTime borrowDate
    );
}
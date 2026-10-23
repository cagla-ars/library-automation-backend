package com.cagla.library_automation_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cagla.library_automation_backend.entity.BorrowRecord;
import com.cagla.library_automation_backend.service.BorrowService;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public List<BorrowRecord> getActiveBorrowRecords() {
        return borrowService.getActiveBorrowRecords();
    }

    @PostMapping
    public BorrowRecord borrowBook(
            @RequestParam Long bookId,
            @RequestParam String studentName
    ) {

        return borrowService.borrowBook(
                bookId,
                studentName
        );
    }

    @PutMapping("/return/{recordId}")
    public BorrowRecord returnBook(
            @PathVariable Long recordId
    ) {

        return borrowService.returnBook(recordId);
    }
}
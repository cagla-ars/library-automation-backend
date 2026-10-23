package com.cagla.library_automation_backend.service;

import com.cagla.library_automation_backend.entity.Book;
import com.cagla.library_automation_backend.entity.BorrowRecord;
import com.cagla.library_automation_backend.exception.BadRequestException;
import com.cagla.library_automation_backend.exception.ResourceNotFoundException;
import com.cagla.library_automation_backend.repository.BookRepository;
import com.cagla.library_automation_backend.repository.BorrowRecordRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BorrowService(
            BorrowRecordRepository borrowRecordRepository,
            BookRepository bookRepository
    ) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
    }

    public List<BorrowRecord> getActiveBorrowRecords() {
        return borrowRecordRepository.findByIsReturnedFalseOrderByBorrowDateDesc();
    }

    public BorrowRecord borrowBook(Long bookId, String studentName) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı."));

        if (book.getAvailableStock() == null || book.getAvailableStock() <= 0) {
            throw new BadRequestException("Bu kitap için yeterli stok bulunmamaktadır.");
        }

        List<BorrowRecord> activeRecords =
                borrowRecordRepository.findByStudentNameAndIsReturnedFalse(studentName);

        if (activeRecords.size() >= 2) {
            throw new BadRequestException("Bir üye aynı anda en fazla 2 kitap ödünç alabilir.");
        }

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

        List<BorrowRecord> recentSameBookRecords =
                borrowRecordRepository.findByStudentNameAndBookIdAndBorrowDateAfter(
                        studentName,
                        bookId,
                        oneWeekAgo
                );

        if (!recentSameBookRecords.isEmpty()) {
            throw new BadRequestException(
                    "Aynı kitap 1 hafta geçmeden tekrar ödünç alınamaz."
            );
        }

        book.setAvailableStock(book.getAvailableStock() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setBookId(book.getId());
        record.setBookTitle(book.getTitle());
        record.setBookAuthor(book.getAuthor());
        record.setStudentName(studentName);
        record.setBorrowDate(LocalDateTime.now());
        record.setDueDate(LocalDateTime.now().plusDays(15));
        record.setIsReturned(false);

        return borrowRecordRepository.save(record);
    }

    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Ödünç kaydı bulunamadı."));

        if (Boolean.TRUE.equals(record.getIsReturned())) {
            throw new BadRequestException("Bu kitap daha önce iade alınmış.");
        }

        Book book = bookRepository.findById(record.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı."));

        book.setAvailableStock(book.getAvailableStock() + 1);
        bookRepository.save(book);

        record.setIsReturned(true);
        record.setReturnDate(LocalDateTime.now());

        return borrowRecordRepository.save(record);
    }
}
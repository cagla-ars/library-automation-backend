package com.cagla.library_automation_backend.service;

import com.cagla.library_automation_backend.entity.Book;
import com.cagla.library_automation_backend.exception.BadRequestException;
import com.cagla.library_automation_backend.exception.ResourceNotFoundException;
import com.cagla.library_automation_backend.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                query,
                query
        );
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı."));
    }

    public Book addBook(Book book) {
        if (book.getTotalStock() == null || book.getTotalStock() <= 0) {
            throw new BadRequestException("Stok miktarı 0'dan büyük olmalıdır.");
        }

        book.setAvailableStock(book.getTotalStock());
        return bookRepository.save(book);
    }
   public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı."));

        if (updatedBook.getTotalStock() == null || updatedBook.getTotalStock() <= 0) {
            throw new BadRequestException("Stok miktarı 0'dan büyük olmalıdır.");
        }

        int oldTotalStock = existingBook.getTotalStock();
        int oldAvailableStock = existingBook.getAvailableStock();

        int newTotalStock = updatedBook.getTotalStock();
        int stockDifference = newTotalStock - oldTotalStock;

        int newAvailableStock = oldAvailableStock + stockDifference;

        if (newAvailableStock < 0) {
            throw new BadRequestException(
                    "Toplam stok, ödünç verilen kitap sayısından düşük olamaz."
            );
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setDescription(updatedBook.getDescription());

        existingBook.setTotalStock(newTotalStock);
        existingBook.setAvailableStock(newAvailableStock);

        return bookRepository.save(existingBook);
    }
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kitap bulunamadı."));

        bookRepository.delete(book);
    }
}
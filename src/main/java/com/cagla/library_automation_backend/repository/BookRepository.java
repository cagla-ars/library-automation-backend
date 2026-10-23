package com.cagla.library_automation_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cagla.library_automation_backend.entity.Book;


public interface BookRepository extends JpaRepository<Book,Long> {
    
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author
    );
}

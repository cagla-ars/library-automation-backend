package com.cagla.library_automation_backend.entity;

import com.cagla.library_automation_backend.entity.enums.BookCategory;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Enumerated(EnumType.STRING)
    private BookCategory category;

    private Integer totalStock;

    private Integer availableStock;

    @Column(columnDefinition = "TEXT")
    private String description;
}
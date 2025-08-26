package com.rahulshettyacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahulshettyacademy.controller.Library;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library, String> {

    // Case-insensitive search by author
    List<Library> findByAuthorIgnoreCase(String authorName);

    // Case-insensitive search by book name
    Library findByBookNameIgnoreCase(String bookName);

    // Or if you want exact match
    List<Library> findByAuthor(String authorName);
    Library findByBookName(String bookName);
}

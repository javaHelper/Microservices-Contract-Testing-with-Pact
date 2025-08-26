package com.rahulshettyacademy.controller;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Storage2")
public class Library {
	@Column(name = "book_name")
	private String bookName;
	@Id
	@Column(name = "id")
	private String id;
	@Column(name = "isbn")
	private String isbn;
	@Column(name = "aisle")
	private int aisle;
	@Column(name = "author")
	private String author;
}

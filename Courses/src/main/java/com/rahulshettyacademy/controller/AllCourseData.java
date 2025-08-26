package com.rahulshettyacademy.controller;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Storage2")
public class AllCourseData {
    @Id
    @Column(name = "course_name")
    private String course_name;

    @Column(name = "id")
    private String id;

    @Column(name = "price")
    private int price;
    @Column(name = "category")
    private String category;
}

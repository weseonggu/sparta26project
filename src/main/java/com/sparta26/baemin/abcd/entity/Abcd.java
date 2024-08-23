package com.sparta26.baemin.abcd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Abcd {

    @Id
    @GeneratedValue
    private Long id;
}


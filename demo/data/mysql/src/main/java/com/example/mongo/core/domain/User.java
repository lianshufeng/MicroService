package com.example.mongo.core.domain;

import com.github.microservice.components.data.jpa.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User extends SuperEntity {

    @Column(unique = true)
    private String name;

    @Column
    private int age;


}

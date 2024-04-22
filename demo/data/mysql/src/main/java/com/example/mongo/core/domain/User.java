package com.example.mongo.core.domain;

import com.github.microservice.components.data.jpa.domain.SuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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

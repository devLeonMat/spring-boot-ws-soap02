package com.rleon.springbootwssoap02.model.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message ="no puede estar vacio")
    @Column(nullable=false)
    private String name;

    @NotEmpty(message ="no puede estar vacio")
    @Column(nullable=false)
    private int age;

    @NotEmpty(message ="no puede estar vacio")
    @Column(name="address", nullable = false)
    private String address;


}

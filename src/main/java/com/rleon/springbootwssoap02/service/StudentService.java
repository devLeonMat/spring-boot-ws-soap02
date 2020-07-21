package com.rleon.springbootwssoap02.service;

import com.rleon.springbootwssoap02.model.entity.Student;

import java.util.Optional;

public interface StudentService {

    Student getStudentByName(String name);

    Optional<Student> getStudentById(Long id);
}

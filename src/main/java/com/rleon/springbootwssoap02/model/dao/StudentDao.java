package com.rleon.springbootwssoap02.model.dao;

import com.rleon.springbootwssoap02.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDao extends JpaRepository<Student, Long> {

    public Student findByName(String name);

}

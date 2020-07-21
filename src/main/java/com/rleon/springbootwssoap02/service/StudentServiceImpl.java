package com.rleon.springbootwssoap02.service;

import com.rleon.springbootwssoap02.model.dao.StudentDao;
import com.rleon.springbootwssoap02.model.entity.Student;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student getStudentByName(String name) {
        return studentDao.findByName(name);
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentDao.findById(id);
    }
}

package com.rleon.springbootwssoap02.controller;


import com.rleon.services.Student;
import com.rleon.services.StudentDetailsRequest;
import com.rleon.services.StudentDetailsResponse;
import com.rleon.springbootwssoap02.exception.ServiceFault;
import com.rleon.springbootwssoap02.exception.ServiceFaultException;
import com.rleon.springbootwssoap02.service.StudentService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class StudentController {
    private static final String NAMESPACE_URI = "http://services.rleon.com/";

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "StudentDetailsRequest")
    @ResponsePayload
    public StudentDetailsResponse getStudentDetailsResponse(@RequestPayload StudentDetailsRequest request) {
        StudentDetailsResponse response = new StudentDetailsResponse();
        if (studentService.getStudentByName(request.getName()) != null) {
            com.rleon.springbootwssoap02.model.entity.Student student = studentService.getStudentByName(request.getName());
            Student st = new Student();
            st.setName(student.getName());
            st.setAddress(student.getAddress());
            st.setAge(student.getAge());
            response.setStudent(st);
        } else {
            ServiceFault serviceFault = new ServiceFault("404", "NOT_FOUND");
            throw new ServiceFaultException("NotFound", serviceFault);
        }

        return response;
    }

}

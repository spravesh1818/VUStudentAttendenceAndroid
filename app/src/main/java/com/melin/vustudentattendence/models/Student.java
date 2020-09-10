package com.melin.vustudentattendence.models;

public class Student {
    private String name;
    private String student_id;
    private String status;

    public Student() {
    }

    public Student(String name, String student_id, String status) {
        this.name = name;
        this.student_id = student_id;
        this.status = status;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

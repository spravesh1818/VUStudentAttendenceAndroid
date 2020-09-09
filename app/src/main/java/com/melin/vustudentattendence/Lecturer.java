package com.melin.vustudentattendence;

public class Lecturer {
    String name,email,department,documentName;

    public Lecturer(String name, String email, String department,String documentName) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.documentName=documentName;
    }

    public Lecturer() {
    }

    public Lecturer(String email) {
        this.email = email;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

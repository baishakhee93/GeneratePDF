package com.generatepdf.com;

import java.io.Serializable;

public class StudentModel implements Serializable {
    String rollNumber,name,classes,section,gender,totalMarks;

    public StudentModel(String rollNumber, String name, String classes, String section, String gender, String totalMarks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.classes = classes;
        this.section = section;
        this.gender = gender;
        this.totalMarks = totalMarks;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }
}

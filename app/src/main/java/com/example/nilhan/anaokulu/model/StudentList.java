package com.example.nilhan.anaokulu.model;

import java.util.Hashtable;

/**
 * Created by nilhan on 06.12.2015.
 */
public class StudentList {
    Hashtable<Beacon, Student> studentList;

    public StudentList() {
        studentList = new Hashtable<Beacon, Student>();
    }

    public void initialize() {
        Beacon beacon = new Beacon("699EBC80-E1F3-11E3-9A0F-0CF3EE3BC012", 1, 16575);
        studentList.put(beacon, new Student(beacon, "Dicle Ilhan"));
        beacon = new Beacon("699EBC80-E1F3-11E3-9A0F-0CF3EE3BC012", 1, 16565);
        studentList.put(beacon, new Student(beacon,"Baris Ilhan"));
    }

    public String getStudentName(Beacon beacon) {
        return studentList.get(beacon).getName();
    }
}

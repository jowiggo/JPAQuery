package se.yrgo.domain;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Student
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(unique=true, nullable=false)
    private String enrollmentID;
    private String name;

    @Embedded
    private Address address;


    public Student() {}

    public String getEnrollmentID() {
        return enrollmentID;
    }

    public Student(String name, String enrollmentID, String street, String city,
                   String zipCode){
        this.name = name;
        this.enrollmentID= enrollmentID;
        this.address = new Address(street,city,zipCode);
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address newAddress) {
        this.address = newAddress;
    }

    public String toString() {
        return name + " lives at: " + address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getEnrollmentID(), student.getEnrollmentID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getEnrollmentID());
    }
}

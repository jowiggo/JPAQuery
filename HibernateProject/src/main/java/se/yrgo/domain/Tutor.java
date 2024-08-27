package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "findTutorsWithSalaryAbove", query = "SELECT t.name, t.salary FROM Tutor t WHERE t.salary > :reqSalary")
})
public class Tutor {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String tutorId;
    private String name;
    private int salary;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name="TUTOR_FK")
    private Set<Student> teachingGroup;

    @ManyToMany(mappedBy="tutors")
    private Set<Subject>subjectsToTeach;

    public Tutor() {}

    public Tutor(String tutorId,String name, int salary) {
        this.tutorId= tutorId;
        this.name=name;
        this.salary= salary;
        this.teachingGroup = new HashSet<Student>();
        this.subjectsToTeach = new HashSet<Subject>();

    }

    public void addStudentToTeachingGroup(Student newStudent) {
        this.teachingGroup.add(newStudent);

    }

    public void addSubjectsToTeach(Subject subject) {
        this.subjectsToTeach.add(subject);
        subject.getTutors().add(this);
    }

    public void createStudentAndAddtoTeachingGroup(String studentName,
                                                   String enrollmentID,String street, String city,
                                                   String zipcode) {
        Student student = new Student(studentName, enrollmentID,
                street,city,zipcode);
        this.addStudentToTeachingGroup(student);
    }
    public String getName() {
        return name;
    }


    public Set<Student> getTeachingGroup() {

        Set<Student>unmodifiable=
                Collections.unmodifiableSet(this.teachingGroup);
        return unmodifiable;
    }

    public Set<Subject> getSubjects() {
        return this.subjectsToTeach;
    }


    public String getTutorId() {
        return tutorId;
    }

    public String toString(){
        return name;
    }
}
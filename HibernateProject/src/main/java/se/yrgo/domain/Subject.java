package se.yrgo.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQueries({
    @NamedQuery(name = "findSubjectByName", query = "SELECT s FROM Subject s WHERE s.subjectName = :name")
})
public class Subject {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String subjectName;
    private int numberOfSemesters;

    @ManyToMany
    private Set<Tutor> tutors;


    public Subject() {}

    public Subject(String subjectName, int numberOfSemesters) {
        this.subjectName = subjectName;
        this.numberOfSemesters=numberOfSemesters;
        this.tutors = new HashSet<Tutor>();
    }

    public String subjectName() {
        return subjectName;
    }

    public void addTutorToSubject(Tutor tutor) {
        this.tutors.add(tutor);
        tutor.getSubjects().add(this);
    }

    public Set<Tutor> getTutors() {
        return this.tutors;
    }
}


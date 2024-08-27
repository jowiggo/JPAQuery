package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.util.ArrayList;
import java.util.List;

public final class App {
    private static final EntityManagerFactory emf;
    private static final EntityManager em;
    private static final EntityTransaction tx;

    static {
        emf = Persistence.createEntityManagerFactory("databaseConfig");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    public static void main(String[] args) {
        setUpData();
        taskOne();
        taskTwo();
        taskThree();
        taskFour();
        taskFive();
    }

    /**
     * Create example data and persist to database.
     */
    private static void setUpData() {
        try {
            tx.begin();
            Subject subject1 = new Subject("Math", 3);
            Subject subject2 = new Subject("Science", 3);
            Subject subject3 = new Subject("Geography", 2);
            Subject subject4 = new Subject("Arts and craft", 1);

            Tutor tutor1 = new Tutor("JSMI", "John Smith", 42000);
            Tutor tutor2 = new Tutor("MABR", "Maria Brown", 39500);
            Tutor tutor3 = new Tutor("PAJO", "Paul Johnson", 9800);
            Tutor tutor4 = new Tutor("CLDA", "Claire Davis", 8000);
            Tutor tutor5 = new Tutor("EMCL", "Emily Clark", 23900);


            var tutors = new ArrayList<>(List.of(tutor1, tutor2, tutor3, tutor4, tutor5));
            tutors.forEach(em::persist);

            tutor1.addSubjectsToTeach(subject1);
            tutor1.addSubjectsToTeach(subject2);
            tutor1.addSubjectsToTeach(subject3);
            tutor2.addSubjectsToTeach(subject3);
            tutor3.addSubjectsToTeach(subject1);
            tutor4.addSubjectsToTeach(subject4);
            tutor5.addSubjectsToTeach(subject3);

            tutor1.createStudentAndAddtoTeachingGroup("Ethan Parker", "stu-25-7102", "Orchard Avenue 5", "Burbank", "91507 CA");
            tutor1.createStudentAndAddtoTeachingGroup("Sophia Mitchell", "stu-25-2147", "Willow Street 58 apt 15", "Burbank", "91508 CA");
            tutor1.createStudentAndAddtoTeachingGroup("Lucas Harper", "stu-25-3195", "Cedar Lane 330", "Burbank", "91509 CA");
            tutor1.createStudentAndAddtoTeachingGroup("Ava Thompson", "stu-25-0562", "King's Road 12", "Burbank", "91511 CA");
            tutor2.createStudentAndAddtoTeachingGroup("Isabella Garcia", "stu-25-4401", "Baker Street 123", "Burbank", "92506 CA");
            tutor2.createStudentAndAddtoTeachingGroup("James Miller", "stu-25-0836", "Elm Drive 45 apt 7", "Burbank", "91512 CA");
            tutor3.createStudentAndAddtoTeachingGroup("Lily Roberts", "stu-25-5093", "Pine Crest 89 apt 310", "Burbank", "91906 CA");
            tutor4.createStudentAndAddtoTeachingGroup("Henry Wilson", "stu-25-0527", "Summit Drive 102", "Burbank", "91513 CA");
            tutor4.createStudentAndAddtoTeachingGroup("Grace Edwards", "stu-25-8421", "Mountain Road 15", "Burbank", "91514 CA");
            tutor5.createStudentAndAddtoTeachingGroup("Olivia Brooks", "stu-25-7558", "Lakeview Avenue 78", "Burbank", "91516 CA");

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query för att få fram alla namn på studenter vars lärare utbildar i science.
     */
    private static void taskOne() {
        try {
            tx.begin();
            String subjectName = "Science";
            
            // Hämta Subject
            Subject science = null;
            try {
                TypedQuery<Subject> fetchSubject = em.createNamedQuery("findSubjectByName", Subject.class)
                        .setParameter("name", subjectName);
                science = fetchSubject.getSingleResult();
            } catch (NoResultException nre) {
                System.out.println("No subject found with name: " + subjectName);
            }
    
            if (science != null) {
                var results = em.createQuery("select s.name from Tutor t join t.teachingGroup s where :subject member of t.subjectsToTeach", String.class)
                        .setParameter("subject", science).getResultList();
                System.out.println("------Result from task one------");
                results.forEach(System.out::println);
            }
    
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query för att hitta alla namn av stundeter och deras lärare.
     */
    private static void taskTwo() {
        try {
            tx.begin();
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery("select s.name, t.name from Tutor t join t.teachingGroup s").getResultList();

            System.out.println("------Result from task two------");
            for (Object[] pair : results) {
                System.out.println((results.indexOf(pair) + 1) + " Student: " + pair[0] + ", \t Tutor: " + pair[1]);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /**
     * Query för att räkna ut medellängden på en termin.
     */
    private static void taskThree() {
        try {
            tx.begin();
            Double result = (Double) em.createQuery("select avg(sub.numberOfSemesters) from Subject sub").getSingleResult();
    
            if (result == null) {
                System.out.println("No subjects found or all subjects have null numberOfSemesters.");
            } else {
                System.out.println("------Result from task three------");
                System.out.printf("Average semester length: %.2f%n%n", result);
            }
    
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query för att hitta den som har högst lön bland alla lärare.
     */
    private static void taskFour() {
        try {
            tx.begin();
            var highestSalary = (Integer) em.createQuery("select max(t.salary) from Tutor t").getSingleResult();

            System.out.println("------Result from task four-----");
            System.out.printf("Highest salary for tutor: %d $%n%n", highestSalary);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Query för attt hitta alla lärare som har en lön högre än 10 000.
     */
    private static void taskFive() {
        try {
            tx.begin();
    
            var tenThousand = 10000;
            List<Object[]> results = em.createNamedQuery("findTutorsWithSalaryAbove", Object[].class)
                    .setParameter("reqSalary", tenThousand)
                    .getResultList();
    
            System.out.println("------Result from task five-----");
            for (Object[] result : results) {
                System.out.printf("%s's salary: %d $%n", result[0], result[1]);
            }
    
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
            em.close();
        }
    }
}

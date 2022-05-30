package entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Data
@ToString
@Entity
@Table
public class Student {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String first_name;

    @Column
    private String middle_name;

    @Column
    private String last_name;

    @Column
    private String date_of_birth;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(first_name, student.first_name) && Objects.equals(middle_name, student.middle_name) && Objects.equals(last_name, student.last_name) && Objects.equals(date_of_birth, student.date_of_birth);
    }

}
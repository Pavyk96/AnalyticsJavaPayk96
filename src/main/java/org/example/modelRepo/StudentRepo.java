package org.example.modelRepo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
public class StudentRepo {
    @Id
    private String studentId;

    @Column
    private int score;

    @Column
    private int itGroups;

    @Column
    private int cityId;
}

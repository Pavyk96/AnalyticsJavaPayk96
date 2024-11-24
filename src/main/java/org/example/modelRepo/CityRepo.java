package org.example.modelRepo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "city")
public class CityRepo {
    @Id
    private int id;

    @Column
    private String name;
}

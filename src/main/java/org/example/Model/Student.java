package org.example.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String idUlern;
    private String fullName;
    private int score;
    private String group;
    private City city;
}

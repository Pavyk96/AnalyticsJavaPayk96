package org.example.model;

import com.vk.api.sdk.objects.base.City;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student {
    private String id;
    private String fullName;
    private int score;
    private String group;
    private City city;
    private int countOfItGroups;
}

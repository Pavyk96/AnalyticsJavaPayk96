package org.example.Model;

import com.vk.api.sdk.objects.base.City;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private String id;
    private String fullName;
    private int score;
    private String group;
    private City city;
}

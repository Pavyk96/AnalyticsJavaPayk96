package org.example.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private int score;
    private TaskType type;
}

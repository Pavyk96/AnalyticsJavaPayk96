package org.example.Model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private int score;
    private TaskType type;
}

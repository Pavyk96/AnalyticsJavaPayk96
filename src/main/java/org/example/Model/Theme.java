package org.example.Model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Theme {
    private String name;
    private List<Task> tasks;
}

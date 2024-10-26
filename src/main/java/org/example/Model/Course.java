package org.example.Model;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Course {
    private String name;
    private List<Theme> themes;
}

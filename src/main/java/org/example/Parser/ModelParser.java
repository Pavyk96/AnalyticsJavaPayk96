package org.example.Parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.Model.Student;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModelParser {

    protected static Path path = Paths.get("java-rtf.csv");
    protected static List<Student> studentList = new ArrayList<>();

    public static void CsvToStudents() {

        try (CSVReader reader = new CSVReader(new FileReader(path.toString()))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                Student student = parseRecord(record);
                studentList.add(student);
            }

            studentList.forEach(System.out::println);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private static Student parseRecord(String[] record) {
        String name = record[0];
        String id = record[1];
        String group = record[2];
        int score = Integer.parseInt(record[3])
                    +Integer.parseInt(record[4])
                    +Integer.parseInt(record[5]);
        return new Student(id, name, score, group);
    }
}

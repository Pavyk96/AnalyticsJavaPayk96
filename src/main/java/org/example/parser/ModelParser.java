package org.example.parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Getter;
import org.example.model.Student;
import org.example.model.Theme;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ModelParser {

    protected static Path path = Paths.get("java-rtf.csv");

    public static List<Student> studentList = new ArrayList<>();
    public static List<Theme> themeList = new ArrayList<>();

    public static void CsvToStudents() {
        try (CSVReader reader = new CSVReader(new FileReader(path.toString()))) {
            reader.skip(3);
            List<String[]> records = reader.readAll(); //nextRead
            for (String[] record : records) {
                Student student = parseRecord(record);
                studentList.add(student);
            }
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
        return new Student(id, name, score, group, null, 0);
    }

    public static void CsvToThemes() {
        try (CSVReader reader = new CSVReader(new FileReader(path.toString()))) {
            String[] firstRow = removeEmptyStrings(reader.readNext());

            long idCounter = 1;

            for (int i = 1; i < firstRow.length - 1; i++) {
                String themeName = firstRow[i];
                if (!themeName.isEmpty()) {
                    Theme theme = new Theme(idCounter++, themeName.trim());
                    themeList.add(theme);
                    }
                }

            themeList.forEach(System.out::println);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private static String[] removeEmptyStrings(String[] input) {
        return Arrays.stream(input)
                .filter(s -> s != null && !s.trim().isEmpty())
                .toArray(String[]::new);
    }
}

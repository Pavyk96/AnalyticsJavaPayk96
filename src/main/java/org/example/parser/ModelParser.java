package org.example.parser;



import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Getter;
import org.example.model.Student;
import org.example.model.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
public class ModelParser {

    protected static Path path = Paths.get("java-rtf.csv");

    public static List<Student> studentList = new ArrayList<>();
    public static List<Theme> themeList = new ArrayList<>();

    public static void parseCsvToStudents() {
        try (CSVReader reader = new CSVReader(new FileReader(path.toString()))) {
            reader.skip(3); // Пропускаем первые три строки
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                Student student = parseStudentRecord(record);
                if (student != null) {
                    studentList.add(student);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public static void parseCsvToThemes() {
        try (CSVReader reader = new CSVReader(new FileReader(path.toString()))) {
            String[] firstRow = reader.readNext();
            if (firstRow != null) {
                String[] cleanedRow = removeEmptyStrings(firstRow);
                long idCounter = 1;
                for (int i = 1; i < cleanedRow.length - 1; i++) {
                    String themeName = cleanedRow[i];
                    if (!themeName.isEmpty()) {
                        Theme theme = parseThemeRecord(themeName, idCounter++);
                        themeList.add(theme);
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private static Student parseStudentRecord(String[] record) {
        if (record.length < 6) {
            return null; // Пропускаем некорректные записи
        }
        try {
            String name = record[0];
            String id = record[1];
            String group = record[2];
            int score = Integer.parseInt(record[3])
                    + Integer.parseInt(record[4])
                    + Integer.parseInt(record[5]);
            return new Student(id, name, score, group, null, 0);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Theme parseThemeRecord(String themeName, long id) {
        return new Theme(id, themeName.trim());
    }

    private static String[] removeEmptyStrings(String[] input) {
        return Arrays.stream(input)
                .filter(s -> s != null && !s.trim().isEmpty())
                .toArray(String[]::new);
    }


    @Nested
    class ModelParserTest {

        @BeforeEach
        void setUp() {
            // Очищаем списки перед каждым тестом
            ModelParser.studentList.clear();
            ModelParser.themeList.clear();
        }

        @Test
        void parseStudentRecord_validRecord() {
            String[] record = {"John Doe", "123", "A1", "80", "90", "85"};
            Student student = ModelParser.parseStudentRecord(record);

            assertNotNull(student);
            assertEquals("123", student.getId());
            assertEquals("John Doe", student.getFullName());
            assertEquals("A1", student.getGroup());
            assertEquals(255, student.getScore());
        }

        @Test
        void parseStudentRecord_invalidRecord() {
            String[] record = {"John Doe", "123", "A1"}; // Недостаточно полей
            Student student = ModelParser.parseStudentRecord(record);

            assertNull(student);
        }


        @Test
        void parseThemeRecord_validTheme() {
            String themeName = "Java Basics";
            Theme theme = ModelParser.parseThemeRecord(themeName, 1);

            assertNotNull(theme);
            assertEquals(1, theme.getId());
            assertEquals("Java Basics", theme.getName());
        }

        @Test
        void removeEmptyStrings_validInput() {
            String[] input = {"Java", "", "  ", null, "Python"};
            String[] result = ModelParser.removeEmptyStrings(input);

            assertNotNull(result);
            assertArrayEquals(new String[]{"Java", "Python"}, result);
        }

        @Test
        void parseCsvToStudents_validCsv() {
            // Мокаем данные CSV
            String[][] mockData = {
                    {"John Doe", "123", "A1", "80", "90", "85"},
                    {"Jane Smith", "456", "B2", "75", "85", "95"}
            };

            Arrays.stream(mockData).forEach(record -> {
                Student student = ModelParser.parseStudentRecord(record);
                if (student != null) {
                    ModelParser.studentList.add(student);
                }
            });

            assertEquals(2, ModelParser.studentList.size());

            Student firstStudent = ModelParser.studentList.get(0);
            assertEquals("123", firstStudent.getId());
            assertEquals(255, firstStudent.getScore());
        }
    }


}




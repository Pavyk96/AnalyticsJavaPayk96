package org.example;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.example.modelRepo.StudentRepo;
import org.example.parser.ModelParser;
import org.example.util.DatabaseUtil;
import org.example.vkRepo.VkApiRepo;
import org.example.windowApp.drawer.AverageScoreCityChartDrawer;
import org.example.windowApp.drawer.PieChartDrawer;
import org.example.windowApp.drawer.ScoreVsItGroupsChartDrawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App
{
    public static void main( String[] args ) throws IOException, ClientException, ApiException, InterruptedException {
        //ModelParser.CsvToStudents();
        //VkApiRepo.UppdateStudentInf();
        ArrayList<StudentRepo> studentRepoList = (ArrayList<StudentRepo>) DatabaseUtil.readStudents();

        // Фильтрация списка студентов, исключая тех, у кого город "Неизвестный город"
        List<StudentRepo> filteredStudents = studentRepoList.stream()
                .filter(student -> !student.getCityName().equals("Неизвестный город"))
                .collect(Collectors.toList());

        // Печать списка студентов (фильтрованный список)
        DatabaseUtil.printStudents(new ArrayList<>(filteredStudents));

        // Рисование графиков с фильтрованными данными
        new PieChartDrawer("аналитика по джаве от payk96", new ArrayList<>(filteredStudents)).setVisible(true);
        new ScoreVsItGroupsChartDrawer("Зависимость баллов от IT-групп", studentRepoList).setVisible(true);
        new AverageScoreCityChartDrawer("Гистограмма баллов по городам", new ArrayList<>(filteredStudents)).setVisible(true);
    }
}

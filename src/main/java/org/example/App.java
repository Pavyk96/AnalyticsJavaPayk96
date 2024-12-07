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

public class App
{
    public static void main( String[] args ) throws IOException, ClientException, ApiException, InterruptedException {
        //ModelParser.CsvToStudents();
        //VkApiRepo.UppdateStudentInf();
        ArrayList<StudentRepo> studentRepoList = (ArrayList<StudentRepo>) DatabaseUtil.readStudents();
        DatabaseUtil.printStudents(studentRepoList);

        new PieChartDrawer("аналитика по джаве от payk96", studentRepoList).setVisible(true);

        new ScoreVsItGroupsChartDrawer("Зависимость баллов от IT-групп", studentRepoList).setVisible(true);

        new AverageScoreCityChartDrawer("Гистограмма баллов по городам", studentRepoList).setVisible(true);

    }
}

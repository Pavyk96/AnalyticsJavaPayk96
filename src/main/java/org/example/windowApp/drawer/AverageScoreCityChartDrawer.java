package org.example.windowApp.drawer;

import org.example.modelRepo.StudentRepo;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AverageScoreCityChartDrawer extends JFrame {

    public AverageScoreCityChartDrawer(String title, List<StudentRepo> students) {
        super(title);
        setContentPane(createCityScorePanel(students));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 600);
    }

    private JPanel createCityScorePanel(List<StudentRepo> students) {
        CategoryDataset dataset = createAverageCityScoreDataset(students);
        JFreeChart chart = createBarChart(dataset);
        return new ChartPanel(chart);
    }

    private static CategoryDataset createAverageCityScoreDataset(List<StudentRepo> students) {
        Map<String, Double> averageScoresByCity = students.stream()
                .collect(Collectors.groupingBy(
                        StudentRepo::getCityName,
                        Collectors.averagingDouble(StudentRepo::getScore)
                ));

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        averageScoresByCity.forEach((city, averageScore) -> dataset.addValue(averageScore, "Средний балл", city));

        return dataset;
    }

    private static JFreeChart createBarChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Средний балл студентов по городам", // Заголовок графика
                "Город",                            // Метка оси X
                "Средний балл",                     // Метка оси Y
                dataset,                            // Данные
                PlotOrientation.VERTICAL,           // Ориентация графика
                false,                              // Легенда
                true,                               // Подсказки
                false                               // URLs
        );

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Настройка отображения
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        return chart;
    }
}

package org.example.windowApp.drawer;

import org.example.modelRepo.StudentRepo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ScoreVsItGroupsChartDrawer extends JFrame {
    public ScoreVsItGroupsChartDrawer(String title, List<StudentRepo> studentList) {
        super(title);
        setContentPane(createScoreVsItGroupsPanel(studentList));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(600, 300);
    }

    public static JPanel createScoreVsItGroupsPanel(List<StudentRepo> studentList) {
        JFreeChart chart = createLineChart(createScoreVsItGroupsDataset(studentList));
        return new ChartPanel(chart);
    }

    private static XYDataset createScoreVsItGroupsDataset(List<StudentRepo> studentList) {
        // Диапазоны групп
        int[] intervals = {0, 2, 5, 8, 12}; // Пример диапазонов
        String[] intervalLabels = {"0–2", "3–5", "6–8", "9–12"};
        double[] intervalMidpoints = {1, 4, 6.5, 10.5}; // Средние точки для интервалов

        // Подготовка данных
        Map<String, List<Integer>> groupedScores = new LinkedHashMap<>();
        for (String label : intervalLabels) {
            groupedScores.put(label, new ArrayList<>());
        }

        // Распределение студентов по диапазонам
        for (StudentRepo student : studentList) {
            int itGroups = student.getItGroups();
            int score = student.getScore();

            for (int i = 0; i < intervals.length - 1; i++) {
                if (itGroups >= intervals[i] && itGroups <= intervals[i + 1]) {
                    groupedScores.get(intervalLabels[i]).add(score);
                    break;
                }
            }
        }

        // Вычисление среднего балла для каждого диапазона и добавление в серию
        XYSeries series = new XYSeries("Средний балл по диапазонам IT-групп");
        for (int i = 0; i < intervalLabels.length; i++) {
            List<Integer> scores = groupedScores.get(intervalLabels[i]);
            if (!scores.isEmpty()) {
                double averageScore = scores.stream().mapToInt(Integer::intValue).average().orElse(0);
                series.add(intervalMidpoints[i], averageScore); // Добавляем среднюю точку интервала по X
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }



    private static JFreeChart createLineChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Зависимость баллов от IT-групп",
                "Количество IT-групп",
                "Количество баллов",
                dataset,
                PlotOrientation.VERTICAL,
                true, // legend
                true, // tooltips
                false // URLs
        );

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Настройка отображения точек и линий
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);

        plot.setRenderer(renderer);

        // Настройка внешнего вида
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        chart.setBackgroundPaint(Color.WHITE);
        return chart;
    }

}


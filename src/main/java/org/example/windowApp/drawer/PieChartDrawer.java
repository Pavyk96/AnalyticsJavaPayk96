package org.example.windowApp.drawer;

import org.example.modelRepo.StudentRepo;
import org.example.windowApp.mapper.DataMapper;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PieChartDrawer extends JFrame {
    public PieChartDrawer(String title, ArrayList<StudentRepo> list) {
        super(title);
        setContentPane(createPlayersByTeamsPanel(list));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(600, 300);
    }

    public static JPanel createPlayersByTeamsPanel(ArrayList<StudentRepo> playerList)
    {
        JFreeChart chart = createPieChart(DataMapper.createCountCityByNameDataset(playerList));
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        return new ChartPanel(chart);
    }

    private static JFreeChart createPieChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "сважечка после тяжечки",  // chart title
                dataset,             // data
                false,               // no legend
                true,                // tooltips
                false                // no URL generation
        );

        chart.setBackgroundPaint(
                new GradientPaint(
                        new Point(0, 0),
                        new Color(0, 0, 0),
                        new Point(400, 200),
                        Color.DARK_GRAY
                )
        );

        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setPaint(new Color(240, 240, 240));
        t.setFont(new Font("Arial", Font.BOLD, 26));
        t.setText("Количество студентов в зависимости от города");

        return chart;
    }
}

package org.example.windowApp.mapper;

import org.example.modelRepo.StudentRepo;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataMapper {
    public static PieDataset createCountCityByNameDataset(List<StudentRepo> list) {
        HashMap<String, Long> cityCointByName = list.stream()
                .collect(
                        Collectors.groupingBy(
                                StudentRepo::getCityName,
                                HashMap::new,
                                Collectors.counting()
                        )
                );

        DefaultPieDataset dataset = new DefaultPieDataset();

        cityCointByName.forEach(dataset::setValue);

        return dataset;
    }
}

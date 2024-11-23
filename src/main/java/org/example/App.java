package org.example;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.example.Parser.ModelParser;
import org.example.vkRepo.VkApiRepo;

import java.io.IOException;

public class App
{
    public static void main( String[] args ) throws IOException, ClientException, ApiException, InterruptedException {
        ModelParser.CsvToStudents();
        VkApiRepo.UppdateStudentInf();
    }
}

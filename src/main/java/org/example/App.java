package org.example;

import org.example.Parser.ModelParser;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ModelParser.CsvToStudents();
        ModelParser.CsvToThemes();
    }
}

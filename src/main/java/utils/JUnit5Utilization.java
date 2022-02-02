package utils;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import projectCrawler.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class JUnit5Utilization {

    private final ProjectCrawler projectCrawler;
    private final Set<String> fileNames = new HashSet<>();

    public JUnit5Utilization(String projectPath) throws FileNotFoundException {
        OutputWriter.getInstance().setOutputFile(projectPath);
        this.projectCrawler = new ProjectCrawler(projectPath);
        projectCrawler.run();
    }

    public void findJUnit5Imports() throws IOException {
        for (TestClass testClass : projectCrawler.getTestClasses()) {
            CompilationUnit cu = StaticJavaParser.parse(testClass.getClassContent());
            for (ImportDeclaration importDeclaration : cu.getImports()) {
                String importName = importDeclaration.getName().asString();
                if (importName.contains("org.junit.jupiter")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), splitImport[splitImport.length - 1]);
                    fileNames.add(testClass.getClassName());
                }
                if (importName.contains("org.testng")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), "TestNG");
                    fileNames.add(testClass.getClassName());
                }
                if (importName.contains("org.hamcrest")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), "Hamcrest");
                    fileNames.add(testClass.getClassName());
                }
                if (importName.contains("org.assertj")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), "AssertJ");
                    fileNames.add(testClass.getClassName());
                }
                if (importName.contains("org.mockito")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), "Mockito");
                    fileNames.add(testClass.getClassName());
                }
                if (importName.contains("cucumber.api")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), "Cucumber");
                    fileNames.add(testClass.getClassName());
                }
                if (importName.contains("net.serenitybdd")) {
                    String[] splitImport = importName.split("\\.");
                    OutputWriter.getInstance().write(testClass.getAbsolutePath(), "Serenity");
                    fileNames.add(testClass.getClassName());
                }
            }
        }
        OutputWriter.csvWriter.close();
    }
}

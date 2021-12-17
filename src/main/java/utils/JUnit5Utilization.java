package utils;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import projectCrawler.*;

import java.io.*;

public class JUnit5Utilization {

    private final ProjectCrawler projectCrawler;

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
                }
            }
        }
        OutputWriter.csvWriter.close();
    }
}

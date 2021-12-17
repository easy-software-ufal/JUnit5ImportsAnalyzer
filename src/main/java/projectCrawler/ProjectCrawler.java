package projectCrawler;

import com.github.javaparser.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.*;
import utils.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class ProjectCrawler {
    private final List<TestClass> testClasses;
    private final File rootFile;

    public ProjectCrawler(String rootDirectory) {
        this.testClasses = new LinkedList<>();
        this.rootFile = new File(rootDirectory);
    }

    public void run() throws FileNotFoundException {
        run(rootFile);
    }

    private void run(File rootFile) throws FileNotFoundException {
        File[] listedFiles = rootFile.listFiles();
        if (listedFiles != null) {
            for (File file : listedFiles) {
                if (file.isDirectory()) {
                    run(file);
                } else if (file.isFile()) {
                    if (file.getName().endsWith(".java")) {
                        Logger logger = Logger.getLogger("Sniffer");
                        logger.info("Analyzing file " + file.getAbsolutePath());
                        List<TestMethod> testMethods = gatherAllTestMethodsFromFile(new LinkedList<>(), file);
                        if (testMethods.size() > 0) {
                            testClasses.add(new TestClass(testMethods, file.getName(), StaticJavaParser.parse(file).toString(), file.getAbsolutePath()));
                        }
                    }
                }
            }
        }
    }

    public List<TestMethod> gatherAllTestMethodsFromFile(List<TestMethod> testMethods, File javaFile) throws FileNotFoundException {
        try {
            new VoidVisitorAdapter<>() {
                @Override
                public void visit(MethodDeclaration n, Object arg) {
                    super.visit(n, arg);
                    if (n.getAnnotations().size() != 0) {
                        for (AnnotationExpr annotationExpr : n.getAnnotations()) {
                            if (annotationExpr.getNameAsString().equals("Test")) {
                                if (n.getRange().isPresent()) {
                                    testMethods.add(new TestMethod(n.getRange().get().begin.line,
                                            n.getRange().get().end.line,
                                            n.getNameAsString(),
                                            n.asMethodDeclaration(),
                                            javaFile.getAbsolutePath()));
                                    break;
                                }
                            }
                        }
                    }
                }
            }.visit(StaticJavaParser.parse(javaFile), null);
        } catch (ParseProblemException e) {
            return new LinkedList<>();
        }
        return testMethods;
    }

    public List<TestClass> getTestClasses() {
        return testClasses;
    }
}

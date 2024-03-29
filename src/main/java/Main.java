import utils.*;

import java.io.*;
import java.util.*;

public class Main {
    public synchronized static void main(String[] args) throws Exception {
        if (args.length > 0) {
            if (args[0].equals("-multipleProjects")) {
                File rootDir = new File(args[1]);
                for (File file : Objects.requireNonNull(rootDir.listFiles())) {
                    if (file.isDirectory()) {
                        JUnit5Utilization jUnit5Utilization = new JUnit5Utilization(args[1] + '\\' + file.getName());
                        jUnit5Utilization.findJUnit5Imports();
                    }
                }
            } else {
                JUnit5Utilization jUnit5Utilization = new JUnit5Utilization(args[0]);
                jUnit5Utilization.findJUnit5Imports();
            }  
        } else {
            throw new IllegalArgumentException("You must provide a correct file path with a java project!");
        }
    }
}

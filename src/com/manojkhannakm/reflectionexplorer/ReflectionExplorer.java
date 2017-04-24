package com.manojkhannakm.reflectionexplorer;

import com.manojkhannakm.reflectionexplorer.loader.ClassLoaderInterface;
import com.manojkhannakm.reflectionexplorer.swing.Frame;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author Manoj Khanna
 */

public class ReflectionExplorer {

    private Frame frame;

    public ReflectionExplorer() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame = new Frame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReflectionExplorer reflectionExplorer = new ReflectionExplorer();
            if (args.length >= 1) {
                String path = args[0];
                if (path.endsWith("\\") || path.endsWith("/")) {
                    if (args.length >= 2) {
                        String[] classArgs = new String[0];
                        if (args.length >= 3) {
                            classArgs = Arrays.copyOfRange(args, 2, args.length);
                        }

                        reflectionExplorer.openClass(path, args[1], classArgs);
                    }
                } else if (path.endsWith(".jar")) {
                    String[] jarArgs = new String[0];
                    if (args.length >= 2) {
                        jarArgs = Arrays.copyOfRange(args, 1, args.length);
                    }

                    reflectionExplorer.openJar(path, jarArgs);
                }
            }
        });
    }

    public void openClass(String classPath, String className, String... args) {
        frame.openClass(classPath, className, args);
    }

    public void openJar(String jarPath, String... args) {
        frame.openJar(jarPath, args);
    }

    public void openClassLoader(ClassLoaderInterface classLoader) {
        frame.openClassLoader(classLoader);
    }

}

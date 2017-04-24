package com.manojkhannakm.reflectionexplorer.swing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Manoj Khanna
 */

public class MenuBar extends JMenuBar {

    public MenuBar(Frame frame) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        add(fileMenu);

        JFileChooser fileChooser = new JFileChooser("E:\\Manoj\\Projects\\Java\\ReflectionExplorer\\out\\production\\");    //TODO: Remove path later
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Java class path or jar file", "jar"));

        JMenuItem fileOpenMenuItem = new JMenuItem("Open");
        fileOpenMenuItem.setMnemonic('O');
        fileOpenMenuItem.addActionListener(e -> {
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file.isDirectory()) {
                    String className = null;
                    try (URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()})) {
                        Object[] classNames = classNames(file, classLoader).toArray();
                        if (classNames.length > 0) {
                            className = (String) JOptionPane.showInputDialog(frame, "Select a main class.",
                                    Frame.TITLE, JOptionPane.QUESTION_MESSAGE, null, classNames, classNames[0]);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    String[] args = new String[0];
                    if (className != null) {
                        String argsString = JOptionPane.showInputDialog(frame, "Type the command line arguments.",
                                Frame.TITLE, JOptionPane.QUESTION_MESSAGE);
                        if (argsString != null && !argsString.isEmpty()) {
                            args = argsString.split(" ");
                        }
                    }

                    frame.openClass(file.getPath(), className, args);
                } else {
                    String className = null;
                    try (JarFile jarFile = new JarFile(file)) {
                        Manifest manifest = jarFile.getManifest();
                        if (manifest != null) {
                            className = manifest.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    String[] args = new String[0];
                    if (className != null) {
                        String argsString = JOptionPane.showInputDialog(frame, "Type the command line arguments.",
                                Frame.TITLE, JOptionPane.QUESTION_MESSAGE);
                        if (argsString != null && !argsString.isEmpty()) {
                            args = argsString.split(" ");
                        }
                    }

                    frame.openJar(file.getPath(), args);
                }
            }
        });
        fileMenu.add(fileOpenMenuItem);

        JMenuItem fileExitMenuItem = new JMenuItem("Exit");
        fileExitMenuItem.setMnemonic('X');
        fileExitMenuItem.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        fileMenu.add(fileExitMenuItem);

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');
        add(aboutMenu);
    }

    private ArrayList<String> classNames(File file, URLClassLoader classLoader) {
        ArrayList<String> classNameList = new ArrayList<>();
        if (file.isDirectory()) {
            for (File subFile : file.listFiles(pathname -> pathname.isDirectory() || pathname.getName().endsWith(".class"))) {
                classNameList.addAll(classNames(subFile, classLoader));
            }
        } else {
            String className = file.getPath();
            className = className.substring(classLoader.getURLs()[0].getPath().length() - 1, className.length() - 6)
                    .replace('\\', '.')
                    .replace('/', '.');

            try {
                int modifiers = classLoader.loadClass(className).getMethod("main", String[].class).getModifiers();
                if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                    classNameList.add(className);
                }
            } catch (ClassNotFoundException | NoClassDefFoundError | NoSuchMethodException ignored) {
            }
        }

        return classNameList;
    }

}

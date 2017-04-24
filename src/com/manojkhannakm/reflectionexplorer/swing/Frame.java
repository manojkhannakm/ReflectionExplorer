package com.manojkhannakm.reflectionexplorer.swing;

import com.manojkhannakm.reflectionexplorer.loader.URLClassLoader;
import com.manojkhannakm.reflectionexplorer.swing.tab.ExploreTab;
import com.manojkhannakm.reflectionexplorer.swing.tab.SearchTab;
import com.manojkhannakm.reflectionexplorer.loader.ClassLoaderInterface;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Manoj Khanna
 */

public class Frame extends JFrame {

    public static final String TITLE = "Reflection Explorer";

    private ExploreTab exploreTab;
    private SearchTab searchTab;
    private ClassLoaderInterface classLoader;

    public Frame() throws HeadlessException {
        super(TITLE);

        setLayout(new BorderLayout());
        setJMenuBar(new com.manojkhannakm.reflectionexplorer.swing.MenuBar(this));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        exploreTab = new ExploreTab(this);
        tabbedPane.addTab("Explore", exploreTab);

        searchTab = new SearchTab(this);
        tabbedPane.addTab("Search", searchTab);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void open(File file, String className, String... args) {
        try {
            classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            classLoader.setCallback(exploreTab);

            Method method = ((URLClassLoader) classLoader).loadClass(className).getMethod("main", String[].class);
            int modifiers = method.getModifiers();
            if (!Modifier.isPublic(modifiers) || !Modifier.isStatic(modifiers)) {
                JOptionPane.showMessageDialog(this, "Main class not found!",
                        TITLE, JOptionPane.ERROR_MESSAGE);
                return;
            }

            setTitle(TITLE + " - " + className);

            exploreTab.fileOpened();
            searchTab.fileOpened();

            new Thread(() -> {
                try {
                    method.invoke(null, (Object) args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException e) {
            JOptionPane.showMessageDialog(this, "Main class not found!",
                    TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void close() {
        setTitle(TITLE);

        exploreTab.fileClosed();
        searchTab.fileClosed();

        if (classLoader != null) {
            if (classLoader instanceof URLClassLoader) {
                try {
                    ((URLClassLoader) classLoader).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            classLoader = null;
        }
    }

    public void openClass(String classPath, String className, String... args) {
        close();

        if (classPath == null) {
            classPath = "";
        }

        File file = new File(classPath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Class path not found!",
                    TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (className == null) {
            className = "";
        }

        open(file, className, args);
    }

    public void openJar(String jarPath, String... args) {
        close();

        if (jarPath == null) {
            jarPath = "";
        }

        File file = new File(jarPath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Jar file not found!",
                    TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        String className = null;
        try (JarFile jarFile = new JarFile(file)) {
            Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                className = manifest.getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (className == null) {
            className = "";
        }

        open(file, className, args);
    }

    public void openClassLoader(ClassLoaderInterface classLoader) {
        close();

        this.classLoader = classLoader;

        classLoader.setCallback(exploreTab);
    }

    public ExploreTab getExploreTab() {
        return exploreTab;
    }

    public SearchTab getSearchTab() {
        return searchTab;
    }

    public interface Callback {

        void fileOpened();

        void fileClosed();

    }

}

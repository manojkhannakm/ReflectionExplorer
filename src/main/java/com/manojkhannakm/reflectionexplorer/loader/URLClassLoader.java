package com.manojkhannakm.reflectionexplorer.loader;

import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class URLClassLoader extends java.net.URLClassLoader implements ClassLoaderInterface {

    private Callback callback;
    private ArrayList<Class<?>> classList = new ArrayList<>();

    public URLClassLoader(URL[] urls) {
        super(urls);
    }

    public URLClassLoader(URL[] urls, java.lang.ClassLoader parent) {
        super(urls, parent);
    }

    public URLClassLoader(URL[] urls, java.lang.ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = super.loadClass(name, resolve);
        classList.add(clazz);

        callback.classLoaded(clazz);

        return clazz;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public ArrayList<Class<?>> getClassList() {
        return classList;
    }

}

package com.manojkhannakm.reflectionexplorer.loader;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class ClassLoader extends java.lang.ClassLoader implements ClassLoaderInterface {

    private Callback callback;
    private ArrayList<Class<?>> classList = new ArrayList<>();

    public ClassLoader() {
        super();
    }

    public ClassLoader(java.lang.ClassLoader parent) {
        super(parent);
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

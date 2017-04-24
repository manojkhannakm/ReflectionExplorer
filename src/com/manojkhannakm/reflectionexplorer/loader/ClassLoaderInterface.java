package com.manojkhannakm.reflectionexplorer.loader;

import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public interface ClassLoaderInterface {

    void setCallback(Callback callback);

    ArrayList<Class<?>> getClassList();

    interface Callback {

        void classLoaded(Class<?> clazz);

    }

}

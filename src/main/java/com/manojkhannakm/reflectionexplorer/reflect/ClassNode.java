package com.manojkhannakm.reflectionexplorer.reflect;

/**
 * @author Manoj Khanna
 */

public class ClassNode extends Node {

    private Class<?> clazz;

    public ClassNode(Class<?> clazz) {
        super(clazz.getSimpleName());

        this.clazz = clazz;
    }

    @Override
    public int compareTo(Node o) {
        if (!(o instanceof ClassNode)) {
            return 1;
        }

        int i = Boolean.compare(clazz.isInterface(), ((ClassNode) o).clazz.isInterface());
        if (i == 0) {
            i = name.compareTo(o.name);
        }

        return i;
    }

    public Class<?> getClazz() {
        return clazz;
    }

}

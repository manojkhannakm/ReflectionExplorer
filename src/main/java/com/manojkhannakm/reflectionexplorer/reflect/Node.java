package com.manojkhannakm.reflectionexplorer.reflect;

/**
 * @author Manoj Khanna
 */

public abstract class Node implements Comparable<Node> {

    protected String name;

    protected Node(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node && ((Node) obj).name.equals(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

}

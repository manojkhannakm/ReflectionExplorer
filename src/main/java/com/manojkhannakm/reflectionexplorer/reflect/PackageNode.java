package com.manojkhannakm.reflectionexplorer.reflect;

/**
 * @author Manoj Khanna
 */

public class PackageNode extends Node {

    public PackageNode(String name) {
        super(name);
    }

    @Override
    public int compareTo(Node o) {
        return name.compareTo(o.name);
    }

}

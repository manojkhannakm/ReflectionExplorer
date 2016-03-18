package com.manojkhannakm.reflectionexplorer.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author Manoj Khanna
 */

public class MethodNode extends Node {

    private Method method;

    public MethodNode(Method method) {
        super(method.getName()
                + ": (" + Arrays.toString(method.getGenericParameterTypes())
                .substring(1, Arrays.toString(method.getGenericParameterTypes()).length() - 1)
                .replace("class ", "") + ")"
                + method.getGenericReturnType().getTypeName());

        this.method = method;
    }

    @Override
    public int compareTo(Node o) {
        if (o instanceof FieldNode) {
            return 1;
        } else if (o instanceof ClassNode) {
            return -1;
        }

        int modifiers1 = method.getModifiers(),
                modifiers2 = ((MethodNode) o).method.getModifiers(),
                i = Boolean.compare(Modifier.isStatic(modifiers2) && Modifier.isFinal(modifiers2),
                        Modifier.isStatic(modifiers1) && Modifier.isFinal(modifiers1));
        if (i == 0) {
            i = Boolean.compare(Modifier.isStatic(modifiers2), Modifier.isStatic(modifiers1));
            if (i == 0) {
                i = Boolean.compare(Modifier.isFinal(modifiers2), Modifier.isFinal(modifiers1));
                if (i == 0) {
                    i = Boolean.compare(Modifier.isPublic(modifiers2), Modifier.isPublic(modifiers1));
                    if (i == 0) {
                        i = Boolean.compare(Modifier.isProtected(modifiers2), Modifier.isProtected(modifiers1));
                        if (i == 0) {
                            i = Boolean.compare(Modifier.isPrivate(modifiers2), Modifier.isPrivate(modifiers1));
                            if (i == 0) {
                                i = method.getName().compareTo(((MethodNode) o).method.getName());
                                if (i == 0) {
                                    i = Integer.compare(method.getParameterCount(), ((MethodNode) o).method.getParameterCount());
                                }
                            }
                        }
                    }
                }
            }
        }

        return i;
    }

    public Method getMethod() {
        return method;
    }

}

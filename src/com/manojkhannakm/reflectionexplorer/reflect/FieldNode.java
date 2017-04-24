package com.manojkhannakm.reflectionexplorer.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Manoj Khanna
 */

public class FieldNode extends Node {

    private Field field;

    public FieldNode(Field field) {
        super(field.getName() + ": " + field.getGenericType().getTypeName());

        this.field = field;
    }

    @Override
    public int compareTo(Node o) {
        if (!(o instanceof FieldNode)) {
            return -1;
        }

        int modifiers1 = field.getModifiers(),
                modifiers2 = ((FieldNode) o).field.getModifiers(),
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
                                i = field.getName().compareTo(((FieldNode) o).field.getName());
                            }
                        }
                    }
                }
            }
        }

        return i;
    }

    public Field getField() {
        return field;
    }

}

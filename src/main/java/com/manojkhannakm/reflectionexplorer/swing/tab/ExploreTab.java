package com.manojkhannakm.reflectionexplorer.swing.tab;

import com.manojkhannakm.reflectionexplorer.loader.ClassLoaderInterface;
import com.manojkhannakm.reflectionexplorer.reflect.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class ExploreTab extends JPanel implements com.manojkhannakm.reflectionexplorer.swing.Frame.Callback, ClassLoaderInterface.Callback {

    private com.manojkhannakm.reflectionexplorer.swing.Frame frame;
    private DefaultTreeModel treeModel;

    public ExploreTab(com.manojkhannakm.reflectionexplorer.swing.Frame frame) {
        super(new BorderLayout());

        this.frame = frame;

        JPanel navigationPanel = new JPanel();
        add(navigationPanel, BorderLayout.NORTH);

        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("") {

            @Override
            public boolean isLeaf() {
                return false;
            }

        });

        JTree tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setPreferredSize(new Dimension(250, 500));

        JPanel propertyPanel = new JPanel();

        JScrollPane propertyScrollPane = new JScrollPane(propertyPanel);
        propertyScrollPane.setPreferredSize(new Dimension(250, 500));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, propertyScrollPane);
        splitPane.setDividerLocation(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void fileOpened() {

    }

    @Override
    public void fileClosed() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel.getRoot();
        while (rootNode.getChildCount() > 0) {
            treeModel.removeNodeFromParent((MutableTreeNode) rootNode.getLastChild());
        }
    }

    @Override
    public void classLoaded(Class<?> clazz) {
        SwingUtilities.invokeLater(() -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getRoot();

            Package packege = clazz.getPackage();
            if (packege != null) {
                for (String packageName : packege.getName().split("\\.")) {
                    node = insertNode(node, new DefaultMutableTreeNode(new PackageNode(packageName)));
                }
            }

            ArrayList<Class<?>> declaringClassList = new ArrayList<>();
            Class<?> declaringClass = clazz.getDeclaringClass();
            while (declaringClass != null) {
                declaringClassList.add(declaringClass);
                declaringClass = declaringClass.getDeclaringClass();
            }

            for (int i = declaringClassList.size() - 1; i >= 0; i--) {
                node = insertNode(node, new DefaultMutableTreeNode(new ClassNode(declaringClassList.get(i))));
            }

            node = insertNode(node, new DefaultMutableTreeNode(new ClassNode(clazz)));

            for (Field field : clazz.getDeclaredFields()) {
                insertNode(node, new DefaultMutableTreeNode(new FieldNode(field)));
            }

            for (Method method : clazz.getDeclaredMethods()) {
                insertNode(node, new DefaultMutableTreeNode(new MethodNode(method)));
            }
        });
    }

    private DefaultMutableTreeNode insertNode(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode childNode) {
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            if (node.getUserObject().equals(childNode.getUserObject())) {
                return node;
            }
        }

        int index = parentNode.getChildCount();
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            if (((Node) node.getUserObject()).compareTo((Node) childNode.getUserObject()) > 0) {
                index = i;
                break;
            }
        }

        treeModel.insertNodeInto(childNode, parentNode, index);

        return childNode;
    }

}

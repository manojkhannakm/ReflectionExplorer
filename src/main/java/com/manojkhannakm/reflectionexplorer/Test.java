package com.manojkhannakm.reflectionexplorer;

import javax.swing.*;

/**
 * @author Manoj Khanna
 */

public class Test {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReflectionExplorer().openJar("E:\\Manoj\\Projects\\Java\\StocksManager\\out\\artifacts\\StocksManager_jar\\StocksManager.jar"));
    }

}

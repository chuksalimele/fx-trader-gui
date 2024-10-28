package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.trader.expert.ExpertUtil;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import javax.swing.tree.TreeCellRenderer;

/**
 * FileTreeModel to display only directories and .class files.
 */
public class FileTreeModel implements TreeModel {

    private File root;

    public FileTreeModel(File root) {
        this.root = root;
    }

    public FileTreeModel(String filename) {
        this.root = new File(filename);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        File directory = (File) parent;
        File[] children = getFilteredFiles(directory);
        if (children != null && index >= 0 && index < children.length) {
            return children[index];
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        File file = (File) parent;
        File[] fileList = getFilteredFiles(file);
        return (fileList != null) ? fileList.length : 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        File file = (File) node;
        return file.isFile();  // Leaf if it's a file, not a directory
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        File directory = (File) parent;
        File childFile = (File) child;
        File[] children = getFilteredFiles(directory);
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (childFile.getName().equals(children[i].getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // This method can be used if you want to allow renaming of folders/files
        // For this example, it is not implemented
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        // No listeners in this simple example
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        // No listeners in this simple example
    }

    /**
     * Helper method to filter files and return only .class files and
     * directories.
     */
    private File[] getFilteredFiles(File directory) {
        if (directory.isDirectory()) {
            return directory.listFiles(file -> {
                if (file.isFile() && ExpertUtil.isExpert(file)) {
                    return true;
                } else {
                    return hasNesteFileOfType(file, ".class");
                }
            });
        }
        return null;
    }

    private boolean hasNesteFileOfType(File directory, String type) {

       var list =  directory.listFiles(file -> {
            if (file.isFile() && ExpertUtil.isExpert(file)) {
                return true;
            } else {
                return hasNesteFileOfType(file, type);
            }
        });
       
       if(list == null){
           return false;
       }
       
       return list.length > 0;
    }    
    
    public static void createAndShowGUI() {
        // Choose a root directory to display in the JTree
        File rootDir = new File("C:/");  // Change this to any directory you want to display

        // Create the FileTreeModel
        FileTreeModel model = new FileTreeModel(rootDir);

        // Create a JTree with the custom model
        JTree tree = new JTree(model);

        // Put the JTree in a scroll pane
        JScrollPane scrollPane = new JScrollPane(tree);

        // Create a JFrame to display the tree
        JFrame frame = new JFrame("Folder and .class Files Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane);
        frame.setSize(400, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

}

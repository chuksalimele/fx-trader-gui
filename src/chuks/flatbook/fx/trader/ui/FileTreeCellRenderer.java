/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import chuks.flatbook.fx.trader.expert.ExpertUtil;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author user
 */
public class FileTreeCellRenderer extends JLabel implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        File file = (File) value;
        // Display only the file name (or last part of the path)
        if (file.isFile()) {
            setText(ExpertUtil.expertSimpleName(file.getName()));
        } else {
            setText(file.getName());
        }
        
        if (selected) {
            setForeground(Color.WHITE);
        } else {
            setForeground(Color.BLACK);
        }
        return this;
    }
}

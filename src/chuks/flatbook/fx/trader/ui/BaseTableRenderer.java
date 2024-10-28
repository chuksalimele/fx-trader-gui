/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Set;
import java.util.EnumSet;
import java.util.HashSet;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author user
 */
public class BaseTableRenderer extends DefaultTableCellRenderer {
    protected final Color _RED = new Color(150, 0, 0);
    protected Color _GREEN = new Color(0, 150, 0);

    public BaseTableRenderer() {
        setHorizontalAlignment(JLabel.RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if(isSelected){
            c.setForeground(Color.WHITE);
        }else{
            c.setForeground(Color.BLACK);
        }
               
        return c;
    }
}

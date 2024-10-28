/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class ExitButtonEditor extends DefaultCellEditor {

    private final JLabel label;
    private final Color OUT_COLOR = Color.LIGHT_GRAY;
    private final Color IN_COLOR = Color.LIGHT_GRAY;

    public ExitButtonEditor(JCheckBox checkBox) {
        super(checkBox);

        label = new JLabel("X");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(OUT_COLOR);
        //label.setBackground(label.getParent().getBackground());
        label.setOpaque(true);
              
        // Add MouseAdapter for mouse enter and leave events
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(OUT_COLOR); 
                label.setBackground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(OUT_COLOR); 
                label.setBackground(label.getParent().getBackground());
            }
        });

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return label;
    }

    @Override
    public Object getCellEditorValue() {
        return "X";
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

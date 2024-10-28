/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;

/**
 *
 * @author user
 */
public class AmountRenderer extends BaseTableRenderer {

    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    boolean altColor;
    public AmountRenderer(boolean alt_color) {
        altColor = alt_color;
    }
    public AmountRenderer() {
        this(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        
        if(isSelected){
            return c;
        }
        
        if (altColor && value instanceof Double dValue) {

            if (dValue > 0) {
                c.setForeground(_GREEN);
            } else if (dValue < 0) {
                c.setForeground(_RED);
            }
        }
        
        return c;
    }

    
    
    @Override
    protected void setValue(Object value) {
        double amt = (double)value;
        String strAmt = decimalFormat.format(amt);
        super.setValue(strAmt);
    }

}

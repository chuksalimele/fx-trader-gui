/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import chuks.flatbook.fx.common.account.order.Order;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author user
 */
public class SideRenderer extends BaseTableRenderer {

    public SideRenderer() {
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        if(isSelected){
            return c;
        }
        
        
        if (value instanceof Character ch) {
            if (ch == Order.Side.BUY
                    || ch == Order.Side.BUY_LIMIT
                    || ch == Order.Side.BUY_STOP) { 
                c.setForeground(_GREEN);
            } else if (ch == Order.Side.SELL
                    || ch == Order.Side.SELL_LIMIT
                    || ch == Order.Side.SELL_STOP) {
                c.setForeground(_RED);
            }
        }

        return c;
    }    
    @Override
    protected void setValue(Object value) {
        char side = (char) value;
        String strSide = side == Order.Side.BUY ? "BUY"
                : side == Order.Side.SELL ? "SELL"
                : side == Order.Side.BUY_LIMIT ? "BUY LIMIT"
                : side == Order.Side.SELL_LIMIT ? "SELL LIMIT"
                : side == Order.Side.BUY_STOP ? "BUY STOP"
                : side == Order.Side.SELL_STOP ? "SELL STOP" : "UNKNOWN";
        
        super.setValue(strSide);
    }

}

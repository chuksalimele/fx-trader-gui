/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import chuks.flatbook.fx.common.account.order.Order;
import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JTable;

/**
 *
 * @author user
 */
public class PriceRenderer extends BaseTableRenderer {

    int symbolDigits;
    DecimalFormat fiveDigitFormat = new DecimalFormat("0.00000");
    DecimalFormat threeDigitFormat = new DecimalFormat("0.000");
    private Double lastPriceValue;
    boolean altColor;

    public PriceRenderer() {
        this(true);
    }

    public PriceRenderer(boolean alt_color) {
        altColor = alt_color;
    }

    public void setSymbolDigits(int symbol_digits) {
        this.symbolDigits = symbol_digits;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            return c;
        }

        if (altColor && value instanceof Double dValue) {

            if (lastPriceValue != null) {
                if (dValue > lastPriceValue) {
                    c.setForeground(_GREEN);
                } else {
                    c.setForeground(_RED);
                }
            }
            lastPriceValue = dValue;
        }

        return c;
    }

    @Override
    protected void setValue(Object value) {
        double price = (double) value;
        String strPrice = symbolDigits == 5
                ? fiveDigitFormat.format(price)
                : threeDigitFormat.format(price);
        super.setValue(strPrice);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

/**
 *
 * @author user
 */
public class IdRenderer extends BaseTableRenderer {

    @Override
    protected void setValue(Object value) {
        String strValue = "#" + value.toString();
        super.setValue(strValue);
    }

}

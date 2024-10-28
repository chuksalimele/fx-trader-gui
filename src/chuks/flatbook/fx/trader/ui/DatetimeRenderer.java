/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author user
 */
public class DatetimeRenderer extends BaseTableRenderer{
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void setValue(Object value) {
        if(value == null){
            super.setValue(value); 
            return;
        }
        Date date = (Date)value;        
        super.setValue(dateFormat.format(date)); 
    }
    
    
}

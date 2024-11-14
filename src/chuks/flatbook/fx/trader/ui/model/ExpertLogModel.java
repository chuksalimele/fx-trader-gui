/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.trader.main.Timeframe;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author user
 */
public class ExpertLogModel  extends AbstractTableModel {

    public final String time = "Time";
    protected final String message = "Message";

    protected String[] columnNames = {time, message};
    protected final LinkedList<Date> timeList = new LinkedList<>();
    protected final LinkedList<String> messageList = new LinkedList<>();

    public void addLog(Date time, String message) {
        timeList.add(time);
        messageList.add(message);
        fireTableRowsInserted(messageList.size() - 1, messageList.size() - 1);
    }


    public String removeLogAt(int index) {
        String msg = messageList.remove(index);
        timeList.remove(index);
        messageList.remove(index);
        fireTableRowsInserted(messageList.size() - 1, messageList.size() - 1);
        return msg;
    }

    public void removeAll() {
        messageList.clear();
        timeList.clear();
        messageList.clear();
        fireTableRowsInserted(0, messageList.size() - 1);
    }

    public String getLogAt(int row) {
        return messageList.get(row);
    }
    
    public int indexOfColumn(String col) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(col)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getRowCount() {
        return messageList.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Date _time = timeList.get(rowIndex);
        String msg = messageList.get(rowIndex);

        
        return switch (columnIndex) {
            case 0 ->
                _time.toString();
            case 1 ->                
                msg;           
            default ->
                null;
        };
    }
}

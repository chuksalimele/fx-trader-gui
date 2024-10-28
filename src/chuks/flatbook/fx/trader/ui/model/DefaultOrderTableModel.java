/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.listener.OrderActionListener;
import chuks.flatbook.fx.trader.ui.PriceRenderer;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author user
 */
 public class DefaultOrderTableModel  extends AbstractTableModel implements OrderActionListener{
     public final String ID = "ID";
     protected final String Time = "Time";
     protected final String Side = "Side";
     protected final String Lot = "Lot";
     protected final String Symbol = "Symbol";
     protected final String Entry = "Entry";
     protected final String Exit = "Exit";
     protected final String Time_Closed = "Time Closed";
     protected final String Commission = "Commission";
     protected final String Price = "Price";     
     protected final String S_L = "S/L";
     protected final String T_P = "T/P";
     protected final String Swap = "Swap";
     protected final String Profit = "Profit";
     protected final String EXIT = "";
          
     
    protected String[] columnNames = {"Order ID", "Symbol", "Side", "Price", "Lot Size", "Target Price", "Stoploss Price"};
    protected final LinkedList<Order> orderList = new LinkedList<>();

    public void addOrder(Order order) {
        orderList.add(order);
        fireTableRowsInserted(orderList.size() - 1, orderList.size() - 1);
    }

    public int indexOfColumn(String col){
        for(int i=0; i<columnNames.length; i++){
            if(columnNames[i].equals(col)){
                return i;
            }
        }
        return -1;
    }
    
    public void setCellRender(JTable table) {
    }
    
    public void removeOrder(Order order) {
        int index = orderList.indexOf(order);
        if(index < 0){
            String errMsg = "Order ["+order.getOrderID()+"] not found. This should not happen.";
            System.out.println(errMsg);
            JOptionPane.showMessageDialog(null, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        orderList.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void updateOrder(int rowIndex, Order order){    
    }
    
    @Override
    public int getRowCount() {
        return orderList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Order getOrderAt(int rowIndex) {
        return orderList.get(rowIndex);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    @Override
    public void onNewMarketOrder(Order order) {
    }

    @Override
    public void onClosedMarketOrder(Order order) {
    }

    @Override
    public void onModifiedMarketOrder(Order order) {
    }

    @Override
    public void onNewPendingOrder(Order order) {
    }

    @Override
    public void onDeletedPendingOrder(Order order) {
    }

    @Override
    public void onModifiedPendingOrder(Order order) {
    }

    @Override
    public void onTriggeredPendingOrder(Order order) {
    }

    @Override
    public void onOrderSendFailed(Order order, String errMsg) {
    }

    @Override
    public void onOrderRemoteError(Order order, String errMsg) {
    }

    @Override
    public void onOrderNotAvailable(String req_identifier, String errMsg) {
    }
    
    @Override
    public void onAddAllOpenOrders(List<Order> order) {
    }

    @Override
    public void onAddAllPendingOrders(List<Order> order) {
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> order) {
    }
}


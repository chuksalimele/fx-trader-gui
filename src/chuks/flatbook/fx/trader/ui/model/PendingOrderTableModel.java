/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.trader.ui.AmountRenderer;
import chuks.flatbook.fx.trader.ui.BaseTableRenderer;
import chuks.flatbook.fx.trader.ui.CellRenderOption;
import chuks.flatbook.fx.trader.ui.DatetimeRenderer;
import chuks.flatbook.fx.trader.ui.ExitButtonEditor;
import chuks.flatbook.fx.trader.ui.ExitButtonRenderer;
import chuks.flatbook.fx.trader.ui.IdRenderer;
import chuks.flatbook.fx.trader.ui.PriceRenderer;
import chuks.flatbook.fx.trader.ui.SideRenderer;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 *
 * @author user
 */
public class PendingOrderTableModel extends DefaultOrderTableModel {

    private JTable table;

    public PendingOrderTableModel() {
        this.columnNames = new String[]{
            ID,
            Time,
            Side,
            Lot,
            Symbol,
            Entry,
            S_L,
            T_P,
            EXIT};

    }

    @Override
    public void setCellRender(JTable table) {
        this.table = table;
        BaseTableRenderer base_renderer = new BaseTableRenderer();
        JTableHeader tableHeader = table.getTableHeader();

        for (int i = 0; i < columnNames.length; i++) {

            if (i == 0) {
                table.getColumn(columnNames[i]).setCellRenderer(new IdRenderer());
                continue;
            }

            if (!columnNames[i].equals(EXIT)) {
                tableHeader.getColumnModel().getColumn(i).setHeaderRenderer(base_renderer);
            }

            if (columnNames[i].equals(Time)) {
                table.getColumn(columnNames[i]).setCellRenderer(new DatetimeRenderer());
            } else if (columnNames[i].equals(Side)) {
                table.getColumn(columnNames[i]).setCellRenderer(new SideRenderer());
            } else if (columnNames[i].equals(Entry)
                    || columnNames[i].equals(S_L)
                    || columnNames[i].equals(T_P)) {
                table.getColumn(columnNames[i]).setCellRenderer(new PriceRenderer(false));
            } else if (columnNames[i].equals(Lot)) {
                table.getColumn(columnNames[i]).setCellRenderer(new AmountRenderer(false));
            } else if (columnNames[i].equals(EXIT)) {
                table.getColumn(columnNames[i]).setCellRenderer(new ExitButtonRenderer());
                table.getColumn(columnNames[i]).setCellEditor(new ExitButtonEditor(new JCheckBox()));
                table.getColumn(columnNames[i]).setMaxWidth(25);
            } else {
                table.getColumn(columnNames[i]).setCellRenderer(new BaseTableRenderer());
            }
        }
    }

    public boolean isEixtColumn(int col_index) {
        return indexOfColumn(EXIT) == col_index;
    }

    @Override
    public void updateOrder(int rowIndex, Order order) {
        orderList.set(rowIndex, order);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = orderList.get(rowIndex);
        if (table.getCellRenderer(rowIndex, columnIndex) instanceof PriceRenderer priceRenderer) {
            priceRenderer.setSymbolDigits(order.getSymbolDigits());
        }

        return switch (columnIndex) {
            case 0 ->
                order.getOrderID();
            case 1 ->
                order.getOpenTime();//will mean pending order placement time
            case 2 ->
                order.getSide();
            case 3 ->
                order.getLotSize();
            case 4 ->
                order.getSymbol();
            case 5 ->
                order.getOpenPrice();
            case 6 ->
                order.getStoplossPrice();
            case 7 ->
                order.getTakeProfitPrice();
            default ->
                null;
        };
    }

    @Override
    public void onAddAllPendingOrders(List<Order> order) {
        if (order.isEmpty()) {
            return;
        }

        fireTableRowsDeleted(0, order.size() - 1);
        this.orderList.clear();
        this.orderList.addAll(order);
        fireTableRowsInserted(0, order.size() - 1);
    }

    @Override
    public void onNewPendingOrder(Order order) {
        this.addOrder(order);
    }

    @Override
    public void onModifiedPendingOrder(Order order) {
        int index = this.orderList.indexOf(order);
        if (index != -1) {
            this.updateOrder(index, order);
        } else {
            System.err.println("WARNING!!! Modified order [" + order.getOrderID() + "] not found!");
        }
    }

    @Override
    public void onDeletedPendingOrder(Order order) {
        this.removeOrder(order);
    }

    @Override
    public void onTriggeredPendingOrder(Order order) {
        this.removeOrder(order);
    }

}

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
import chuks.flatbook.fx.trader.ui.IdRenderer;
import chuks.flatbook.fx.trader.ui.PriceRenderer;
import chuks.flatbook.fx.trader.ui.SideRenderer;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 *
 * @author user
 */
public class HistoryOrderTableModel extends DefaultOrderTableModel {

    JTable table;

    public HistoryOrderTableModel() {
        this.columnNames = new String[]{
            ID,
            Time,
            Side,
            Lot,
            Symbol,
            Entry,
            Exit,
            Time_Closed,
            S_L,
            T_P,
            Commission,
            Swap,
            Profit};
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

            tableHeader.getColumnModel().getColumn(i).setHeaderRenderer(base_renderer);

            if (columnNames[i].equals(Time)
                    || columnNames[i].equals(Time_Closed)) {
                table.getColumn(columnNames[i]).setCellRenderer(new DatetimeRenderer());
            } else if (columnNames[i].equals(Side)) {
                table.getColumn(columnNames[i]).setCellRenderer(new SideRenderer());
            } else if (columnNames[i].equals(Entry)
                    || columnNames[i].equals(Exit)
                    || columnNames[i].equals(S_L)
                    || columnNames[i].equals(T_P)) {
                table.getColumn(columnNames[i]).setCellRenderer(new PriceRenderer(false));
            } else if (columnNames[i].equals(Lot)) {
                table.getColumn(columnNames[i]).setCellRenderer(new AmountRenderer(false));
            } else if (columnNames[i].equals(Commission)
                    || columnNames[i].equals(Swap)
                    || columnNames[i].equals(Profit)) {
                table.getColumn(columnNames[i]).setCellRenderer(new AmountRenderer());
            } else {
                table.getColumn(columnNames[i]).setCellRenderer(new BaseTableRenderer());
            }
        }

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
                order.getOpenTime();
            case 2 ->
                order.getSide();
            case 3 ->
                order.getLotSize();
            case 4 ->
                order.getSymbol();
            case 5 ->
                order.getOpenPrice();
            case 6 ->
                order.getClosePrice();
            case 7 ->
                order.getCloseTime();
            case 8 ->
                order.getStoplossPrice();
            case 9 ->
                order.getTakeProfitPrice();
            case 10 ->
                order.getCommission();
            case 11 ->
                order.getSwap();
            case 12 ->
                order.getProfit();
            default ->
                null;
        };
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> order) {
        if (order.isEmpty()) {
            return;
        }

        fireTableRowsDeleted(0, order.size() - 1);
        this.orderList.clear();
        this.orderList.addAll(order);
        fireTableRowsInserted(0, order.size() - 1);
    }

    @Override
    public void onClosedMarketOrder(Order order) {
        this.addOrder(order);
    }

    @Override
    public void onDeletedPendingOrder(Order order) {
        this.addOrder(order);
    }
}

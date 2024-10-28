/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.common.listener.SymbolUpdateListener;
import chuks.flatbook.fx.trader.ui.BaseTableRenderer;
import chuks.flatbook.fx.trader.ui.PriceRenderer;
import chuks.flatbook.fx.trader.ui.SpreadRenderer;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author user
 */
public class MarketWatchTableModel extends AbstractTableModel implements SymbolUpdateListener {

    protected final String Bid = "Bid";
    protected final String Ask = "Ask";
    protected final String Spread = "Spread";
    protected final String Symbol = "Symbol";
    private final String[] columnNames = {Symbol, Bid, Ask, Spread};
    protected final LinkedList<SymbolInfo> symbolInfoList = new LinkedList<>();
    private JTable table;
    
    protected int indexOfColumn(String col) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(col)) {
                return i;
            }
        }
        return -1;
    }

    public void setCellRender(JTable table) {
        this.table = table;
        BaseTableRenderer base_renderer = new BaseTableRenderer();
        JTableHeader tableHeader = table.getTableHeader();

        for (int i = 0; i < columnNames.length; i++) {

            if (i == 0) {
                continue;
            }

            tableHeader.getColumnModel().getColumn(i).setHeaderRenderer(base_renderer);

            if (columnNames[i].equals(Bid)
                    || columnNames[i].equals(Ask)) {
                table.getColumn(columnNames[i]).setCellRenderer(new PriceRenderer());
            } else if (columnNames[i].equals(Spread)){
                table.getColumn(columnNames[i]).setCellRenderer(new SpreadRenderer());
            }
        }

    }

    public void addSymbolInfo(SymbolInfo symbolInfo) {
        symbolInfoList.add(symbolInfo);
        fireTableRowsInserted(symbolInfoList.size() - 1, symbolInfoList.size() - 1);
    }

    public void removeSymbolInfo(SymbolInfo symbolInfo) {
        int index = symbolInfoList.indexOf(symbolInfo);
        if (index < 0) {
            String errMsg = "Symbol [" + symbolInfo.getName() + "] not found. This should not happen.";
            System.out.println(errMsg);
            JOptionPane.showMessageDialog(null, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        symbolInfoList.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void updateSymbolInfo(int rowIndex, SymbolInfo symbolInfo) {
        symbolInfoList.set(rowIndex, symbolInfo);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return symbolInfoList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SymbolInfo symbolInfo = symbolInfoList.get(rowIndex);
        
        if (table.getCellRenderer(rowIndex, columnIndex) instanceof PriceRenderer priceRenderer) {
            priceRenderer.setSymbolDigits(symbolInfo.getDigits());
        }
        
        
        return switch (columnIndex) {
            case 0 ->
                symbolInfo.getName();
            case 1 ->
                symbolInfo.getBid();
            case 2 ->
                symbolInfo.getAsk();
            case 3 ->
                symbolInfo.getSpreadPipette(); // as it is in MT4
            default ->
                null;
        };
    }

    @Override
    public void onSwapChange(SymbolInfo symbolInfo) {
        int index = this.symbolInfoList.indexOf(symbolInfo);
        if (index != -1) {
            this.updateSymbolInfo(index, symbolInfo);
        } else {
            System.err.println("WARNING!!! Modified symbol [" + symbolInfo.getName() + "] not found!");
        }
    }

    @Override
    public void onPriceChange(SymbolInfo symbolInfo) {
        int index = this.symbolInfoList.indexOf(symbolInfo);
        if (index != -1) {
            this.updateSymbolInfo(index, symbolInfo);
        } else {
            System.err.println("WARNING!!! Modified symbol [" + symbolInfo.getName() + "] not found!");
        }
    }

    @Override
    public void onSymbolInfoAdded(SymbolInfo symbolInfo) {
        //avoid duplicate
        for (SymbolInfo symbInfo : symbolInfoList) {
            if (symbInfo.getName().equals(symbolInfo.getName())) {
                //symbolInfoList.remove(symbInfo);
                //break;
                return;
            }
        }
        symbolInfoList.add(symbolInfo);
        int index = symbolInfoList.lastIndexOf(symbolInfo);
        fireTableRowsInserted(index, index);
    }

    @Override
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo) {
        for (int i = 0; i < symbolInfoList.size(); i++) {
            if (symbolInfoList.get(i).getName().equals(symbolInfo.getName())) {
                symbolInfoList.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }

    }

    @Override
    public void onGetFullRefereshSymbol(String symbolName) {
    }

    @Override
    public void onSeletedSymbolList(List<String> symbol_list) {
    }

    @Override
    public void onSeletedSymbolInfoList(List<SymbolInfo> symbol_info_list) {
        if (symbol_info_list.isEmpty()) {
            return;
        }

        fireTableRowsDeleted(0, symbol_info_list.size() - 1);
        this.symbolInfoList.clear();
        this.symbolInfoList.addAll(symbol_info_list);
        fireTableRowsInserted(0, symbol_info_list.size() - 1);
    }

    @Override
    public void onfullSymbolList(List<String> symbol_list) {
        
    }
}

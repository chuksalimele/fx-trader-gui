package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.trader.expert.ExpertUtil;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 */
public class AttachededExpertModel extends AbstractTableModel {

    public final String symbol = "";
    protected final String eARuning = "EA Running";

    protected String[] columnNames = {symbol, eARuning};
    protected final LinkedList<File> eaList = new LinkedList<>();
    protected final LinkedList<String> symbolList = new LinkedList<>();

    public void addExpert(File expertFile, String symbol) {
        eaList.add(expertFile);
        symbolList.add(symbol);
        fireTableRowsInserted(eaList.size() - 1, eaList.size() - 1);
    }

    public void removeExpert(File expertFile) {
        int index = eaList.indexOf(expertFile);
        if (index == -1) {
            return;
        }
        removeExpertAt(index);
    }

    public File removeExpertAt(int index) {
        File expert = eaList.remove(index);
        symbolList.remove(index);
        fireTableRowsInserted(eaList.size() - 1, eaList.size() - 1);
        return expert;
    }

    public void removeAll() {
        eaList.clear();
        symbolList.clear();
        fireTableRowsInserted(0, eaList.size() - 1);
    }

    public File getExpertAt(int row) {
        return eaList.get(row);
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
        return eaList.size();
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
        File expert = eaList.get(rowIndex);
        String _symbol = symbolList.get(rowIndex);

        return switch (columnIndex) {
            case 0 ->
                _symbol;
            case 1 ->
                expert.getName();
            default ->
                null;
        };
    }
}

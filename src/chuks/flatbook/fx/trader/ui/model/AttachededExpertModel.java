package chuks.flatbook.fx.trader.ui.model;

import chuks.flatbook.fx.trader.main.Timeframe;
import java.io.File;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 */
public class AttachededExpertModel extends AbstractTableModel {

    public final String symbol = "";
    protected final String timeframe = "";
    protected final String eARuning = "EA Running";

    protected String[] columnNames = {symbol, timeframe, eARuning};
    protected final LinkedList<File> eaList = new LinkedList<>();
    protected final LinkedList<String> symbolList = new LinkedList<>();
    protected final LinkedList<Timeframe> timeframeList = new LinkedList<>();

    public void addExpert(File expertFile, String symbol, Timeframe timeframe) {
        eaList.add(expertFile);
        symbolList.add(symbol);
        timeframeList.add(timeframe);
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
        timeframeList.remove(index);
        fireTableRowsInserted(eaList.size() - 1, eaList.size() - 1);
        return expert;
    }

    public void removeAll() {
        eaList.clear();
        symbolList.clear();
        timeframeList.clear();
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
        String strTf = timeframeList.get(rowIndex).getString();

        
        return switch (columnIndex) {
            case 0 ->
                _symbol;
            case 1 ->                
                strTf;                 
            case 2 ->
                expert.getName();
            default ->
                null;
        };
    }
}

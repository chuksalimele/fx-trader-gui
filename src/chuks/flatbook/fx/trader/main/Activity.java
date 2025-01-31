/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.main;

import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.common.misc.Candle;
import chuks.flatbook.fx.common.util.CappedList;
import chuks.flatbook.fx.common.util.CappedMap;
import chuks.flatbook.fx.trader.expert.ExpertManager;
import chuks.flatbook.fx.trader.listener.ActivityAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *
 * @author user
 */
public class Activity extends ActivityAdapter {

    static private boolean isConnected;
    static private int accountNumber;
    private boolean isPlaftormConnected;
    static private Map<String, SymbolInfo> selectedSymbolInfoMap = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<Long, Order> openOrdersMap = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<Long, Order> historyOrdersMap = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<Long, Order> pendingOrdersMap = Collections.synchronizedMap(new LinkedHashMap());
    //comibined open and pending orders are trade orders
    static private Map<Long, Order> tradeOrdersMap = Collections.synchronizedMap(new LinkedHashMap());

    static private List<String> selectedSymbolList;
    static private List<String> symbolList;
    private static Order[] historyOrderArray;
    private static Order[] tradeOrdersArray;
    private long priceTime;
    static final private int MAX_TIMEFRAME_ENTRIES = 1000;
    static private Map<String, List<Candle>> tfM1 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfM5 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfM15 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfM30 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfH1 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfH4 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfD1 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfW1 = Collections.synchronizedMap(new LinkedHashMap());
    static private Map<String, List<Candle>> tfMN1 = Collections.synchronizedMap(new LinkedHashMap());

    public static boolean isIsConnected() {
        return isConnected;
    }
    public static int getAccountNumber() {
        return accountNumber;
    }
    
    public static double checkAccountMargin(String symbol, int type, double lot_size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountMargin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountFreeMargin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountEquity() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountProfit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountStopoutLevel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static int getAccountLeverage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountBalance() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static double getAccountCredit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static Map<String, List<Candle>> getTfMap(int timeframe) {
        switch (timeframe) {
            case  Timeframe.M1 -> {
                return  tfM1;
            }
            case Timeframe.M5 -> {
                return tfM5;
            }
            case Timeframe.M15 -> {
                return tfM15;
            }
            case Timeframe.M30 -> {
                return tfM30;
            }
            case Timeframe.H1 -> {
                return tfH1;
            }
            case Timeframe.H4 -> {
                return tfH4;
            }
            case Timeframe.D1 -> {
                return tfD1;
            }
            case Timeframe.W1 -> {
                return tfW1;
            }
            case Timeframe.MN1 -> {
                return tfMN1;
            }                
        }
        return null;
    }

    public static double getCandleOpen(String symbol, int timeframe, int shift) {
        List<Candle> candles = getTfMap(timeframe).get(symbol);
        if (candles == null) {
            return 0;
        }

        return shift >= 0 && shift < candles.size()
                ? candles.get(shift).getOpen()
                : 0;
    }

    public static double getCandleHigh(String symbol, int timeframe, int shift) {
        List<Candle> candles = getTfMap(timeframe).get(symbol);
        if (candles == null) {
            return 0;
        }

        return shift >= 0 && shift < candles.size()
                ? candles.get(shift).getHigh()
                : 0;
    }

    public static double getCandleLow(String symbol, int timeframe, int shift) {
        List<Candle> candles = getTfMap(timeframe).get(symbol);
        if (candles == null) {
            return 0;
        }

        return shift >= 0 && shift < candles.size()
                ? candles.get(shift).getLow()
                : 0;
    }

    public static double getCandleClose(String symbol, int timeframe, int shift) {
        List<Candle> candles = getTfMap(timeframe).get(symbol);
        if (candles == null) {
            return 0;
        }

        return shift >= 0 && shift < candles.size()
                ? candles.get(shift).getClose()
                : 0;
    }
    
    public static long getCandleTime(String symbol, int timeframe, int shift) {
        List<Candle> candles = getTfMap(timeframe).get(symbol);
        if (candles == null) {
            return 0;
        }

        return shift >= 0 && shift < candles.size()
                ? candles.get(shift).getTime()
                : 0;
    }
    
    public static int getCandleVolume(String symbol, int timeframe, int shift) {
        List<Candle> candles = getTfMap(timeframe).get(symbol);
        if (candles == null) {
            return 0;
        }

        return shift >= 0 && shift < candles.size()
                ? candles.get(shift).getVolume()
                : 0;
    }
    
    public static Map<String, SymbolInfo> getSelectedSymbolInfoMap() {
        return selectedSymbolInfoMap;
    }

    public static Map<Long, Order> getTradeOrdersMap() {
        return tradeOrdersMap;
    }

    public static Map<Long, Order> getOpenOrdersMap() {
        return openOrdersMap;
    }

    public static Map<Long, Order> getHistoryOrdersMap() {
        return historyOrdersMap;
    }

    public static Map<Long, Order> getPendingOrdersMap() {
        return pendingOrdersMap;
    }

    public static List<String> getSelectedSymbolList() {
        return selectedSymbolList;
    }

    public static List<String> getSymbolList() {
        return symbolList;
    }

    public static Order[] getHistoryOrdersArray() {
        return historyOrderArray;
    }

    public static Order[] getTradeOrdersArray() {
        return tradeOrdersArray;
    }
    

    @Override
    public void onLoggedIn(int account_number) {
        isConnected = true;
        this.accountNumber = account_number;
    }

    @Override
    public void onLoggedOut() {
        isConnected = false;
        this.accountNumber  = - 1;
    }

    @Override
    public void onConnected() {
        isPlaftormConnected = true;
    }

    @Override
    public void onDisconnected(String errMsg) {
        isPlaftormConnected = false;
        isConnected = false;
    }

    @Override
    public void onNewMarketOrder(Order order) {

        openOrdersMap.put(order.getTicket(), order);
        tradeOrdersMap.put(order.getTicket(), order);
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);

    }

    @Override
    public void onTriggeredPendingOrder(Order order) {
        //remove
        pendingOrdersMap.remove(order.getTicket());

        tradeOrdersMap.remove(order.getTicket());
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onClosedMarketOrder(Order order) {
        openOrdersMap.remove(order.getTicket());
        tradeOrdersMap.remove(order.getTicket());

        historyOrdersMap.put(order.getTicket(), order);

        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
        historyOrderArray = historyOrdersMap.values().toArray(Order[]::new);

    }

    @Override
    public void onModifiedMarketOrder(Order order) {
        openOrdersMap.put(order.getTicket(), order); //will replace old value
        tradeOrdersMap.put(order.getTicket(), order);  //will replace old value        
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onNewPendingOrder(Order order) {
        pendingOrdersMap.put(order.getTicket(), order);
        tradeOrdersMap.put(order.getTicket(), order);
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onDeletedPendingOrder(Order order) {
        pendingOrdersMap.remove(order.getTicket());
        tradeOrdersMap.remove(order.getTicket());

        historyOrdersMap.put(order.getTicket(), order);

        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
        historyOrderArray = historyOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onModifiedPendingOrder(Order order) {
        pendingOrdersMap.put(order.getTicket(), order); //will replace old value
        tradeOrdersMap.put(order.getTicket(), order);  //will replace old value      
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
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
    public void onAddAllOpenOrders(List<Order> orders) {
        orders.forEach((Order order) -> {
            openOrdersMap.put(order.getTicket(), order);
            tradeOrdersMap.put(order.getTicket(), order);
        });
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onAddAllPendingOrders(List<Order> orders) {
        orders.forEach((Order order) -> {
            pendingOrdersMap.put(order.getTicket(), order);
            tradeOrdersMap.put(order.getTicket(), order);
        });
        tradeOrdersArray = tradeOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> orders) {
        orders.forEach((Order order) -> {
            historyOrdersMap.put(order.getTicket(), order);
        });
        historyOrderArray = historyOrdersMap.values().toArray(Order[]::new);
    }

    @Override
    public void onSwapChange(SymbolInfo symbolInfo) {
    }

    @Override
    public void onPriceChange(SymbolInfo symbolInfo) {
        priceTime = System.currentTimeMillis();
        ExpertManager.submitOnTickEventOnAllExperts(symbolInfo.getName());
    }

    @Override
    public void onSymbolInfoAdded(SymbolInfo symbolInfo) {
        selectedSymbolInfoMap.put(symbolInfo.getName(), symbolInfo);
    }

    @Override
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo) {
        selectedSymbolInfoMap.remove(symbolInfo.getName());
    }

    @Override
    public void onGetFullRefereshSymbol(String symbolName) {
        if(!symbolList.contains(symbolName)){
            symbolList.add(symbolName);
        }
    }

    @Override
    public void onfullSymbolList(List<String> symbol_list) {
        symbolList = symbol_list;
    }

    @Override
    public void onSeletedSymbolList(List<String> symbol_list) {
        selectedSymbolList = symbol_list;        
    }

    @Override
    public void onSeletedSymbolInfoList(List<SymbolInfo> symbol_info_list) {

        selectedSymbolInfoMap.clear();
        symbol_info_list.forEach((SymbolInfo symbolInfo) -> {
            selectedSymbolInfoMap.put(symbolInfo.getName(), symbolInfo);
        });
    }

    public static void main(String args[]) {
        Map<Long, String> strMap = Collections.synchronizedMap(new LinkedHashMap());
        String[] arr = new String[0];
        strMap.put(1l, "a");
        strMap.put(2l, "b");
        strMap.put(3l, "c");
        String[] arr1 = strMap.values().toArray(String[]::new);
        arr = strMap.values().toArray(arr);

        for (int i = 0; i < arr1.length; i++) {
            System.out.println(arr1[i]);
        }

        System.out.println("----------------");

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        System.out.println("--------list--------");
        System.out.println(list.get(0));
    }
}

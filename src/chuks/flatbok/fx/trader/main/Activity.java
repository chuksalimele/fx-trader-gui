/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.trader.main;

import chuks.flatbok.fx.common.account.order.Order;
import chuks.flatbok.fx.common.account.order.SymbolInfo;
import chuks.flatbok.fx.common.listener.ConnectionListener;
import chuks.flatbok.fx.common.listener.OrderActionListener;
import chuks.flatbok.fx.common.listener.SymbolUpdateListener;
import chuks.flatbok.fx.trader.expert.ExpertManager;
import chuks.flatbok.fx.trader.listener.ActivityAdapter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class Activity extends ActivityAdapter {

    static private boolean isConnected;
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
    private static  Order[] tradeOrdersArray;

    public static boolean isIsConnected() {
        return isConnected;
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
    public void onLoggedIn() {
        isConnected = true;
    }

    @Override
    public void onLoggedOut() {
        isConnected = false;
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
        
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
        
    }

    @Override
    public void onTriggeredPendingOrder(Order order) {
        //remove
        pendingOrdersMap.remove(order.getTicket());
               
        tradeOrdersMap.remove(order.getTicket());  
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
    }

    @Override
    public void onClosedMarketOrder(Order order) {
        openOrdersMap.remove(order.getTicket());
        tradeOrdersMap.remove(order.getTicket());

        historyOrdersMap.put(order.getTicket(), order);
        
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
        historyOrderArray = historyOrdersMap.values().toArray(new Order[]{});
        
    }

    @Override
    public void onModifiedMarketOrder(Order order) {
        openOrdersMap.put(order.getTicket(), order); //will replace old value
        tradeOrdersMap.put(order.getTicket(), order);  //will replace old value        
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
    }

    @Override
    public void onNewPendingOrder(Order order) {
        pendingOrdersMap.put(order.getTicket(), order);
        tradeOrdersMap.put(order.getTicket(), order);
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
    }

    @Override
    public void onDeletedPendingOrder(Order order) {
        pendingOrdersMap.remove(order.getTicket());
        tradeOrdersMap.remove(order.getTicket());
                
        historyOrdersMap.put(order.getTicket(), order);

        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
        historyOrderArray = historyOrdersMap.values().toArray(new Order[]{});        
    }

    @Override
    public void onModifiedPendingOrder(Order order) {
        pendingOrdersMap.put(order.getTicket(), order); //will replace old value
        tradeOrdersMap.put(order.getTicket(), order);  //will replace old value      
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
    }

    @Override
    public void onOrderSendFailed(Order order, String errMsg) {
    }

    @Override
    public void onOrderRemoteError(Order order, String errMsg) {
    }

    @Override
    public void onOrderNotAvailable(String errMsg) {
    }

    @Override
    public void onAddAllOpenOrders(List<Order> orders) {
        orders.forEach((Order order) -> {
            openOrdersMap.put(order.getTicket(), order);
            tradeOrdersMap.put(order.getTicket(), order);
        });
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
    }

    @Override
    public void onAddAllPendingOrders(List<Order> orders) {
        orders.forEach((Order order) -> {
            pendingOrdersMap.put(order.getTicket(), order);
            tradeOrdersMap.put(order.getTicket(), order);
        });
        tradeOrdersArray = tradeOrdersMap.values().toArray(new Order[]{});
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> orders) {
        orders.forEach((Order order) -> {
            historyOrdersMap.put(order.getTicket(), order);
        });
        historyOrderArray = historyOrdersMap.values().toArray(new Order[]{});          
    }

    @Override
    public void onSwapChange(SymbolInfo symbolInfo) {
    }

    @Override
    public void onPriceChange(SymbolInfo symbolInfo) {
        ExpertManager.submitOnTickEventOnAllExperts(symbolInfo.getName());
    }

    @Override
    public void onSymbolInfoAdded(SymbolInfo symbolInfo) {
        //selectedSymbolInfoMap.put(symbolInfo.getName(), symbolInfo);
    }

    @Override
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo) {
        selectedSymbolInfoMap.remove(symbolInfo.getName());
    }

    @Override
    public void onGetFullRefereshSymbol(String symbolName) {
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

}

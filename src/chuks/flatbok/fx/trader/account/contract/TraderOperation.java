/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbok.fx.trader.account.contract;

import chuks.flatbok.fx.common.account.order.Order;
import chuks.flatbok.fx.common.account.order.SymbolInfo;
import chuks.flatbok.fx.trader.listener.AccountListener;
import chuks.flatbok.fx.common.listener.ConnectionListener;
import chuks.flatbok.fx.common.listener.OrderActionListener;
import chuks.flatbok.fx.common.listener.SymbolUpdateListener;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 *
 * @author user
 */
public interface TraderOperation {

    public SymbolInfo getSymbolInfo(String symbol);           
    
    public List<String> getSelectedSymbols();

    public void setSelectedSymbols(List<String> list);    

    public CompletableFuture sendMarketOrder(Order order);

    public CompletableFuture modifyOpenOrder(String clOrdId, double target_price, double stoploss_price);

    public CompletableFuture sendClosePosition(String clOrdId, double lot_size);
    
    public CompletableFuture placePendingOrder(Order order);

    public CompletableFuture modifyPendingOrder(String clOrdId, double open_price, double target_price, double stoploss_price);
    
    public CompletableFuture deletePendingOrder(String clOrdId);
    
    public void addOrderActionListener(OrderActionListener listener);

    public void addSymbolUpdateListener(SymbolUpdateListener listener);
    
    public void addConnectionListener(ConnectionListener listener);   
    
    public void addAccountListener(AccountListener listener);   
                
    public void shutdown();
       
    public void refreshContent();        
            
}

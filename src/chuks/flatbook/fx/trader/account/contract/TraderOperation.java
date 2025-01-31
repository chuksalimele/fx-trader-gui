/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.trader.account.contract;

import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.trader.listener.AccountListener;
import chuks.flatbook.fx.common.listener.ConnectionListener;
import chuks.flatbook.fx.common.listener.OrderActionListener;
import chuks.flatbook.fx.common.listener.SymbolUpdateListener;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author user
 */
public interface TraderOperation {
    
    public void subscribeToSelectedSymbols(List<String> list);    

    public CompletableFuture sendMarketOrder(Order order);

    public CompletableFuture modifyOpenOrder(String clOrdId, double target_price, double stoploss_price);

    public CompletableFuture sendClosePosition(String clOrdId, double lot_size, double price, int slippage);
    
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

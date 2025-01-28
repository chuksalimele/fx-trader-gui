/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.listener;

import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.common.listener.ConnectionListener;
import chuks.flatbook.fx.common.listener.OrderActionListener;
import chuks.flatbook.fx.common.listener.SymbolUpdateListener;
import java.util.List;

/**
 *
 * @author user
 */
public class ActivityAdapter implements OrderActionListener, SymbolUpdateListener,ConnectionListener, AccountListener{


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
    public void onTriggeredPendingOrder(Order order) {
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
    }

    @Override
    public void onAddAllPendingOrders(List<Order> orders) {
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> orders) {
    }

    @Override
    public void onSwapChange(SymbolInfo symbolInfo) {
    }

    @Override
    public void onPriceChange(SymbolInfo symbolInfo) {
    }

    @Override
    public void onSymbolInfoAdded(SymbolInfo symbolInfo) {
    }

    @Override
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo) {
    }

    @Override
    public void onGetFullRefereshSymbol(String symbolName) {
    }

    @Override
    public void onfullSymbolList(List<String> symbol_list) {
    }

    @Override
    public void onSeletedSymbolList(List<String> symbol_list) {
    }

    @Override
    public void onSeletedSymbolInfoList(List<SymbolInfo> symbol_info_list) {        
    }

    @Override
    public void onConnectionProgress(String status) {
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected(String errMsg) {
    }

    @Override
    public void onLoggedIn(int account_number) {
    }

    @Override
    public void onLogInFailed(String reason) {
    }

    @Override
    public void onLoggedOut() {
    }

    @Override
    public void onLogOutFailed(String reason) {
    }

    @Override
    public void onAccountOpen(int account_number) {
    }

    @Override
    public void onSignUpInitiated(String reason) {
    }
    
    @Override
    public void onSignUpFailed(String reason) {
    }

    @Override
    public void onAccountDisabled() {
    }

    @Override
    public void onAccountEnabled() {
    }

    @Override
    public void onAccountApproved() {
    }

    @Override
    public void onAccountClosed() {
    }

    @Override
    public void onPasswordChanged(char[] new_password) {        
    }
}

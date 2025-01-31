/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.transport;

import chuks.flatbook.fx.transport.SharableTransportHandler;
import chuks.flatbook.fx.trader.account.contract.AccountContext;
import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.transport.message.ChannelMessage;
import io.netty.channel.ChannelHandlerContext;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author user
 */
class TradingClientHandler extends SharableTransportHandler {

    private final AccountContext accountCtx;

    public TradingClientHandler(AccountContext context) {
        this.accountCtx = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send login message
        accountCtx.setContext(ctx);
        //ctx.writeAndFlush(new ChannelMessage(MessageType.LOGIN, username));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChannelMessage msg) throws Exception {
        
        switch (msg.getType()) {
            case LOGGED_IN ->
                handleLoggedIn(ctx, msg);
            case NEW_MARKET_ORDER ->
                handleNewMarketOrder(ctx, msg);
            case CLOSED_MARKET_ORDER ->
                handleClosedMarketOrder(ctx, msg);
            case MODIFIED_MARKET_ORDER ->
                handleModifiedMarketOrder(ctx, msg);
            case TRIGGERED_PENDING_ORDER ->
                handleTriggerredPendingOrder(ctx, msg);
            case NEW_PENDING_ORDER ->
                handleNewPendingOrder(ctx, msg);
            case DELETED_PENDING_ORDER ->
                handleDeletedPendingOrder(ctx, msg);
            case MODIFIED_PENDING_ORDER ->
                handleModifiedPendingOrder(ctx, msg);
            case ORDER_REMOTE_ERROR ->
                handleOrderRemoteError(ctx, msg);
            case ORDER_NOT_AVAILABLE ->
                handleOrderNotAvailable(ctx, msg);                
            case ADD_ALL_OPEN_ORDERS ->
                handleAddAllOpenOrders(ctx, msg);
            case ADD_ALL_PENDING_ORDERS ->
                handleAddAllPendingOrders(ctx, msg);        
            case ADD_ALL_HISTORY_ORDERS ->
                handleAddAllHistoryOrders(ctx, msg);
            case SWAP_CHANGE ->
                handleSwapChange(ctx, msg);
            case PRICE_CHANGE ->
                handlePriceChange(ctx, msg);        
            case FULL_SYMBOL_LIST ->
                handleFullSymbolList(ctx, msg);
            case SELECTED_SYMBOL_INFO_LIST ->
                handleSelectedSymbolInfoList(ctx, msg);                
            case LOGGED_OUT ->
                handleLogout(ctx, msg);
            case LOGIN_FAILED ->
                handleLoginFailed(ctx, msg);
            case LOGOUT_FAILED ->
                handleLogoutFaied(ctx, msg); 
            case SIGN_UP_FAILED ->
                handleSignUpFaied(ctx, msg);                 
            case SIGN_UP_INITIATED ->
                handleSignUpInitiated(ctx, msg);                    
        }
        
        String message_identifier = msg.getIdentifier();
        
        if(message_identifier != null){
            this.accountCtx.onMessageIdentifier(message_identifier);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void handleLoggedIn(ChannelHandlerContext ctx, ChannelMessage msg) {
        int account_number = msg.getInt(0);
        this.accountCtx.onLoggedIn(account_number);
    }

    private void handleNewMarketOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onNewMarketOrder(order);
    }

    private void handleClosedMarketOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onClosedMarketOrder(order);        
    }

    private void handleModifiedMarketOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onModifiedMarketOrder(order);        
    }

    private void handleTriggerredPendingOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onTriggeredPendingOrder(order);        
    }

    private void handleNewPendingOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onNewPendingOrder(order);        
    }

    private void handleDeletedPendingOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onDeletedPendingOrder(order);        
    }

    private void handleModifiedPendingOrder(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        this.accountCtx.onModifiedPendingOrder(order);        
    }

    private void handleOrderRemoteError(ChannelHandlerContext ctx, ChannelMessage msg) {
        Order order = new Order(msg.getString(0));
        String reason = msg.getString(1);
        this.accountCtx.onOrderRemoteError(order, reason);        
    }

    private void handleOrderNotAvailable(ChannelHandlerContext ctx, ChannelMessage msg) {
        String reason = msg.getString(0);
        this.accountCtx.onOrderNotAvailable(msg.getIdentifier(), reason);        
    }            
    
    private void addAllOrders0(ChannelMessage msg){
        List<Order> orders = new LinkedList();
        String[] stringified_order_arr = msg.isStringArray(0)? msg.getStringArray(0) : new String[0];
        for (String order_str : stringified_order_arr) {
            orders.add(new Order(order_str));
        }
        this.accountCtx.onAddAllOpenOrders(orders);        
    }
    
    private void handleAddAllOpenOrders(ChannelHandlerContext ctx, ChannelMessage msg) {
        addAllOrders0(msg);
    }

    private void handleAddAllPendingOrders(ChannelHandlerContext ctx, ChannelMessage msg) {
        addAllOrders0(msg);
    }

    private void handleAddAllHistoryOrders(ChannelHandlerContext ctx, ChannelMessage msg) {
        addAllOrders0(msg);
    }

    private void handleSwapChange(ChannelHandlerContext ctx, ChannelMessage msg) {
        SymbolInfo symbolInfo = new SymbolInfo(msg.getString(0));
        this.accountCtx.onSwapChange(symbolInfo);        
    }

    private void handlePriceChange(ChannelHandlerContext ctx, ChannelMessage msg) {
        SymbolInfo symbolInfo = new SymbolInfo(msg.getString(0));
        this.accountCtx.onPriceChange(symbolInfo);        
    }

    private void handleFullSymbolList(ChannelHandlerContext ctx, ChannelMessage msg) {
        List<String> symbols = new LinkedList();
        String [] symb_arr = msg.isStringArray(0)? msg.getStringArray(0) : new String[0];
        for (String payload1 : symb_arr) {
            symbols.add((String)payload1);
        }
        this.accountCtx.onfullSymbolList(symbols);                
    }

    private void handleSelectedSymbolInfoList(ChannelHandlerContext ctx, ChannelMessage msg) {
        List<SymbolInfo> symbolInfoList = new LinkedList();
        String [] stringified_symbInfo_arr = msg.isStringArray(0)? msg.getStringArray(0) : new String[0];
        for (String str_info : stringified_symbInfo_arr) {
            symbolInfoList.add(new SymbolInfo(str_info));
        }
        this.accountCtx.onSeletedSymbolInfoList(symbolInfoList);
    }

    private void handleLogout(ChannelHandlerContext ctx, ChannelMessage msg) {
        this.accountCtx.onLoggedOut();
    }

    private void handleLoginFailed(ChannelHandlerContext ctx, ChannelMessage msg) {
        this.accountCtx.onLogInFailed(msg.getString(1));
    }

    private void handleLogoutFaied(ChannelHandlerContext ctx, ChannelMessage msg) {
        this.accountCtx.onLogOutFailed(msg.getString(0));
    }

    private void handleSignUpFaied(ChannelHandlerContext ctx, ChannelMessage msg) {
        this.accountCtx.onSignUpFailed(msg.getString(0));
    }    
    
    private void handleSignUpInitiated(ChannelHandlerContext ctx, ChannelMessage msg) {
        this.accountCtx.onSignUpInitiated(msg.getString(0));
    }    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.transport;

import chuks.flatbook.fx.trader.account.contract.TraderAccount;
import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.trader.listener.AccountListener;
import chuks.flatbook.fx.common.listener.ConnectionListener;
import chuks.flatbook.fx.common.listener.OrderActionListener;
import chuks.flatbook.fx.common.listener.SymbolUpdateListener;
import chuks.flatbook.fx.common.util.OnceAccessStore;
import static chuks.flatbook.fx.trader.config.AppConfig.MAX_RESPONSE_WAIT_TIME;
import chuks.flatbook.fx.trader.exception.OrderNotFoundException;
import chuks.flatbook.fx.trader.exception.OrderSendException;
import chuks.flatbook.fx.trader.main.Activity;
import chuks.flatbook.fx.transport.message.MessageFactory;
import chuks.flatbook.fx.transport.message.MessageType;
import static chuks.flatbook.fx.transport.message.MessageType.SUBCRIBE_SYMBOLS;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 *
 * @author user
 */
public class TraderAccountManager implements TraderAccount {

    ChannelHandlerContext ctx;
    private boolean isLoggedIn;
    private int accountNumber;
    private String accountName;
    OnceAccessStore<String, CompletableFuture> requestFutureStore = new OnceAccessStore();

    List<OrderActionListener> orderActionListenerList = new LinkedList();
    List<SymbolUpdateListener> symbolUpdateListenerList = new LinkedList();
    List<ConnectionListener> connectionListenerList = new LinkedList();
    List<AccountListener> accountListenerList = new LinkedList();

    String getUniqe() {

        // Convert UUID to string and remove the hyphens        
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * this method is called when a message contain request identifier
     *
     * It must be called last after all operation especially for the purpose to
     * free up resources for storing CompletableFuture objects
     *
     * @param message_identifier
     */
    @Override
    public void onMessageIdentifier(String message_identifier) {

        CompletableFuture future = requestFutureStore
                .getMappedItemAndDelete(message_identifier);

        if (future != null) {
            //awake any waiting Future 
            future.complete("Completed");
        }

    }

    @Override
    public void setIsLoggeIn(boolean is_logged_in) {
        isLoggedIn = is_logged_in;
    }    
    
    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public void setContext(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String getAccountName() {
        return accountName;
    }

    @Override
    public char[] getPassword() {
        return new char[0];
    }


    void handleWriteCompletion(ChannelFuture future, Consumer success, Consumer error) {
        // Add a listener to the future to check if the write was successful
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (future.isSuccess()) {
                    System.out.println("Message sent successfully");
                    success.accept("");
                } else {
                    System.err.println("Failed to send message");
                    error.accept(future.cause().getMessage());
                    future.cause().printStackTrace();
                }
                // Remove this listener since it won't be needed again
                future.removeListener(this);
            }
        });

    }

    @Override
    public void signUp(String email, byte[] hash_password, String first_name, String last_name) {

        ChannelFuture future = ctx.writeAndFlush(
                MessageFactory
                        .create(MessageType.SIGN_UP)
                        .assign(email, hash_password, first_name, last_name)
        );

        handleWriteCompletion(future, success -> {

        }, error -> {

        });

    }

    @Override
    public void login(int account_number, byte[] hash_password) {

        ChannelFuture future = ctx.writeAndFlush(
                MessageFactory
                        .create(MessageType.LOGIN)
                        .assign(account_number, hash_password
                        ));

        handleWriteCompletion(future, success -> {

        }, error -> {

        });
    }

    @Override
    public void logout(int account_number) {

        ChannelFuture future = ctx.writeAndFlush(
                MessageFactory
                        .create(MessageType.LOGOUT)
                        .assign(account_number)
        );

        handleWriteCompletion(future, success -> {

        }, error -> {

        });
    }

    @Override
    public CompletableFuture sendMarketOrder(Order order) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String requestIdentifier = getUniqe();

        ChannelFuture future = ctx.writeAndFlush(MessageFactory
                .create(MessageType.SEND_MARKET_ORDER, requestIdentifier)
                .assign(Activity.getAccountNumber(), order.stringify()));
        handleWriteCompletion(future, success -> {

            //store the completableFuture object against the request
            //identifier to track when the response comes from the remote end
            requestFutureStore.put(requestIdentifier, completableFuture);

            //We do not want to wait indefinetly in case the remote
            //end fails to send reponse
            completableFuture.orTimeout(MAX_RESPONSE_WAIT_TIME, TimeUnit.SECONDS);

        }, error -> {
            String errMsg = "Could not send market order. Check connection.";
            completableFuture.completeExceptionally(new OrderSendException(errMsg));
            onOrderSendFailed(order, errMsg);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture modifyOpenOrder(String clOrdId, double target_price, double stoploss_price) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String requestIdentifier = getUniqe();

        ChannelFuture future = ctx.writeAndFlush(MessageFactory
                .create(MessageType.MODIFY_OPEN_ORDER, requestIdentifier)
                .assign(clOrdId, target_price, stoploss_price));

        handleWriteCompletion(future, success -> {

            //store the completableFuture object against the request
            //identifier to track when the response comes from the remote end
            requestFutureStore.put(requestIdentifier, completableFuture);

            //We do not want to wait indefinetly in case the remote
            //end fails to send reponse
            completableFuture.orTimeout(MAX_RESPONSE_WAIT_TIME, TimeUnit.SECONDS);

        }, error -> {
            String errMsg = "Could not modify market order. Check connection.";
            completableFuture.completeExceptionally(new OrderSendException(errMsg));
            onOrderSendFailed(null, errMsg);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture sendClosePosition(String clOrdId, double lot_size, double price, int slippage) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String requestIdentifier = getUniqe();

        ChannelFuture future = ctx.writeAndFlush(MessageFactory
                .create(MessageType.SEND_CLOSE_POSITION, requestIdentifier)
                .assign(clOrdId, lot_size, price, slippage));
        handleWriteCompletion(future, success -> {

            //store the completableFuture object against the request
            //identifier to track when the response comes from the remote end
            requestFutureStore.put(requestIdentifier, completableFuture);

            //We do not want to wait indefinetly in case the remote
            //end fails to send reponse
            completableFuture.orTimeout(MAX_RESPONSE_WAIT_TIME, TimeUnit.SECONDS);

        }, error -> {
            String errMsg = "Could not close position. Check connection.";
            completableFuture.completeExceptionally(new OrderSendException(errMsg));
            onOrderSendFailed(null, errMsg);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture placePendingOrder(Order order) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String requestIdentifier = getUniqe();

        ChannelFuture future = ctx.writeAndFlush(MessageFactory
                .create(MessageType.PLACE_PENDING_ORDER, requestIdentifier)
                .assign(Activity.getAccountNumber(), order.stringify()));
        handleWriteCompletion(future, success -> {

            //store the completableFuture object against the request
            //identifier to track when the response comes from the remote end
            requestFutureStore.put(requestIdentifier, completableFuture);

            //We do not want to wait indefinetly in case the remote
            //end fails to send reponse
            completableFuture.orTimeout(MAX_RESPONSE_WAIT_TIME, TimeUnit.SECONDS);

        }, error -> {
            String errMsg = "Could not place pending order. Check connection.";
            completableFuture.completeExceptionally(new OrderSendException(errMsg));
            onOrderSendFailed(order, errMsg);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture modifyPendingOrder(String clOrdId, double open_price, double target_price, double stoploss_price) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String requestIdentifier = getUniqe();

        ChannelFuture future = ctx.writeAndFlush(MessageFactory
                .create(MessageType.MODIFY_PENDING_ORDER, requestIdentifier)
                .assign(clOrdId, open_price, target_price, stoploss_price));

        handleWriteCompletion(future, success -> {

            //store the completableFuture object against the request
            //identifier to track when the response comes from the remote end
            requestFutureStore.put(requestIdentifier, completableFuture);

            //We do not want to wait indefinetly in case the remote
            //end fails to send reponse
            completableFuture.orTimeout(MAX_RESPONSE_WAIT_TIME, TimeUnit.SECONDS);

        }, error -> {
            String errMsg = "Could not modify pending order. Check connection.";
            completableFuture.completeExceptionally(new OrderSendException(errMsg));
            onOrderSendFailed(null, errMsg);
        });

        return completableFuture;
    }

    @Override
    public CompletableFuture deletePendingOrder(String clOrdId) {

        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        String requestIdentifier = getUniqe();

        ChannelFuture future = ctx.writeAndFlush(MessageFactory
                .create(MessageType.DELETE_PENDING_ORDER, requestIdentifier)
                .assign(clOrdId));
        handleWriteCompletion(future, success -> {

            //store the completableFuture object against the request
            //identifier to track when the response comes from the remote end
            requestFutureStore.put(requestIdentifier, completableFuture);

            //We do not want to wait indefinetly in case the remote
            //end fails to send reponse
            completableFuture.orTimeout(MAX_RESPONSE_WAIT_TIME, TimeUnit.SECONDS);

        }, error -> {

            String errMsg = "Could not delete pending order. Check connection.";
            completableFuture.completeExceptionally(new OrderSendException(errMsg));
            onOrderSendFailed(null, errMsg);
        });

        return completableFuture;
    }

    @Override
    public void addOrderActionListener(OrderActionListener listener) {
        orderActionListenerList.add(listener);
    }

    @Override
    public void addSymbolUpdateListener(SymbolUpdateListener listener) {
        symbolUpdateListenerList.add(listener);
    }

    @Override
    public void addConnectionListener(ConnectionListener listener) {
        connectionListenerList.add(listener);
    }

    @Override
    public void addAccountListener(AccountListener listener) {
        accountListenerList.add(listener);
    }

    @Override
    public void subscribeToSelectedSymbols(List<String> list) {
        String[] symbols = list.toArray(String[]::new);
        
        ChannelFuture future = ctx.writeAndFlush(
                MessageFactory
                        .create(MessageType.SUBCRIBE_SYMBOLS)
                        .assign(Activity.getAccountNumber(), symbols)
        );
        
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void refreshContent() {
        ctx.writeAndFlush(MessageFactory
                .create(MessageType.REFRESH_CONTENT)
                .assign(""));
    }

    @Override
    public void onNewMarketOrder(Order order) {
        CompletableFuture future = this.requestFutureStore.getMappedItemAndDelete(
                order.getMarketOrderRequestIdentifier());

        if (future != null) {
            future.complete(order.getTicket());
        }
        orderActionListenerList.forEach(listener -> {
            listener.onNewMarketOrder(order);
        });
    }

    @Override
    public void onClosedMarketOrder(Order order) {
        CompletableFuture future = this.requestFutureStore
                .getMappedItemAndDelete(order.getCloseOrderRequestIdentifier());

        if (future != null) {
            future.complete(true);
        }
        orderActionListenerList.forEach(listener -> {
            listener.onClosedMarketOrder(order);
        });
    }

    @Override
    public void onModifiedMarketOrder(Order order) {
        CompletableFuture future = this.requestFutureStore
                .getMappedItemAndDelete(order.getModifyOrderRequestIdentifier());

        if (future != null) {
            future.complete(true);
        }

        orderActionListenerList.forEach(listener -> {
            listener.onModifiedMarketOrder(order);
        });
    }

    @Override
    public void onTriggeredPendingOrder(Order order) {
        orderActionListenerList.forEach(listener -> {
            listener.onTriggeredPendingOrder(order);
        });
    }

    @Override
    public void onNewPendingOrder(Order order) {
        CompletableFuture future = this.requestFutureStore
                .getMappedItemAndDelete(order.getPendingOrderRequestIdentifier());

        if (future != null) {
            future.complete(order.getTicket());
        }

        orderActionListenerList.forEach(listener -> {
            listener.onNewPendingOrder(order);
        });
    }

    @Override
    public void onDeletedPendingOrder(Order order) {
        CompletableFuture future = this.requestFutureStore
                .getMappedItemAndDelete(order.getDeleteOrderRequestIdentifier());

        if (future != null) {
            future.complete(true);
        }

        orderActionListenerList.forEach(listener -> {
            listener.onDeletedPendingOrder(order);
        });
    }

    @Override
    public void onModifiedPendingOrder(Order order) {
        CompletableFuture future = this.requestFutureStore
                .getMappedItemAndDelete(order.getModifyOrderRequestIdentifier());

        if (future != null) {
            future.complete(true);
        }

        orderActionListenerList.forEach(listener -> {
            listener.onModifiedPendingOrder(order);
        });
    }

    void handleOrderError(Order order, String errMsg) {

        orderErrorFuture(this.requestFutureStore
                .getMappedItemAndDelete(order.getMarketOrderRequestIdentifier()), errMsg);

        orderErrorFuture(this.requestFutureStore
                .getMappedItemAndDelete(order.getPendingOrderRequestIdentifier()), errMsg);

        //for stoploss and target modification
        orderErrorFuture(this.requestFutureStore
                .getMappedItemAndDelete(order.getModifyOrderRequestIdentifier()), errMsg);

        orderErrorFuture(this.requestFutureStore
                .getMappedItemAndDelete(order.getCloseOrderRequestIdentifier()), errMsg);

        orderErrorFuture(this.requestFutureStore
                .getMappedItemAndDelete(order.getDeleteOrderRequestIdentifier()), errMsg);

    }

    void orderErrorFuture(CompletableFuture future, String errMsg) {
        if (future != null) {
            future.completeExceptionally(new OrderSendException(errMsg));
        }
    }

    @Override
    public void onOrderSendFailed(Order order, String errMsg) {
        orderActionListenerList.forEach(listener -> {
            listener.onOrderSendFailed(order, errMsg);
        });
    }

    @Override
    public void onOrderRemoteError(Order order, String errMsg) {
        handleOrderError(order, errMsg);
        orderActionListenerList.forEach(listener -> {
            listener.onOrderSendFailed(order, errMsg);
        });
    }

    @Override
    public void onOrderNotAvailable(String req_identifier, String reason) {
        CompletableFuture future = this.requestFutureStore
                .getMappedItemAndDelete(req_identifier);
        if (future != null) {
            future.completeExceptionally(new OrderNotFoundException("Order not available at the remote end"));
        }
        orderActionListenerList.forEach(listener -> {
            listener.onOrderNotAvailable(req_identifier, reason);
        });
    }

    @Override
    public void onAddAllOpenOrders(List<Order> orders) {
        orderActionListenerList.forEach(listener -> {
            listener.onAddAllOpenOrders(orders);
        });
    }

    @Override
    public void onAddAllPendingOrders(List<Order> orders) {
        orderActionListenerList.forEach(listener -> {
            listener.onAddAllOpenOrders(orders);
        });
    }

    @Override
    public void onAddAllHistoryOrders(List<Order> orders) {
        orderActionListenerList.forEach(listener -> {
            listener.onAddAllOpenOrders(orders);
        });
    }

    @Override
    public void onSwapChange(SymbolInfo symbolInfo) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onSwapChange(symbolInfo);
        });
    }

    @Override
    public void onPriceChange(SymbolInfo symbolInfo) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onPriceChange(symbolInfo);
        });
    }

    @Override
    public void onSymbolInfoAdded(SymbolInfo symbolInfo) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onSymbolInfoAdded(symbolInfo);
        });
    }

    @Override
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onSymbolInfoRemoved(symbolInfo);
        });
    }

    @Override
    public void onGetFullRefereshSymbol(String symbolName) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onGetFullRefereshSymbol(symbolName);
        });
    }

    @Override
    public void onfullSymbolList(List<String> symbol_list) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onfullSymbolList(symbol_list);
        });
    }

    @Override
    public void onSeletedSymbolList(List<String> symbol_list) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onSeletedSymbolList(symbol_list);
        });
    }

    @Override
    public void onSeletedSymbolInfoList(List<SymbolInfo> symbol_info_list) {
        symbolUpdateListenerList.forEach(listener -> {
            listener.onSeletedSymbolInfoList(symbol_info_list);
        });
    }

    @Override
    public void onConnectionProgress(String status) {
        connectionListenerList.forEach(listener -> {
            listener.onConnectionProgress(status);
        });
    }

    @Override
    public void onLoggedIn(int account_number) {
        setIsLoggeIn(true);
        accountListenerList.forEach(listener -> {
            listener.onLoggedIn(account_number);
        });
    }

    @Override
    public void onLoggedOut() {
        setIsLoggeIn(false);
        accountListenerList.forEach(listener -> {
            listener.onLoggedOut();
        });
    }

    @Override
    public void onLogInFailed(String reason) {
        accountListenerList.forEach(listener -> {
            listener.onLogInFailed(reason);
        });
    }

    @Override
    public void onLogOutFailed(String reason) {
        accountListenerList.forEach(listener -> {
            listener.onLogOutFailed(reason);
        });
    }

    static public void test(int account_number, Object... args) {
        System.out.println(args[1] instanceof int[]);
        System.out.println(((int[]) args[1])[1]);
    }

    public static void main(String[] args) {
        //char[] c = {'a','b','c'};
        int[] c = {1, 2, 3};
        //String[] c = {"1yw","2","3"};

        int n = 90;
        test(n, c, c);
    }

    @Override
    public void onConnected() {
        connectionListenerList.forEach(listener -> {
            listener.onConnected();
        });
    }

    @Override
    public void onDisconnected(String errMsg) {
        connectionListenerList.forEach(listener -> {
            listener.onDisconnected(errMsg);
        });
    }

    @Override
    public void onAccountOpen(int account_number) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onSignUpInitiated(String email) {
        accountListenerList.forEach(listener -> {
            listener.onSignUpInitiated(email);
        });
    }

    @Override
    public void onSignUpFailed(String reason) {
        accountListenerList.forEach(listener -> {
            listener.onSignUpFailed(reason);
        });
    }

    @Override
    public void onAccountDisabled() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onAccountEnabled() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onAccountApproved() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onAccountClosed() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onPasswordChanged(char[] new_password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

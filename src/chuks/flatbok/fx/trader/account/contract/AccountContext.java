/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbok.fx.trader.account.contract;

import chuks.flatbok.fx.common.account.order.Order;
import chuks.flatbok.fx.trader.listener.AccountListener;
import chuks.flatbok.fx.common.listener.ConnectionListener;
import chuks.flatbok.fx.common.listener.OrderActionListener;
import chuks.flatbok.fx.common.listener.SymbolUpdateListener;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author user
 */
public interface AccountContext extends OrderActionListener, SymbolUpdateListener, ConnectionListener, AccountListener{
    void setContext(ChannelHandlerContext ctx);

    public void onMessageIdentifier(String message_identifier);
    @Override
    public void onOrderRemoteError(Order order, String reason);
    @Override
    public void onOrderNotAvailable(String req_identifier, String reason);
    
}

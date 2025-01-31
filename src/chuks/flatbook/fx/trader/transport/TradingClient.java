package chuks.flatbook.fx.trader.transport;

import chuks.flatbook.fx.transport.SharableTransportHandler;
import chuks.flatbook.fx.transport.TransportClient;
import chuks.flatbook.fx.trader.account.contract.AccountContext;
import chuks.flatbook.fx.trader.account.contract.TraderAccount;

public class TradingClient extends TransportClient {

    private final TraderAccount traderAccount;
    private final TradingClientHandler handler;

    public TradingClient(TraderAccount traderAccount, String host, int port) throws Exception {
        super(host, port);
        this.traderAccount = traderAccount;
        handler =  new TradingClientHandler((AccountContext) traderAccount);        
    }

    @Override
    protected SharableTransportHandler getHandler() {
       return handler; 
    }

    @Override
    protected void onConnected() {
        traderAccount.onConnected();
    }

    @Override
    protected void onDisconnected(String msg) {
        traderAccount.onDisconnected(msg);
        traderAccount.setIsLoggeIn(false);
    }

}

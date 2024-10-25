package chuks.flatbok.fx.trader.transport;

import chuks.flatbok.fx.transport.SharableTransportHandler;
import chuks.flatbok.fx.transport.TransportClient;
import chuks.flatbok.fx.trader.account.contract.AccountContext;
import chuks.flatbok.fx.trader.account.contract.TraderAccount;

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
    }

}

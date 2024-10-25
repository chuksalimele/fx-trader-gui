/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.trader.expert;

import chuks.flatbok.fx.common.account.order.Order;
import chuks.flatbok.fx.common.account.order.OrderException;
import chuks.flatbok.fx.common.account.order.SymbolInfo;
import chuks.flatbok.fx.trader.account.contract.TraderAccount;
import chuks.flatbok.fx.trader.main.Activity;
import chuks.flatbok.fx.trader.main.MainGUI;
import chuks.flatbok.fx.transport.message.ChannelMessage;
import expert.ExpertAdvisorMQ4;
import expert.contract.IExpertAdvisor;
import expert.contract.IExpertService;
import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user
 */
class ExpertServiceImpl implements IExpertService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExpertServiceImpl.class.getName());
    ExpertAdvisorMQ4 expert;
    private boolean isExpertEnabled;
    private boolean isDllsAllowed;
    private boolean isTradeAllowed;
    private int positionsTotal;
    private double point;
    private int ordersTotal;
    private int ordersHistoryTotal;
    private Order selectedOrder;
    private double accountBalance;
    private double accountEquity;
    private double accountMargin;
    private double accountMarginStopout;
    private String expertSymbol;
    private int lastErrorCode;
    private boolean isStop;
    private String __PATH__ = "";
    private String __FILE__ = "";
    DecimalFormat decimalFormat = new DecimalFormat();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ExpertServiceImpl(ExpertAdvisorMQ4 expert) {
        this.expert = expert;
    }

    public void SET__PATH__(String path) {
        __PATH__ = path;
    }

    @Override
    public String __PATH__() {
        return __PATH__;
    }

    public void SET__FILE__(String file) {
        __FILE__ = file;
    }

    @Override
    public String __FILE__() {
        return __FILE__;
    }

    void setSymbol(String symbol) {
        expertSymbol = symbol;
    }

    public ExpertAdvisorMQ4 getExpert() {
        return expert;
    }

    Future asynWait(Runnable runnable) {
        return ExpertManager.getExecutor().submit(runnable);
    }

    @Override
    public void setIsExpertEnabled(boolean b) {
        isExpertEnabled = b;
    }

    @Override
    public void setIsDllsAllowed(boolean b) {
        isDllsAllowed = b;
    }

    @Override
    public void setIsTradeAllowed(boolean b) {
        isTradeAllowed = b;
    }

    @Override
    public int OnInit() {
        return this.getExpert().OnInit();
    }

    @Override
    public void OnDeinit(int reason) {
        this.getExpert().OnDeinit(reason);
        EventKillTimer();
        isStop = true;
    }

    @Override
    public void OnTick() {
        this.getExpert().OnTick();
    }

    @Override
    public void OnTimer() {
        this.getExpert().OnTimer();
    }

    @Override
    public void OnTrade() {
        this.getExpert().OnTrade();
    }

    @Override
    public void OnTradeTransaction() {
        this.getExpert().OnTradeTransaction();
    }

    @Override
    public boolean IsExpertEnabled() {
        return isExpertEnabled;
    }

    @Override
    public boolean IsDllsAllowed() {
        return isDllsAllowed;
    }

    @Override
    public boolean IsTradeAllowed() {
        return isTradeAllowed;
    }

    @Override
    public double MathAbs(double num) {
        return Math.abs(num);
    }

    @Override
    public double AccountFreeMarginCheck(String symbol, int type, double lot_size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountMargin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountFreeMargin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountEquity() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountProfit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountStopoutLevel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountLeverage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountBalance() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double AccountCredit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean IsConnected() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long TimeCurrent() {
        return System.currentTimeMillis();
    }

    @Override
    public int PositionsTotal() {
        return positionsTotal;
    }

    @Override
    public double NormalizeDouble(double d, int digits) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double Point() {
        return point;
    }

    @Override
    public int OrdersTotal() {
        return ordersTotal;
    }

    @Override
    public int OrdersHistoryTotal() {
        return ordersHistoryTotal;
    }

    @Override
    public double OrderOpenPrice() {
        return selectedOrder.getOpenPrice();
    }

    @Override
    public double OrderClosePrice() {
        return selectedOrder.getClosePrice();
    }

    @Override
    public long OrderOpenTime() {
        return selectedOrder.getOpenTime().getTime();
    }

    @Override
    public long OrderCloseTime() {
        return selectedOrder.getCloseTime().getTime();
    }

    @Override
    public String OrderSymbol() {
        return selectedOrder.getSymbol();
    }

    @Override
    public double OrderLots() {
        return selectedOrder.getLotSize();
    }

    @Override
    public double OrderSwap() {
        return selectedOrder.getSwap();
    }

    @Override
    public double OrderType() {
        return selectedOrder.getSide();
    }

    @Override
    public long OrderTicket() {
        return selectedOrder.getTicket();
    }

    @Override
    public double OrderCommission() {
        return selectedOrder.getCommission();
    }

    @Override
    public double OrderTakeProfit() {
        return selectedOrder.getTargetPrice();
    }

    @Override
    public double OrderProfit() {
        return selectedOrder.getProfit();
    }

    @Override
    public double OrderStopLoss() {
        return selectedOrder.getStoplossPrice();
    }

    @Override
    public double AccountInfoDouble(int code) {
        switch (code) {
            case ACCOUNT_BALANCE -> {
                return accountBalance;
            }
            case ACCOUNT_CREDIT -> {
                return accountEquity;
            }
            case ACCOUNT_MARGIN -> {
                return accountMargin;
            }
            case ACCOUNT_MARGIN_SO_SO -> {
                return accountMarginStopout;
            }
            default -> {
            }
        }

        return -1;//UNKNOWN
    }

    @Override
    public int SymbolInfoInteger(String symbol, int code) {

        SymbolInfo symbolInfo = Activity.getSelectedSymbolInfoMap().get(symbol);
        if (symbolInfo == null) {
            SetLastError(ERR_SYMBOL_NOT_FOUND);
            return -1;
        }
        switch (code) {
            case SYMBOL_DIGITS -> {
                return symbolInfo.getDigits();
            }
            case SYMBOL_TRADE_MODE -> {

                if (symbolInfo.isDisabled()) {
                    return SYMBOL_TRADE_MODE_DISABLED;
                } else if (symbolInfo.isAllowLongTradesOnly()) {
                    return SYMBOL_TRADE_MODE_LONGONLY;
                } else if (symbolInfo.isAllowShortTradesOnly()) {
                    return SYMBOL_TRADE_MODE_SHORTONLY;
                } else if (symbolInfo.isAllowCloseTradesOnly()) {
                    return SYMBOL_TRADE_MODE_CLOSEONLY;
                } else if (symbolInfo.isNoRestriction()) {
                    return SYMBOL_TRADE_MODE_FULL;
                }
            }
            default -> {
            }
        }

        return -1; //COMEBACK
    }

    @Override
    public double SymbolInfoDouble(String symbol, int code) {

        SymbolInfo symbolInfo = Activity.getSelectedSymbolInfoMap().get(symbol);
        if (symbolInfo == null) {
            SetLastError(ERR_SYMBOL_NOT_FOUND);
            return -1;
        }
        switch (code) {
            case SYMBOL_TRADE_TICK_SIZE -> {
                return symbolInfo.getTickSize();
            }
            case SYMBOL_TRADE_TICK_VALUE -> {
                return symbolInfo.getTickValue();
            }
            case SYMBOL_VOLUME_MIN -> {
                return symbolInfo.getMinAllowedVolume();
            }
            case SYMBOL_VOLUME_MAX -> {
                return symbolInfo.getMaxAllowedVolume();
            }
            case SYMBOL_SWAP_LONG -> {
                return symbolInfo.getSwapLong();
            }
            case SYMBOL_SWAP_SHORT -> {
                return symbolInfo.getSwapShort();
            }
            case SYMBOL_SPREAD -> {
                return symbolInfo.getSpreadPipette();//acatually it is the pipette
            }
            case SYMBOL_ASK -> {
                return symbolInfo.getAsk();
            }
            case SYMBOL_BID -> {
                return symbolInfo.getBid();
            }
            default -> {
            }
        }

        return -1; //COMEBACK
    }

    @Override
    public String Symbol() {
        return expertSymbol;
    }

    @Override
    public boolean OrderSelect(long index, int select_type, int mode) {
        if (select_type == SELECT_BY_POS) {
            if (mode == MODE_TRADES) {
                selectedOrder = Activity.getTradeOrdersArray()[(int) index];
                return true;
            } else if (mode == MODE_HISTORY) {
                selectedOrder = Activity.getHistoryOrdersArray()[(int) index];
                return true;
            }
        } else if (select_type == SELECT_BY_TICKET) {
            //according to MQL4 the index is the ticket
            //so we will get from the respective order map
            if (mode == MODE_TRADES) {
                return ((selectedOrder = Activity.getTradeOrdersMap().get(index)) != null);
            } else if (mode == MODE_HISTORY) {
                return ((selectedOrder = Activity.getHistoryOrdersMap().get(index)) != null);
            }
        }

        return false;
    }

    @Override
    public boolean OrderSelect(long ticket, int select_by) {
        return OrderSelect(ticket, SELECT_BY_TICKET, MODE_TRADES);
    }

    @Override
    public boolean OrderDelete(long ticket) {
        try {
            Order order = Activity.getPendingOrdersMap().get(ticket);
            if (order == null) {
                SetLastError(ERR_ORDER_NOT_FOUND);
                return false;
            }

            Future future = ExpertManager.getTraderAccount().deletePendingOrder(order.getOrderID());

            // Once the task is done, get the result            
            return (boolean) future.get();
        } catch (InterruptedException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    @Override
    public long OrderSend(String symbol, int order_type, double lot_size, double entry_price, int slippage, double stoploss, double target, String comment, int magic_number, long expiry) {
        int FAIL_INT_VALUE = -1;
        try {
            SymbolInfo symbol_info = Activity.getSelectedSymbolInfoMap().get(symbol);
            if (symbol_info == null) {
                SetLastError(ERR_SYMBOL_NOT_FOUND);
                return FAIL_INT_VALUE;
            }

            char side = (char) order_type;

            Order order = new Order(symbol_info, side, target, stoploss);
            Future future;
            switch (side) {
                case Order.Side.BUY, Order.Side.SELL ->
                    future = ExpertManager
                            .getTraderAccount()
                            .sendMarketOrder(order);
                case Order.Side.BUY_STOP, Order.Side.SELL_STOP, Order.Side.BUY_LIMIT, Order.Side.SELL_LIMIT ->
                    future = ExpertManager
                            .getTraderAccount()
                            .placePendingOrder(order);
                default -> {
                    SetLastError(ERR_UNKNOWN_ORDER_TYPE);
                    return FAIL_INT_VALUE;
                }
            }
            // Once the task is done, get the result            
            return (long) future.get(); //block till the task is completed                       
        } catch (OrderException | InterruptedException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return FAIL_INT_VALUE;
    }

    @Override
    public boolean OrderModify(long ticket, double open_price, double stoploss, double target, long expiry) {
        try {
            SymbolInfo symbol_info = Activity.getSelectedSymbolInfoMap().get(selectedOrder.getSymbol());
            if (symbol_info == null) {
                SetLastError(ERR_SYMBOL_NOT_FOUND);
                return false;
            }

            Order order = Activity.getTradeOrdersMap().get(ticket);

            if (order == null) {
                SetLastError(ERR_ORDER_NOT_FOUND);
                return false;
            }

            char side = (char) order.getSide();

            Future future;
            switch (side) {
                case Order.Side.BUY, Order.Side.SELL ->
                    future = ExpertManager
                            .getTraderAccount()
                            .modifyOpenOrder(order.getOrderID(), target, stoploss);
                case Order.Side.BUY_STOP, Order.Side.SELL_STOP, Order.Side.BUY_LIMIT, Order.Side.SELL_LIMIT ->
                    future = ExpertManager
                            .getTraderAccount()
                            .modifyPendingOrder(order.getOrderID(), open_price, target, stoploss);
                default -> {
                    SetLastError(ERR_UNKNOWN_ORDER_TYPE);
                    return false;
                }
            }
            // Once the task is done, get the result            
            return (boolean) future.get(); //block till the task is completed                       
        } catch (InterruptedException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    @Override
    public boolean OrderClose(long ticket, double lots, int mode, int slippage) {
        try {
            SymbolInfo symbol_info = Activity.getSelectedSymbolInfoMap().get(selectedOrder.getSymbol());
            if (symbol_info == null) {
                SetLastError(ERR_SYMBOL_NOT_FOUND);
                return false;
            }

            Order order = Activity.getTradeOrdersMap().get(ticket);

            if (order == null) {
                SetLastError(ERR_ORDER_NOT_FOUND);
                return false;
            }

            Future future = ExpertManager
                    .getTraderAccount()
                    .sendClosePosition(order.getOrderID(), lots);

            // Once the task is done, get the result            
            return (boolean) future.get(); //block till the task is completed                       
        } catch (InterruptedException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    @Override
    public int OrderMagicNumber() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int GetLastError() {
        return lastErrorCode;
    }

    @Override
    public void ResetLastError() {
        lastErrorCode = 0;
    }

    @Override
    public String ErrorDescription(int error) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int MarketInfo(String symbo, int mode) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void Print(Object... args) {
        //TODO
    }

    @Override
    public void PrintFormat(String str, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String StringFormat(String str, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void Comment(Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int StringLen(String str) {
        return str.length();
    }

    @Override
    public String StringSubstr(String path, int from, int to) {
        return path.substring(from, to);
    }

    @Override
    public String StringSubstr(String path, int from) {
        return path.substring(from);
    }

    @Override
    public boolean IsDemo() {
        return false;//TODO - NOT YET SUPPORTED
    }

    @Override
    public String AccountCompany() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long AccountNumber() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String AccountName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String AccountCurrency() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String TerminalPath() {
        return System.getProperty("user.dir");
    }

    @Override
    public boolean EventSetMillisecondTimer(int millsec) {
        scheduler.scheduleWithFixedDelay(
                ()->OnTimer(),
                millsec, 
                millsec, 
                TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public void EventKillTimer() {
        try {
            scheduler.awaitTermination(2, TimeUnit.MILLISECONDS);
            return;
        } catch (InterruptedException ex) {            
            logger.error(ex.getMessage(), ex);
        }
        //just in case
        try {
            scheduler.shutdownNow();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void ExpertRemove() {
        MainGUI.removeExpert(this);
    }

    @Override
    public void Alert(String msg) {
        //TODO
    }

    @Override
    public void PlaySound(String alertwav) {
        //TODO
    }

    @Override
    public void MessageBox(String msg) {
        JOptionPane.showMessageDialog(MainGUI.getComponent(), msg);
    }

    @Override
    public long TimeLocal() {
        return new Date().getTime();
    }

    @Override
    public long GetTickCount() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String DoubleToStr(double value, int decimal) {
        String pattern = "0.";
        for (int i = 0; i < decimal; i++) {
            pattern += "0";
        }
        decimalFormat.applyPattern(pattern);
        return decimalFormat.format(value);
    }

    @Override
    public void SendNotification(String str) {
        //TODO
    }

    @Override
    public double Close(int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double High(int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double Low(int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double iClose(String symbol, int timeframe, int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double iOpen(String symbol, int timeframe, int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double iLow(String symbol, int timeframe, int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double iHigh(String symbol, int timeframe, int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int iHighest(String symbol, int timeframe, int mode, int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int iLowest(String symbol, int timeframe, int mode, int shift) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int Period() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean IsStopped() {
        return isStop;
    }

    @Override
    public void MessageBox(String msg, String title, int code) {
        JOptionPane.showMessageDialog(MainGUI.getComponent(), msg, title, code);
    }

    @Override
    public String LongToString(long value) {
        return Long.toString(value);
    }

    @Override
    public String LongToString(long value, int str_len) {
        return String.valueOf(value).substring(0, str_len);
    }

    @Override
    public String IntegerToString(int value) {
        return String.valueOf(value);
    }

    @Override
    public String IntegerToString(int value, int str_len) {
        return String.valueOf(value).substring(0, str_len);
    }

    @Override
    public String DoubleToString(double value) {
        return String.valueOf(value);
    }

    @Override
    public String DoubleToString(double value, int decimal) {
        return DoubleToStr(value, decimal);
    }

    @Override
    public int StringToInteger(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public double StringToDouble(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public char StringGetChar(String str, int index) {
        return str.charAt(index);
    }

    @Override
    public int StringSplit(String str, char ch, String[] split) {
        String strCh = "" + ch;
        String[] arr = str.split(strCh);

        //split = str.split(strCh); //assigning like this does not work in Java to keep the reference 
        return split.length;
    }

    @Override
    public void StringReplace(String str, String search, String replacement) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int StringFind(String str, String search, int from_index) {
        return str.indexOf(search, from_index);
    }

    @Override
    public int StringFind(String str, String search) {
        return str.indexOf(search);
    }

    @Override
    public int StringToUpper(String str, String search, int from_index) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String CharArrayToString(char[] arr) {
        return new String(arr);
    }

    @Override
    public void TerminalClose(int reason) {
        ((MainGUI) MainGUI.getComponent()).dispose();
    }

    @Override
    public long AccountInfoInteger(int code) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int AccountInfoIntegerInt(int code) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean TerminalInfoInteger(int code) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void Sleep(int delay) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void RefreshRates() {
        //TODO
    }

    @Override
    public int ArraySize(String[] arr) {
        return arr.length;
    }

    @Override
    public int ArraySize(long[] arr) {
        return arr.length;
    }

    @Override
    public int ArraySize(int[] arr) {
        return arr.length;
    }

    @Override
    public int ArraySize(double[] arr) {
        return arr.length;
    }

    @Override
    public int ArrayResize(long[] arr, int new_size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int ArrayResize(int[] arr, int new_size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int ArrayResize(double[] arr, int new_size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int ArrayResize(char[] arr, int new_size) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void ArrayCopy(double[] from, double[] to) {
        for (int i = 0; i < from.length; i++) {
            if (i < to.length) {
                to[i] = from[i];
            }
        }
    }

    @Override
    public void ArrayCopy(long[] from, long[] to) {
        for (int i = 0; i < from.length; i++) {
            if (i < to.length) {
                to[i] = from[i];
            }
        }
    }

    @Override
    public void ArrayCopy(int[] from, int[] to) {
        for (int i = 0; i < from.length; i++) {
            if (i < to.length) {
                to[i] = from[i];
            }
        }
    }

    private void SetLastError(int err_code) {
        this.lastErrorCode = err_code;
    }

}

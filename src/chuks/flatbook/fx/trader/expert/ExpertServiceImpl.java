/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.expert;

import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.OrderException;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.trader.exception.ExpertErrorDesc;
import chuks.flatbook.fx.trader.main.Activity;
import chuks.flatbook.fx.trader.main.MainGUI;
import chuks.flatbook.fx.trader.main.Timeframe;
import expert.ExpertAdvisorMQ4;
import expert.contract.IExpertService;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
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
    private Timeframe expertTimeframe;
    private int lastErrorCode;
    private boolean isStop;
    private String __PATH__ = "";
    private String __FILE__ = "";
    DecimalFormat decimalFormat = new DecimalFormat();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private String accountCompany;
    private int accountNumber;
    private String accountName;
    private String accountCurrency;

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

    void setTimeframe(Timeframe timeframe) {
        expertTimeframe = timeframe;
    }

    @Override
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
        return Activity.checkAccountMargin(symbol, type, lot_size);
    }

    @Override
    public double AccountMargin() {
        return Activity.getAccountMargin();
    }

    @Override
    public double AccountFreeMargin() {
        return Activity.getAccountFreeMargin();
    }

    @Override
    public double AccountEquity() {
        return Activity.getAccountEquity();
    }

    @Override
    public double AccountProfit() {
        return Activity.getAccountProfit();
    }

    @Override
    public double AccountStopoutLevel() {
        return Activity.getAccountStopoutLevel();
    }

    @Override
    public int AccountLeverage() {
        return Activity.getAccountLeverage();
    }

    @Override
    public double AccountBalance() {
        return Activity.getAccountBalance();
    }

    @Override
    public double AccountCredit() {
        return Activity.getAccountCredit();
    }

    @Override
    public boolean IsConnected() {
        return Activity.isIsConnected();
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
    public double NormalizeDouble(double value, int digits) {
        String strDouble = DoubleToStr(value, digits);
        return Double.parseDouble(strDouble);
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
        return selectedOrder.getTakeProfitPrice();
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
            order.setMagicNumber(magic_number);
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
    public boolean OrderClose(long ticket, double lots, double price, int slippage) {
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
                    .sendClosePosition(order.getOrderID(), lots, price, slippage);

            // Once the task is done, get the result            
            return (boolean) future.get(); //block till the task is completed
        } catch (InterruptedException | ExecutionException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    @Override
    public int OrderMagicNumber() {
        return selectedOrder.getMagicNumber();
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
    public String ErrorDescription(int error_code) {
        return ExpertErrorDesc.toString(error_code);
    }

    @Override
    public double MarketInfo(String symbol, int mode) {
        SymbolInfo symbolInfo = Activity.getSelectedSymbolInfoMap().get(symbol);
        if (symbolInfo == null) {
            SetLastError(ERR_SYMBOL_NOT_FOUND);
            return -1;
        }

        switch (mode) {
            case MODE_ASK -> {
                return symbolInfo.getAsk();
            }
            case MODE_BID -> {
                return symbolInfo.getBid();
            }
            case MODE_DIGITS -> {
                return symbolInfo.getDigits();
            }
            case MODE_TICKVALUE -> {
                return symbolInfo.getTickValue();
            }
            case MODE_TICKSIZE -> {
                return symbolInfo.getTickSize();
            }
            case MODE_LOTSIZE -> {
                return symbolInfo.getLotSize(); //lot size in the base currency
            }
            case MODE_OPEN -> {
                return symbolInfo.getOpen();// Open day price
            }
            case MODE_HIGH -> {
                return symbolInfo.getHigh();// High day price
            }
            case MODE_LOW -> {
                return symbolInfo.getLow();// Low day price
            }
            case MODE_CLOSE -> {
                return symbolInfo.getClose();// Close day price
            }
            case MODE_TIME -> {
                return symbolInfo.getTime();//The last incoming tick time
            }
            case MODE_SPREAD -> {
                return symbolInfo.getSpread();
            }
            case MODE_SWAPLONG -> {
                return symbolInfo.getSwapLong();
            }
            case MODE_SWAPSHORT -> {
                return symbolInfo.getSwapShort();
            }
            default -> {
            }
        }

        return -1;
    }

    @Override
    public void Print(Object... args) {
        MainGUI.expertLog(ExpertUtil.expertSimpleName(__FILE__),
                expertSymbol,
                expertTimeframe.getString(),
                args);
    }

    @Override
    public void PrintFormat(String str, Object... args) {
        String ftmStr = str.formatted(args);
        Print(ftmStr);
    }

    @Override
    public String StringFormat(String str, Object... args) {
        return str.formatted(args);
    }

    @Override
    public void Comment(Object... args) {

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
        return accountCompany;
    }

    @Override
    public int AccountNumber() {
        return accountNumber;
    }

    @Override
    public String AccountName() {
        return accountName;
    }

    @Override
    public String AccountCurrency() {
        return accountCurrency;
    }

    @Override
    public String TerminalPath() {
        return System.getProperty("user.dir");
    }

    @Override
    public boolean EventSetMillisecondTimer(int millsec) {
        scheduler.scheduleWithFixedDelay(
                () -> OnTimer(),
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
        MainGUI.removeExpert(this, REASON_PROGRAM);
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
    public long GetTickCount() {// not exactly as mql4
        return System.currentTimeMillis();// not exactly as mql4
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
    public long Time(int shift) {
        return Activity.getCandleTime(expertSymbol, expertTimeframe.getInt(), shift);
    }

    @Override
    public int Volume(int shift) {
        return Activity.getCandleVolume(expertSymbol, expertTimeframe.getInt(), shift);
    }

    @Override
    public double Open(int shift) {
        return Activity.getCandleOpen(expertSymbol, expertTimeframe.getInt(), shift);
    }

    @Override
    public double High(int shift) {
        return Activity.getCandleHigh(expertSymbol, expertTimeframe.getInt(), shift);
    }

    @Override
    public double Low(int shift) {
        return Activity.getCandleLow(expertSymbol, expertTimeframe.getInt(), shift);
    }

    @Override
    public double Close(int shift) {
        return Activity.getCandleClose(expertSymbol, expertTimeframe.getInt(), shift);
    }

    @Override
    public long iTime(String symbol, int timeframe, int shift) {
        return Activity.getCandleTime(symbol, timeframe, shift);
    }

    @Override
    public int iVolume(String symbol, int timeframe, int shift) {
        return Activity.getCandleVolume(symbol, timeframe, shift);
    }

    @Override
    public double iOpen(String symbol, int timeframe, int shift) {
        return Activity.getCandleOpen(symbol, timeframe, shift);
    }

    @Override
    public double iHigh(String symbol, int timeframe, int shift) {
        return Activity.getCandleHigh(symbol, timeframe, shift);
    }

    @Override
    public double iLow(String symbol, int timeframe, int shift) {
        return Activity.getCandleLow(symbol, timeframe, shift);
    }

    @Override
    public double iClose(String symbol, int timeframe, int shift) {
        return Activity.getCandleClose(symbol, timeframe, shift);
    }

    @Override
    public int iHighest(String symbol, int timeframe, int type, int count) {
        return iHighest(symbol, timeframe, type, count, 0);
    }

    @Override
    public int iHighest(String symbol, int timeframe, int type, int count, int start) {
        double value = Double.MIN_VALUE;
        int shift = start;
        for (int i = start; i < start + count; i++) {
            double val = 0;
            switch (type) {
                case MODE_OPEN ->
                    val = Activity.getCandleOpen(symbol, timeframe, start);
                case MODE_HIGH ->
                    val = Activity.getCandleHigh(symbol, timeframe, start);
                case MODE_LOW ->
                    val = Activity.getCandleLow(symbol, timeframe, start);
                case MODE_CLOSE ->
                    val = Activity.getCandleClose(symbol, timeframe, start);
                case MODE_TIME ->
                    val = Activity.getCandleTime(symbol, timeframe, start);
                case MODE_VOLUME ->
                    val = Activity.getCandleVolume(symbol, timeframe, start);
                default -> {
                }
            }
            if (val > value) {
                value = val;
                shift = i;
            }
        }

        return shift;
    }

    @Override
    public int iLowest(String symbol, int timeframe, int type, int count) {
        return iLowest(symbol, timeframe, type, count, 0);
    }

    @Override
    public int iLowest(String symbol, int timeframe, int type, int count, int start) {
        double value = Double.MAX_VALUE;
        int shift = start;
        for (int i = start; i < start + count; i++) {
            double val = 0;
            switch (type) {
                case MODE_OPEN ->
                    val = Activity.getCandleOpen(symbol, timeframe, start);
                case MODE_HIGH ->
                    val = Activity.getCandleHigh(symbol, timeframe, start);
                case MODE_LOW ->
                    val = Activity.getCandleLow(symbol, timeframe, start);
                case MODE_CLOSE ->
                    val = Activity.getCandleClose(symbol, timeframe, start);
                case MODE_TIME ->
                    val = Activity.getCandleTime(symbol, timeframe, start);
                case MODE_VOLUME ->
                    val = Activity.getCandleVolume(symbol, timeframe, start);
                default -> {
                }
            }
            if (val < value) {
                value = val;
                shift = i;
            }
        }

        return shift;
    }

    @Override
    public int Period() {
        return expertTimeframe.getInt();
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
    public String[] StringSplit(String str, char ch) {
        String strCh = "" + ch;
        String[] arr = str.split(strCh);
        return arr;
    }

    @Override
    public String StringReplace(String str, String search, String replacement) {
        return str.replace(search, replacement);
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
    public String StringToUpper(String str) {
        return str.toUpperCase();
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
    public int AccountInfoInteger(int code) {
        switch (code) {
            case ACCOUNT_LOGIN -> {
                return AccountNumber();//come back to check for correctness
            }
            case ACCOUNT_LEVERAGE -> {
                return AccountLeverage();
            }
            case ACCOUNT_TRADE_ALLOWED -> {
                return IsTradeAllowed() ? 1 : 0;
            }
            case ACCOUNT_TRADE_EXPERT -> {
                return IsExpertEnabled() ? 1 : 0;//may not be similar behaviour with mt4
            }
            default -> {
            }
        }
        return -1;
    }

    @Override
    public int TerminalInfoInteger(int code) {
        if (code == TERMINAL_CONNECTED) {
            return IsConnected() ? 1 : 0;
        } else if (code == TERMINAL_DISCONNECTED) {
            return !IsConnected() ? 1 : 0;
        }
        //SetLastError(UNKNOWN_CODE);
        return -1;
    }

    @Override
    public void Sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExpertServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public String[] ArrayResize(String[] arr, int new_size) {
        return Arrays.copyOf(arr, new_size);
    }

    @Override
    public long[] ArrayResize(long[] arr, int new_size) {
        return Arrays.copyOf(arr, new_size);
    }

    @Override
    public int[] ArrayResize(int[] arr, int new_size) {
        return Arrays.copyOf(arr, new_size);
    }

    @Override
    public double[] ArrayResize(double[] arr, int new_size) {
        return Arrays.copyOf(arr, new_size);
    }

    @Override
    public char[] ArrayResize(char[] arr, int new_size) {
        return Arrays.copyOf(arr, new_size);
    }

    @Override
    public double[] ArrayCopy(double[] to, double[] from) {
        for (int i = 0; i < from.length; i++) {
            if (i < to.length) {
                to[i] = from[i];
            }
        }
        return to;
    }

    @Override
    public long[] ArrayCopy(long[] to, long[] from) {
        for (int i = 0; i < from.length; i++) {
            if (i < to.length) {
                to[i] = from[i];
            }
        }
        return to;
    }

    @Override
    public int[] ArrayCopy(int[] to, int[] from) {
        for (int i = 0; i < from.length; i++) {
            if (i < to.length) {
                to[i] = from[i];
            }
        }
        return to;
    }

    private void SetLastError(int err_code) {
        this.lastErrorCode = err_code;
    }

}

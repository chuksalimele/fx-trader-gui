/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chuks.flatbook.fx.trader.main;

import chuks.flatbook.fx.trader.ui.model.PendingOrderTableModel;
import chuks.flatbook.fx.trader.ui.model.OpenOrderTableModel;
import chuks.flatbook.fx.trader.ui.model.HistoryOrderTableModel;
import chuks.flatbook.fx.trader.ui.model.MarketWatchTableModel;
import chuks.flatbook.fx.trader.account.contract.TraderAccount;
//import fix.account.factory.FixFactory;
//import quickfix.ConfigError;
import com.formdev.flatlaf.FlatLightLaf;
import chuks.flatbook.fx.trader.transport.TraderAccountManager;
import chuks.flatbook.fx.common.account.order.Order;
import chuks.flatbook.fx.common.account.order.OrderException;
import chuks.flatbook.fx.common.account.order.SymbolInfo;
import chuks.flatbook.fx.trader.listener.AccountListener;
import chuks.flatbook.fx.common.listener.OrderActionAdapter;
import chuks.flatbook.fx.common.listener.OrderActionListener;
import chuks.flatbook.fx.common.listener.SymbolUpdateAdapter;
import chuks.flatbook.fx.common.listener.SymbolUpdateListener;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import chuks.flatbook.fx.common.listener.ConnectionListener;
import chuks.flatbook.fx.trader.transport.TradingClient;
import chuks.flatbook.fx.trader.ui.Toast;
import chuks.flatbook.fx.trader.ui.validate.PositiveNumericDocument;
import chuks.flatbook.fx.common.util.EmailUtils;
import chuks.flatbook.fx.common.util.SecurePasswordUtils;
import chuks.flatbook.fx.trader.config.AppConfig;
import chuks.flatbook.fx.trader.expert.ExpertManager;
import chuks.flatbook.fx.trader.expert.ExpertUtil;
import chuks.flatbook.fx.trader.ui.FileTreeCellRenderer;
import chuks.flatbook.fx.trader.ui.model.AttachededExpertModel;
import chuks.flatbook.fx.trader.ui.model.FileTreeModel;
import expert.contract.IExpertService;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.TreePath;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user
 */
public class MainGUI extends javax.swing.JFrame {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MainGUI.class.getName());
    private boolean simulate2Clicked;

    static MainGUI mainGUI;
    private final String ORDER_SENDING_WAIT_MSG = "Sending...Please wait...";
    private final String ORDER_ACTION_DELETE = "Delete";
    private final String ORDER_ACTION_CLOSE = "Close";
    private final String ORDER_ACTION_MODIFY = "Modify";
    static private Activity activity = new Activity();
    static private MarketWatchTableModel marketWatchTableModel = new MarketWatchTableModel();
    static private OpenOrderTableModel openOrderTableModel = new OpenOrderTableModel();
    static private PendingOrderTableModel pendingOrderTableModel = new PendingOrderTableModel();
    static private HistoryOrderTableModel historyOrderTableModel = new HistoryOrderTableModel();

    static private DefaultListModel<String> allSymbolsDlgSelectSymbolsModel;

    static DecimalFormat fiveDecimalFormat = new DecimalFormat("0.00000");
    static DecimalFormat threeDecimalFormat = new DecimalFormat("0.000");
    static DecimalFormat twoDecimalFormat = new DecimalFormat("0.00");

    static private TraderAccount traderAccount;
    static private TradingClient tradingClient;
    private LinkedHashMap<String, Character> orderSides = new LinkedHashMap();
    private int selectedOpenOrderTableRow = -1;
    private int selectedOpenOrderTableColumn = -1;
    private int selectedPendingOrderTableRow = -1;
    private int selectedPendingOrderTableColumn = -1;
    private Order orderSelected;
    static private boolean isConnected;
    static private ExpertManager expertManager;

    static private AccountListener accountListerner = new AccountListener() {

        void displayConnectionStatus(String status) {
            lblConnectionStatusDisplay.setText(status);
            lblConnectionStatusDisplay.setToolTipText(status);
            cmdConnectionRetry.setVisible(!tradingClient.isConnected() && tradingClient.connectionAttempts() > 1);
        }

        @Override
        public void onLoggedIn() {
            isConnected = true;
            displayConnectionStatus("Logged in");
        }

        @Override
        public void onLoggedOut() {
            isConnected = false;
            displayConnectionStatus("Logged out");
        }

        @Override
        public void onLogInFailed(String errMsg) {
            Toast.show(errMsg, 10000, mainGUI);
        }

        @Override
        public void onLogOutFailed(String errMsg) {
            Toast.show(errMsg, 10000, mainGUI);
        }

        @Override
        public void onAccountOpen(int account_number) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void onSignUpFail(String reason) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
    };

    private static ConnectionListener connectionListerner = new ConnectionListener() {

        void displayConnectionStatus(String status) {
            lblConnectionStatusDisplay.setText(status);
            lblConnectionStatusDisplay.setToolTipText(status);
            cmdConnectionRetry.setVisible(!tradingClient.isConnected() && tradingClient.connectionAttempts() > 1);
        }

        @Override
        public void onConnectionProgress(String status) {
            displayConnectionStatus(status);
        }

        @Override
        public void onConnected() {
            isConnected = true;
            displayConnectionStatus("Connected");
        }

        @Override
        public void onDisconnected(String errMsg) {
            isConnected = false;
            displayConnectionStatus(errMsg);
        }

    };

    private static OrderActionListener userDlgOrderAction;

    static class UserOrderAction extends OrderActionAdapter {

        public UserOrderAction(Component comp) {
            super(comp);
        }

        @Override
        public void onClosedMarketOrder(Order order) {
            dlgOrder.setVisible(false);
        }

        @Override
        public void onModifiedMarketOrder(Order order) {
            dlgOrder.setVisible(false);
        }

        void onOrderError(Order order, String errMsg) {
            //NOTE: order may be null
            if (!dlgOrder.isVisible()) {
                Toast.show(errMsg, 10000, this.getComponent());
            }

            cboActionDlgOrder.setEnabled(true); //enable if was disable by previous order processing
            lblResponseMsgDlgOrder.setText(errMsg);
            if (cboActionDlgOrder.getSelectedIndex() == 0) {//modify
                cmdModifyDlgOrder.setEnabled(true);
            } else if (cboActionDlgOrder.getSelectedIndex() == 1) {//close or delete
                cmdCloseDlgOrder.setEnabled(true);
            }
        }

        @Override
        public void onOrderRemoteError(Order order, String errMsg) {
            onOrderError(order, errMsg);
        }

        @Override
        public void onOrderSendFailed(Order order, String errMsg) {
            onOrderError(order, errMsg);
        }

        @Override
        public void onOrderNotAvailable(String errMsg, String message_identifier) {
            onOrderError(null, errMsg);
        }

    };

    static private SymbolUpdateListener uISymbolUpdateHandler = new SymbolUpdateAdapter() {
        @Override
        public void onGetFullRefereshSymbol(String symbolName) {
            allSymbolsDlgSelectSymbolsModel.addElement(symbolName);
        }

        @Override
        public void onSwapChange(SymbolInfo symbolInfo) {

        }

        @Override
        public void onPriceChange(SymbolInfo symbolInfo) {

            if (symbolInfo.getName().equals(cboSelectSymbolEnterTrade.getSelectedItem())) {
                lblBidEnterTrade.setText(formatPrice(symbolInfo.getBid(), symbolInfo));
                lblAskEnterTrade.setText(formatPrice(symbolInfo.getAsk(), symbolInfo));
            }

            openOrderTableModel.updateAsMarketPriceChanged(symbolInfo);
        }

        @Override
        public void onfullSymbolList(List<String> symbol_list) {

        }

        @Override
        public void onSeletedSymbolList(List<String> symbol_list) {
            cboSelectSymbolEnterTrade.removeAllItems();
            symbol_list.forEach(symb -> {
                cboSelectSymbolEnterTrade.addItem(symb);
            });
        }

    };
    static private File selectedExpertTreePath;
    private Color prevEnableAutoTradingBackground;
    private Color prevEnableAutoTradingForeground;
    private String selectedSymbolForAttachExpert;
    private static final AttachededExpertModel attachededExpertModel = new AttachededExpertModel();
    static private File selectedAttachedExpert;

    /**
     * Creates new form MainGUI
     */
    public MainGUI() {
        init();
        orderSides.put("SELECT", Order.Side.NONE);
        orderSides.put("BUY", Order.Side.BUY);
        orderSides.put("SELL", Order.Side.SELL);
        orderSides.put("BUY LIMIT", Order.Side.BUY_LIMIT);
        orderSides.put("SELL LIMIT", Order.Side.SELL_LIMIT);
        orderSides.put("BUY STOP", Order.Side.BUY_STOP);
        orderSides.put("SELL STOP", Order.Side.SELL_STOP);

        initComponents();

        makePriceSpinnerComponentsPriceCapturable();

        ((JSpinner.DefaultEditor) spnLotSizeDlgOrder
                .getEditor())
                .getTextField()
                .setDocument(new PositiveNumericDocument());

        ((JSpinner.DefaultEditor) spnLotSizeEnterTrade
                .getEditor())
                .getTextField()
                .setDocument(new PositiveNumericDocument());

        validateLotSizeInputControl(spnLotSizeDlgOrder);
        validateLotSizeInputControl(spnLotSizeEnterTrade);

        allSymbolsDlgSelectSymbolsModel = (DefaultListModel<String>) lstAllSymbolsDlgSelectSymbols.getModel();
    }

    public static Component getComponent() {
        return mainGUI;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popMnuOpenOrder = new javax.swing.JPopupMenu();
        mnuItmModifyOrder = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mnuItmCloseOrder = new javax.swing.JMenuItem();
        popMnuPendingOrder = new javax.swing.JPopupMenu();
        mnuItmModifyPendingOrder = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuItmDeletePendingOrder = new javax.swing.JMenuItem();
        dlgOrder = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        txtSymboldlgOrder = new javax.swing.JTextField();
        cmdModifyDlgOrder = new javax.swing.JButton();
        cmdCloseDlgOrder = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        spnStoplossDlgOrder = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        spnTakeProfitDlgOrder = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        cboActionDlgOrder = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        spnLotSizeDlgOrder = new javax.swing.JSpinner();
        jLabel17 = new javax.swing.JLabel();
        lblOrderIDdlgOrder = new javax.swing.JLabel();
        lblResponseMsgDlgOrder = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        spnOpenPriceDlgOrder = new javax.swing.JSpinner();
        dlgSelectSymbols = new javax.swing.JDialog();
        jLabel19 = new javax.swing.JLabel();
        cboFilterSymbolsDlgSelectSymbols = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstAllSymbolsDlgSelectSymbols = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        lstSelectedSymbolsDlgSelectSymbols = new javax.swing.JList<>();
        cmdTransferToSelectedSymbolsDlgSelectSymbols = new javax.swing.JButton();
        cmdRemoveSelectedSymbolsDlgSelectSymbols = new javax.swing.JButton();
        lblAllInstrumentsLabelDlgSelectSymbols = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cmdDoneDlgSelectSymbols = new javax.swing.JButton();
        dlgSignUp = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        txtEmailUsernameDlgSignUp = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtFirstNameDlgSignUp = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtLastNameDlgSignUp = new javax.swing.JTextField();
        pwdPasswordDlgSignUp = new javax.swing.JPasswordField();
        cmdSignUpDlgSignUp = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        pwdConfirmPasswordDlgSignUp = new javax.swing.JPasswordField();
        dlgLogin = new javax.swing.JDialog();
        jLabel21 = new javax.swing.JLabel();
        txtAccountNumberDlgLogin = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        pwdPasswordDlgLogin = new javax.swing.JPasswordField();
        cmdLoginDlgLogin = new javax.swing.JButton();
        cmdIDontHaveAnAccountDlgLogin = new javax.swing.JButton();
        chkRememberMeDlgLogin = new javax.swing.JCheckBox();
        cmdForgotPasswordDlgLogin = new javax.swing.JButton();
        popMnuExpert = new javax.swing.JPopupMenu();
        mnuRefreshExpertTree = new javax.swing.JMenuItem();
        mnuAttachExpert = new javax.swing.JMenuItem();
        mnuOpenExpertDir = new javax.swing.JMenuItem();
        popMnuRunningExpert = new javax.swing.JPopupMenu();
        mnuRemoveRunningExpert = new javax.swing.JMenuItem();
        mnuReAttachExpert = new javax.swing.JMenuItem();
        dlgExpertProperties = new javax.swing.JDialog();
        tabPaneDlgExpertProperties = new javax.swing.JTabbedPane();
        scrollPaneDlgExpertPropertiesSelectedSymbol = new javax.swing.JScrollPane();
        lstDlgExpertPropertiesSelectedSymbol = new javax.swing.JList<>();
        pnlDlgExpertPropertiesInputs = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cmdDglAttachEpert = new javax.swing.JButton();
        cmdDglCancelExpertAttach = new javax.swing.JButton();
        dlgRemoveExpertDialog = new javax.swing.JDialog();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblDlgRemoveExpert = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        cmdDlgRemoveExpertRemove = new javax.swing.JButton();
        cmdDlgRemoveExpertDone = new javax.swing.JButton();
        cmdDlgRemoveExpertSelectAll = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        tabPaneOrders = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOpenOrders = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPendingOrders = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblHistoryOrders = new javax.swing.JTable();
        panelTop = new javax.swing.JPanel();
        splitPaneTop = new javax.swing.JSplitPane();
        pnlEnterTrade = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cboSelectSymbolEnterTrade = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        spnLotSizeEnterTrade = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        spnStoplossEnterTrade = new javax.swing.JSpinner();
        spnTakeProfitEnterTrade = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboSelectSideEnterTrade = new javax.swing.JComboBox<>();
        for (Map.Entry<String, Character> entry : this.orderSides.entrySet()) {
            String side = entry.getKey();
            this.cboSelectSideEnterTrade.addItem(side);
        }
        jLabel6 = new javax.swing.JLabel();
        cmdConfirmEnterTrade = new javax.swing.JButton();
        lblBidEnterTrade = new javax.swing.JLabel();
        lblAskEnterTrade = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        spnEntryPriceForPendingOrderEnterTrade = new javax.swing.JSpinner();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblAttachedExperts = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblMarketWatch = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        treeExpertAdvisors = new javax.swing.JTree();
        toolBarFooter = new javax.swing.JToolBar();
        Status = new javax.swing.JLabel();
        lblConnectionStatusDisplay = new javax.swing.JLabel();
        cmdConnectionRetry = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        cmdEnableAutoTrading = new javax.swing.JToggleButton();
        cmdAddRemvoeSymbol = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuOpenExpertLocation = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnuLogin = new javax.swing.JMenuItem();
        mnuSignUp = new javax.swing.JMenuItem();
        mnuLogout = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnuEixt = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuOpenPositions = new javax.swing.JMenuItem();
        mnuPendingOrders = new javax.swing.JMenuItem();
        mnuHistoryOrders = new javax.swing.JMenuItem();
        mnuExpertLog = new javax.swing.JMenuItem();
        mnuAction = new javax.swing.JMenu();
        mnuAddRemoveSymbol = new javax.swing.JMenuItem();
        mnuRemoveExpert = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        mnuAbout = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();

        mnuItmModifyOrder.setText("Modify Order");
        mnuItmModifyOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmModifyOrderActionPerformed(evt);
            }
        });
        popMnuOpenOrder.add(mnuItmModifyOrder);
        popMnuOpenOrder.add(jSeparator2);

        mnuItmCloseOrder.setText("Close Order");
        mnuItmCloseOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmCloseOrderActionPerformed(evt);
            }
        });
        popMnuOpenOrder.add(mnuItmCloseOrder);

        mnuItmModifyPendingOrder.setText("Modify Order");
        mnuItmModifyPendingOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmModifyPendingOrderActionPerformed(evt);
            }
        });
        popMnuPendingOrder.add(mnuItmModifyPendingOrder);
        popMnuPendingOrder.add(jSeparator1);

        mnuItmDeletePendingOrder.setText("Delete Order");
        mnuItmDeletePendingOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmDeletePendingOrderActionPerformed(evt);
            }
        });
        popMnuPendingOrder.add(mnuItmDeletePendingOrder);

        dlgOrder.setTitle("Order");
        dlgOrder.setModal(true);
        dlgOrder.setResizable(false);

        jLabel7.setText("Symbol");

        txtSymboldlgOrder.setEnabled(false);
        txtSymboldlgOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSymboldlgOrderActionPerformed(evt);
            }
        });

        cmdModifyDlgOrder.setText("Modify");
        cmdModifyDlgOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdModifyDlgOrderActionPerformed(evt);
            }
        });

        cmdCloseDlgOrder.setText("Close / Delete");
        cmdCloseDlgOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCloseDlgOrderActionPerformed(evt);
            }
        });

        jLabel8.setText("Stoploss");

        spnStoplossDlgOrder.setModel(new javax.swing.SpinnerNumberModel(0.0d, null, null, 9.999999747378752E-5d));

        jLabel14.setText("Take profit:");

        spnTakeProfitDlgOrder.setModel(new javax.swing.SpinnerNumberModel(0.0d, null, null, 0.0d));

        jLabel15.setText("Action");

        cboActionDlgOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Modify", "Close / Delete" }));
        cboActionDlgOrder.setSelectedItem(ORDER_ACTION_MODIFY);
        cboActionDlgOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActionDlgOrderActionPerformed(evt);
            }
        });

        jLabel16.setText("Lot size:");

        spnLotSizeDlgOrder.setModel(new javax.swing.SpinnerNumberModel(0.009999999776482582d, null, null, 0.10000000149011612d));

        jLabel17.setText("Order ID");

        lblOrderIDdlgOrder.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        lblOrderIDdlgOrder.setText("#102334");

        lblResponseMsgDlgOrder.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        lblResponseMsgDlgOrder.setText("Waiting Message");

        jLabel24.setText("Open price");

        spnOpenPriceDlgOrder.setModel(new javax.swing.SpinnerNumberModel(0.0d, null, null, 0.0d));

        javax.swing.GroupLayout dlgOrderLayout = new javax.swing.GroupLayout(dlgOrder.getContentPane());
        dlgOrder.getContentPane().setLayout(dlgOrderLayout);
        dlgOrderLayout.setHorizontalGroup(
            dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgOrderLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmdModifyDlgOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdCloseDlgOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dlgOrderLayout.createSequentialGroup()
                        .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblResponseMsgDlgOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblOrderIDdlgOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(dlgOrderLayout.createSequentialGroup()
                                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtSymboldlgOrder, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboActionDlgOrder, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spnStoplossDlgOrder, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(56, 56, 56)
                                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(dlgOrderLayout.createSequentialGroup()
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnLotSizeDlgOrder))
                                    .addGroup(dlgOrderLayout.createSequentialGroup()
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnTakeProfitDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(dlgOrderLayout.createSequentialGroup()
                                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spnOpenPriceDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(20, 20, 20))
        );
        dlgOrderLayout.setVerticalGroup(
            dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgOrderLayout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(lblOrderIDdlgOrder))
                .addGap(20, 20, 20)
                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(spnOpenPriceDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtSymboldlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(spnStoplossDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(spnTakeProfitDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(dlgOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(cboActionDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(spnLotSizeDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(lblResponseMsgDlgOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(cmdModifyDlgOrder)
                .addGap(30, 30, 30)
                .addComponent(cmdCloseDlgOrder)
                .addGap(28, 28, 28))
        );

        dlgSelectSymbols.setTitle("Instruments");
        dlgSelectSymbols.setModal(true);
        dlgSelectSymbols.setResizable(false);
        dlgSelectSymbols.setSize(new java.awt.Dimension(400, 500));

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Filter: ");

        cboFilterSymbolsDlgSelectSymbols.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Instruments", "Forex Majors", "Forex Minors", "Metals", "Oil And Gas", "Indices", " " }));
        cboFilterSymbolsDlgSelectSymbols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFilterSymbolsDlgSelectSymbolsActionPerformed(evt);
            }
        });

        lstAllSymbolsDlgSelectSymbols.setModel(new DefaultListModel<>());
        lstAllSymbolsDlgSelectSymbols.setFixedCellWidth(140);
        lstAllSymbolsDlgSelectSymbols.setMaximumSize(new java.awt.Dimension(50, 90));
        jScrollPane1.setViewportView(lstAllSymbolsDlgSelectSymbols);

        lstSelectedSymbolsDlgSelectSymbols.setModel(new DefaultListModel<>());
        lstSelectedSymbolsDlgSelectSymbols.setFixedCellWidth(140);
        lstSelectedSymbolsDlgSelectSymbols.setMaximumSize(new java.awt.Dimension(50, 90));
        jScrollPane6.setViewportView(lstSelectedSymbolsDlgSelectSymbols);

        cmdTransferToSelectedSymbolsDlgSelectSymbols.setText(">>");
        cmdTransferToSelectedSymbolsDlgSelectSymbols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTransferToSelectedSymbolsDlgSelectSymbolsActionPerformed(evt);
            }
        });

        cmdRemoveSelectedSymbolsDlgSelectSymbols.setText("Remove");
        cmdRemoveSelectedSymbolsDlgSelectSymbols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveSelectedSymbolsDlgSelectSymbolsActionPerformed(evt);
            }
        });

        lblAllInstrumentsLabelDlgSelectSymbols.setText(" All Instruments");

        jLabel20.setText("Selected Instruments");

        cmdDoneDlgSelectSymbols.setText("Done");
        cmdDoneDlgSelectSymbols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDoneDlgSelectSymbolsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgSelectSymbolsLayout = new javax.swing.GroupLayout(dlgSelectSymbols.getContentPane());
        dlgSelectSymbols.getContentPane().setLayout(dlgSelectSymbolsLayout);
        dlgSelectSymbolsLayout.setHorizontalGroup(
            dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboFilterSymbolsDlgSelectSymbols, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                                .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblAllInstrumentsLabelDlgSelectSymbols, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1))
                                .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                        .addComponent(cmdTransferToSelectedSymbolsDlgSelectSymbols)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                        .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(jScrollPane6)))
                                    .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cmdRemoveSelectedSymbolsDlgSelectSymbols))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgSelectSymbolsLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdDoneDlgSelectSymbols)))
                .addGap(10, 10, 10))
        );
        dlgSelectSymbolsLayout.setVerticalGroup(
            dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboFilterSymbolsDlgSelectSymbols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdDoneDlgSelectSymbols)
                .addGap(8, 8, 8)
                .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(cmdTransferToSelectedSymbolsDlgSelectSymbols)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                        .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAllInstrumentsLabelDlgSelectSymbols)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dlgSelectSymbolsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dlgSelectSymbolsLayout.createSequentialGroup()
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(cmdRemoveSelectedSymbolsDlgSelectSymbols)
                                .addGap(136, 136, 136))
                            .addComponent(jScrollPane1))
                        .addContainerGap(12, Short.MAX_VALUE))))
        );

        dlgSignUp.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgSignUp.setTitle("Sign Up");
        dlgSignUp.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jLabel11.setText("Email");

        txtEmailUsernameDlgSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailUsernameDlgSignUpActionPerformed(evt);
            }
        });

        jLabel12.setText("Password");

        jLabel13.setText("First Name");

        jLabel18.setText("Last Name");

        cmdSignUpDlgSignUp.setText("Sign Up");
        cmdSignUpDlgSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSignUpDlgSignUpActionPerformed(evt);
            }
        });

        jLabel23.setText("Confrim Password");

        javax.swing.GroupLayout dlgSignUpLayout = new javax.swing.GroupLayout(dlgSignUp.getContentPane());
        dlgSignUp.getContentPane().setLayout(dlgSignUpLayout);
        dlgSignUpLayout.setHorizontalGroup(
            dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSignUpLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgSignUpLayout.createSequentialGroup()
                        .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwdPasswordDlgSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmdSignUpDlgSignUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pwdConfirmPasswordDlgSignUp, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                        .addGap(14, 14, 14))
                    .addGroup(dlgSignUpLayout.createSequentialGroup()
                        .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtLastNameDlgSignUp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addComponent(txtEmailUsernameDlgSignUp, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFirstNameDlgSignUp))
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        dlgSignUpLayout.setVerticalGroup(
            dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSignUpLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFirstNameDlgSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtLastNameDlgSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmailUsernameDlgSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(pwdPasswordDlgSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgSignUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(pwdConfirmPasswordDlgSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(cmdSignUpDlgSignUp)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        dlgLogin.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgLogin.setTitle("Login");
        dlgLogin.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jLabel21.setText("Account Number");

        jLabel22.setText("Password");

        cmdLoginDlgLogin.setText("Login");
        cmdLoginDlgLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoginDlgLoginActionPerformed(evt);
            }
        });

        cmdIDontHaveAnAccountDlgLogin.setText("I don't have an account");

        chkRememberMeDlgLogin.setText("Remember me");
        chkRememberMeDlgLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRememberMeDlgLoginActionPerformed(evt);
            }
        });

        cmdForgotPasswordDlgLogin.setText("Forgot Password");

        javax.swing.GroupLayout dlgLoginLayout = new javax.swing.GroupLayout(dlgLogin.getContentPane());
        dlgLogin.getContentPane().setLayout(dlgLoginLayout);
        dlgLoginLayout.setHorizontalGroup(
            dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgLoginLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmdForgotPasswordDlgLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(chkRememberMeDlgLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdIDontHaveAnAccountDlgLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(cmdLoginDlgLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAccountNumberDlgLogin)
                    .addComponent(pwdPasswordDlgLogin))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        dlgLoginLayout.setVerticalGroup(
            dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgLoginLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtAccountNumberDlgLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(pwdPasswordDlgLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(chkRememberMeDlgLogin)
                .addGap(30, 30, 30)
                .addComponent(cmdLoginDlgLogin)
                .addGap(18, 18, 18)
                .addComponent(cmdIDontHaveAnAccountDlgLogin)
                .addGap(18, 18, 18)
                .addComponent(cmdForgotPasswordDlgLogin)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        mnuRefreshExpertTree.setText("Refresh");
        mnuRefreshExpertTree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRefreshExpertTreeActionPerformed(evt);
            }
        });
        popMnuExpert.add(mnuRefreshExpertTree);

        mnuAttachExpert.setText("Attach");
        mnuAttachExpert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAttachExpertActionPerformed(evt);
            }
        });
        popMnuExpert.add(mnuAttachExpert);

        mnuOpenExpertDir.setText("Open Location");
        mnuOpenExpertDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenExpertDirActionPerformed(evt);
            }
        });
        popMnuExpert.add(mnuOpenExpertDir);

        mnuRemoveRunningExpert.setText("Remove");
        mnuRemoveRunningExpert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRemoveRunningExpertActionPerformed(evt);
            }
        });
        popMnuRunningExpert.add(mnuRemoveRunningExpert);

        mnuReAttachExpert.setText("Re-Attach");
        mnuReAttachExpert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReAttachExpertActionPerformed(evt);
            }
        });
        popMnuRunningExpert.add(mnuReAttachExpert);

        dlgExpertProperties.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgExpertProperties.setTitle("Expert Properties");
        dlgExpertProperties.setModal(true);
        dlgExpertProperties.getContentPane().setLayout(new javax.swing.BoxLayout(dlgExpertProperties.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        lstDlgExpertPropertiesSelectedSymbol.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "EURUSD", "GBPUSD", "USDJPY" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstDlgExpertPropertiesSelectedSymbol.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPaneDlgExpertPropertiesSelectedSymbol.setViewportView(lstDlgExpertPropertiesSelectedSymbol);

        tabPaneDlgExpertProperties.addTab("Choose Symbol", scrollPaneDlgExpertPropertiesSelectedSymbol);

        javax.swing.GroupLayout pnlDlgExpertPropertiesInputsLayout = new javax.swing.GroupLayout(pnlDlgExpertPropertiesInputs);
        pnlDlgExpertPropertiesInputs.setLayout(pnlDlgExpertPropertiesInputsLayout);
        pnlDlgExpertPropertiesInputsLayout.setHorizontalGroup(
            pnlDlgExpertPropertiesInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        pnlDlgExpertPropertiesInputsLayout.setVerticalGroup(
            pnlDlgExpertPropertiesInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 225, Short.MAX_VALUE)
        );

        tabPaneDlgExpertProperties.addTab("Inputs", pnlDlgExpertPropertiesInputs);

        dlgExpertProperties.getContentPane().add(tabPaneDlgExpertProperties);

        jPanel3.setPreferredSize(new java.awt.Dimension(495, 40));

        cmdDglAttachEpert.setText("Attach");
        cmdDglAttachEpert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDglAttachEpertActionPerformed(evt);
            }
        });

        cmdDglCancelExpertAttach.setText("Cancel");
        cmdDglCancelExpertAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDglCancelExpertAttachActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(260, Short.MAX_VALUE)
                .addComponent(cmdDglAttachEpert)
                .addGap(74, 74, 74)
                .addComponent(cmdDglCancelExpertAttach)
                .addGap(17, 17, 17))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdDglAttachEpert)
                    .addComponent(cmdDglCancelExpertAttach))
                .addContainerGap())
        );

        dlgExpertProperties.getContentPane().add(jPanel3);

        dlgRemoveExpertDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlgRemoveExpertDialog.setTitle("Remove Expert");
        dlgRemoveExpertDialog.setName("dlgRemoveExpert"); // NOI18N
        dlgRemoveExpertDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                dlgRemoveExpertDialogWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dlgRemoveExpertDialogWindowClosing(evt);
            }
        });
        dlgRemoveExpertDialog.getContentPane().setLayout(new javax.swing.BoxLayout(dlgRemoveExpertDialog.getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        tblDlgRemoveExpert.setModel(attachededExpertModel);
        jScrollPane12.setViewportView(tblDlgRemoveExpert);
        tblDlgRemoveExpert
        .getColumn(attachededExpertModel.symbol)
        .setMaxWidth(70);

        dlgRemoveExpertDialog.getContentPane().add(jScrollPane12);

        jPanel4.setPreferredSize(new java.awt.Dimension(90, 300));

        cmdDlgRemoveExpertRemove.setText("Remove");
        cmdDlgRemoveExpertRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDlgRemoveExpertRemoveActionPerformed(evt);
            }
        });

        cmdDlgRemoveExpertDone.setText("Done");
        cmdDlgRemoveExpertDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDlgRemoveExpertDoneActionPerformed(evt);
            }
        });

        cmdDlgRemoveExpertSelectAll.setText("Select All");
        cmdDlgRemoveExpertSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDlgRemoveExpertSelectAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmdDlgRemoveExpertSelectAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdDlgRemoveExpertRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdDlgRemoveExpertDone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(cmdDlgRemoveExpertDone)
                .addGap(25, 25, 25)
                .addComponent(cmdDlgRemoveExpertRemove)
                .addGap(25, 25, 25)
                .addComponent(cmdDlgRemoveExpertSelectAll)
                .addContainerGap(224, Short.MAX_VALUE))
        );

        dlgRemoveExpertDialog.getContentPane().add(jPanel4);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FIX Client Platform");
        setBounds(new java.awt.Rectangle(0, 0, 400, 600));
        setLocation(new java.awt.Point(0, 0));
        setSize(new java.awt.Dimension(600, 400));

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tblOpenOrders.setModel(this.openOrderTableModel);
        this.openOrderTableModel.setCellRender(this.tblOpenOrders);
        tblOpenOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOpenOrdersMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblOpenOrdersMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblOpenOrdersMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblOpenOrders);

        tabPaneOrders.addTab("Open Positions", jScrollPane2);

        tblPendingOrders.setModel(this.pendingOrderTableModel);
        this.pendingOrderTableModel.setCellRender(this.tblPendingOrders);
        tblPendingOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPendingOrdersMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPendingOrdersMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblPendingOrdersMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblPendingOrders);

        tabPaneOrders.addTab("Pending Orders", jScrollPane3);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Time", "Messages"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable1);

        tabPaneOrders.addTab("Expert Log", jScrollPane7);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Time", "Messages"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable2);

        tabPaneOrders.addTab("Jornal", jScrollPane8);

        tblHistoryOrders.setModel(this.historyOrderTableModel);
        this.historyOrderTableModel.setCellRender(this.tblHistoryOrders);
        jScrollPane4.setViewportView(tblHistoryOrders);

        tabPaneOrders.addTab("History Orders", jScrollPane4);

        jSplitPane2.setBottomComponent(tabPaneOrders);

        panelTop.setLayout(new javax.swing.BoxLayout(panelTop, javax.swing.BoxLayout.LINE_AXIS));

        splitPaneTop.setDividerLocation(250);

        jLabel1.setText("Symbol:");

        cboSelectSymbolEnterTrade.setModel(new DefaultComboBoxModel<>());
        cboSelectSymbolEnterTrade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SelectSymbolReceivedFocus(evt);
            }
        });
        cboSelectSymbolEnterTrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboSelectSymbolEnterTradeMouseClicked(evt);
            }
        });
        cboSelectSymbolEnterTrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSelectSymbolEnterTradeActionPerformed(evt);
            }
        });

        jLabel2.setText("Lot Size:");

        spnLotSizeEnterTrade.setModel(new javax.swing.SpinnerNumberModel(0.009999999776482582d, 0.009999999776482582d, 100000.0d, 0.10000000149011612d));
        spnLotSizeEnterTrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                spnLotSizeEnterTradeMouseClicked(evt);
            }
        });

        jLabel3.setText("Stop Loss:");

        spnStoplossEnterTrade.setModel(new javax.swing.SpinnerNumberModel(0.0d, null, null, 0.0d));
        spnStoplossEnterTrade.setEditor(new javax.swing.JSpinner.NumberEditor(spnStoplossEnterTrade, ""));

        spnTakeProfitEnterTrade.setModel(new javax.swing.SpinnerNumberModel(0.0d, null, null, 0.0d));
        spnTakeProfitEnterTrade.setEditor(new javax.swing.JSpinner.NumberEditor(spnTakeProfitEnterTrade, ""));

        jLabel4.setText("Take Profit:");

        jLabel5.setText("Side:");

        cboSelectSideEnterTrade.setName(""); // NOI18N
        cboSelectSideEnterTrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSelectSideEnterTradeActionPerformed(evt);
            }
        });

        jLabel6.setText("At price:");

        cmdConfirmEnterTrade.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        cmdConfirmEnterTrade.setText("Confirm");
        cmdConfirmEnterTrade.setPreferredSize(new java.awt.Dimension(72, 30));
        cmdConfirmEnterTrade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConfirmEnterTradeActionPerformed(evt);
            }
        });

        lblBidEnterTrade.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        lblBidEnterTrade.setText("0");

        lblAskEnterTrade.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        lblAskEnterTrade.setText("0");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        jLabel9.setText("/");

        spnEntryPriceForPendingOrderEnterTrade.setModel(new javax.swing.SpinnerNumberModel(0.0d, null, null, 0.0d));
        spnEntryPriceForPendingOrderEnterTrade.setEditor(new javax.swing.JSpinner.NumberEditor(spnEntryPriceForPendingOrderEnterTrade, ""));
        spnEntryPriceForPendingOrderEnterTrade.setEnabled(false);

        tblAttachedExperts.setModel(attachededExpertModel);
        tblAttachedExperts.setRowHeight(30);
        tblAttachedExperts.setRowMargin(2);
        tblAttachedExperts
        .getColumn(attachededExpertModel.symbol)
        .setMaxWidth(70);
        tblAttachedExperts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAttachedExperts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAttachedExperts.getTableHeader().setResizingAllowed(false);
        tblAttachedExperts.getTableHeader().setReorderingAllowed(false);
        tblAttachedExperts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAttachedExpertsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblAttachedExpertsMouseReleased(evt);
            }
        });
        jScrollPane10.setViewportView(tblAttachedExperts);

        javax.swing.GroupLayout pnlEnterTradeLayout = new javax.swing.GroupLayout(pnlEnterTrade);
        pnlEnterTrade.setLayout(pnlEnterTradeLayout);
        pnlEnterTradeLayout.setHorizontalGroup(
            pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                                .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(spnStoplossEnterTrade)
                                        .addComponent(cboSelectSideEnterTrade, 0, 101, Short.MAX_VALUE))
                                    .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                                        .addComponent(lblBidEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel9)))
                                .addGap(18, 18, 18)
                                .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(50, 50, 50)
                                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(spnTakeProfitEnterTrade, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                            .addComponent(spnEntryPriceForPendingOrderEnterTrade)))
                                    .addComponent(lblAskEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cmdConfirmEnterTrade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                                .addComponent(spnLotSizeEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(261, 261, 261))
                            .addComponent(cboSelectSymbolEnterTrade, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(203, 203, 203)))
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlEnterTradeLayout.setVerticalGroup(
            pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEnterTradeLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboSelectSymbolEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(8, 8, 8)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnLotSizeEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnStoplossEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(spnTakeProfitEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSelectSideEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(spnEntryPriceForPendingOrderEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(40, 40, 40)
                        .addGroup(pnlEnterTradeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBidEnterTrade)
                            .addComponent(jLabel9)
                            .addComponent(lblAskEnterTrade))
                        .addGap(4, 4, 4)
                        .addComponent(cmdConfirmEnterTrade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(164, 164, 164))
        );

        splitPaneTop.setRightComponent(pnlEnterTrade);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        tblMarketWatch.setModel(this.marketWatchTableModel);
        this.marketWatchTableModel.setCellRender(this.tblMarketWatch);
        jScrollPane5.setViewportView(tblMarketWatch);
        tblMarketWatch.getAccessibleContext().setAccessibleName("marketWatchTable");
        tblMarketWatch.getAccessibleContext().setAccessibleDescription("");

        jPanel1.add(jScrollPane5);

        jTabbedPane2.addTab("Match Watch", jPanel1);

        treeExpertAdvisors.setModel(new FileTreeModel(AppConfig.EXPERTS_DIR));
        treeExpertAdvisors.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeExpertAdvisorsMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                treeExpertAdvisorsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                treeExpertAdvisorsMouseReleased(evt);
            }
        });
        jScrollPane9.setViewportView(treeExpertAdvisors);
        treeExpertAdvisors.setCellRenderer(new FileTreeCellRenderer());

        jTabbedPane2.addTab("Expert Advisors", jScrollPane9);

        splitPaneTop.setLeftComponent(jTabbedPane2);

        panelTop.add(splitPaneTop);

        jSplitPane2.setLeftComponent(panelTop);

        toolBarFooter.setRollover(true);
        toolBarFooter.setMargin(new java.awt.Insets(0, 10, 0, 10));

        Status.setText("Status:  ");
        Status.setToolTipText("");
        toolBarFooter.add(Status);
        toolBarFooter.add(lblConnectionStatusDisplay);

        cmdConnectionRetry.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        cmdConnectionRetry.setForeground(java.awt.SystemColor.textHighlight);
        cmdConnectionRetry.setText("Retry");
        cmdConnectionRetry.setFocusable(false);
        cmdConnectionRetry.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdConnectionRetry.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdConnectionRetry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdConnectionRetryActionPerformed(evt);
            }
        });
        toolBarFooter.add(cmdConnectionRetry);
        toolBarFooter.add(jSeparator3);

        cmdEnableAutoTrading.setText("Auto Trading");
        cmdEnableAutoTrading.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEnableAutoTradingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(cmdEnableAutoTrading, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(cmdEnableAutoTrading)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        cmdAddRemvoeSymbol.setText("Add / Remove Symbol");
        cmdAddRemvoeSymbol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddRemvoeSymbolActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        mnuOpenExpertLocation.setText("Open Expert Location");
        mnuOpenExpertLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenExpertLocationActionPerformed(evt);
            }
        });
        jMenu1.add(mnuOpenExpertLocation);
        jMenu1.add(jSeparator4);

        mnuLogin.setText("Login");
        mnuLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLoginActionPerformed(evt);
            }
        });
        jMenu1.add(mnuLogin);

        mnuSignUp.setText("Sign Up");
        mnuSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSignUpActionPerformed(evt);
            }
        });
        jMenu1.add(mnuSignUp);

        mnuLogout.setText("Logout");
        jMenu1.add(mnuLogout);
        jMenu1.add(jSeparator5);

        mnuEixt.setText("Exit");
        jMenu1.add(mnuEixt);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("View");

        mnuOpenPositions.setText("Open Positions");
        jMenu2.add(mnuOpenPositions);

        mnuPendingOrders.setText("Pending Orders");
        jMenu2.add(mnuPendingOrders);

        mnuHistoryOrders.setText("History Orders");
        jMenu2.add(mnuHistoryOrders);

        mnuExpertLog.setText("Expert Log");
        jMenu2.add(mnuExpertLog);

        jMenuBar1.add(jMenu2);

        mnuAction.setText("Action");

        mnuAddRemoveSymbol.setText("Add / Remve Symbols");
        mnuAddRemoveSymbol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAddRemoveSymbolActionPerformed(evt);
            }
        });
        mnuAction.add(mnuAddRemoveSymbol);

        mnuRemoveExpert.setText("Remove Expert");
        mnuRemoveExpert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRemoveExpertActionPerformed(evt);
            }
        });
        mnuAction.add(mnuRemoveExpert);

        jMenuBar1.add(mnuAction);

        jMenu4.setText("Help");

        mnuAbout.setText("How To Use");
        jMenu4.add(mnuAbout);

        jMenuItem11.setText("About");
        jMenu4.add(jMenuItem11);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
                    .addComponent(toolBarFooter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdAddRemvoeSymbol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdAddRemvoeSymbol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(toolBarFooter, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cboSelectSymbolEnterTradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSelectSymbolEnterTradeActionPerformed

        SymbolInfo symbolInfo = traderAccount.getSymbolInfo((String) cboSelectSymbolEnterTrade.getSelectedItem());

        if (symbolInfo != null) {
            spnStoplossEnterTrade.setValue(symbolInfo.getBid());
            spnTakeProfitEnterTrade.setValue(symbolInfo.getBid());

            lblBidEnterTrade.setText(formatPrice(symbolInfo.getBid(), symbolInfo));
            lblAskEnterTrade.setText(formatPrice(symbolInfo.getAsk(), symbolInfo));

        } else {
            spnStoplossEnterTrade.setValue(0);
            spnTakeProfitEnterTrade.setValue(0);
            lblBidEnterTrade.setText("-");
            lblAskEnterTrade.setText("-");
        }


    }//GEN-LAST:event_cboSelectSymbolEnterTradeActionPerformed

    private void cmdConfirmEnterTradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdConfirmEnterTradeActionPerformed

        if (!traderAccount.isLoggedIn()) {
            JOptionPane.showMessageDialog(panelTop, "Check your connection.");
            return;
        }

        try {
            String symbol = (String) cboSelectSymbolEnterTrade.getSelectedItem();
            double lot_size = Double.parseDouble(this.spnLotSizeEnterTrade.getValue().toString());
            double stoploss = Double.parseDouble(this.spnStoplossEnterTrade.getValue().toString());
            double take_profit = Double.parseDouble(this.spnTakeProfitEnterTrade.getValue().toString());
            double entry_price_for_pend_order = Double.parseDouble(this.spnEntryPriceForPendingOrderEnterTrade.getValue().toString());
            String side = (String) this.cboSelectSideEnterTrade.getSelectedItem();

            if (symbol == null || symbol.equals("")) {
                JOptionPane.showMessageDialog(panelTop, "No symbol selected!");
                return;
            }
            if (lot_size == 0) {
                JOptionPane.showMessageDialog(panelTop, "Lot size must be greater than zero!");
                return;
            }
            if (side == null || side.equals("") || side.equals("SELECT")) {
                JOptionPane.showMessageDialog(panelTop, "No side selected!");
                return;
            }

            Object[] options = {"Ok Send", "Cancel"};

            // Show the confirm dialog
            int option = JOptionPane.showOptionDialog(
                    this,
                    formatOrderPrepared(symbol,
                            lot_size,
                            stoploss,
                            take_profit,
                            entry_price_for_pend_order,
                            side),
                    "Confirm Send",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            // Handle the user's choice
            if (option == JOptionPane.NO_OPTION) {
                return;
            }

            SymbolInfo symbolInfo = traderAccount.getSymbolInfo(symbol);

            if (symbolInfo == null) {
                return;
            }

            Order order = new Order(symbolInfo,
                    (char) orderSides.get(side),
                    take_profit,
                    stoploss);

            order.setLotSize(lot_size);

            if ("BUY".equals(side) || "SELL".equals(side)) {
                traderAccount.sendMarketOrder(order);
            } else {
                traderAccount.placePendingOrder(order);
            }

        } catch (OrderException ex) {
            JOptionPane.showMessageDialog(panelTop, ex.getMessage(), "Invalid Order", JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage(), ex);
        }

    }//GEN-LAST:event_cmdConfirmEnterTradeActionPerformed

    String formatOrderPrepared(String symbol,
            double lot_size,
            double stoploss,
            double take_profit,
            double entry_price_for_pend_order,
            String side
    ) {

        String strOrder = "";

        if (side.equals("BUY") || side.equals("SELL")) {
            strOrder += "Market Order!";
            strOrder += "\n\n" + side + " " + lot_size + " lot of " + symbol;
            strOrder += "\nStoploss at " + stoploss;
            strOrder += "\nTake profit at " + take_profit;
        } else {
            strOrder += "Pending Order!";
            strOrder += "\n\nPlace " + side + " at " + entry_price_for_pend_order + " for " + lot_size + " lot of " + symbol;
            strOrder += "\nStoploss at " + stoploss;
            strOrder += "\nTake profit at " + take_profit;
            strOrder += "\n\nKindly note that pending orders are only stored "
                    + "locally and will be automatically cancelled if the "
                    + "platform is closed or there is a significant connection lost.";
        }

        return strOrder;
    }

    private void cboSelectSideEnterTradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSelectSideEnterTradeActionPerformed
        String side = (String) this.cboSelectSideEnterTrade.getSelectedItem();
        if (side.equals("BUY") || side.equals("SELL") || side.equals("SELECT")) {
            spnEntryPriceForPendingOrderEnterTrade.setEnabled(false);
        } else {
            spnEntryPriceForPendingOrderEnterTrade.setEnabled(true);
        }
    }//GEN-LAST:event_cboSelectSideEnterTradeActionPerformed

    private void SelectSymbolReceivedFocus(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SelectSymbolReceivedFocus
        // Clear existing items
        /*this.cboSelectSymbolEnterTrade.removeAllItems();

        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) this.cboSelectSymbolEnterTrade.getModel();
        model.removeAllElements();
        // Add new items
        String[] data = {"Apple", "Banana", "Cherry", "Date", "Elderberry"};
        for (String item : data) {
            model.addElement(item);
        }*/

    }//GEN-LAST:event_SelectSymbolReceivedFocus

    private void mnuItmModifyOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmModifyOrderActionPerformed
        modifyOpenOrderOnSelectedTableRow(null, 0, 0);
    }//GEN-LAST:event_mnuItmModifyOrderActionPerformed

    private void mnuItmDeletePendingOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmDeletePendingOrderActionPerformed
        deletePendingOrderOnSelectedTableRow(null);
    }//GEN-LAST:event_mnuItmDeletePendingOrderActionPerformed

    private void tblOpenOrdersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOpenOrdersMousePressed
        if (evt.isPopupTrigger()) {
            selectedOpenOrderTable(evt, true);
        }
    }//GEN-LAST:event_tblOpenOrdersMousePressed

    private void tblOpenOrdersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOpenOrdersMouseClicked
        if (evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt)) {
            selectedOpenOrderTable(evt, false);
        } else if (evt.getClickCount() == 2) {//double click
            selectedOpenOrderTable(evt, true);
        }
    }//GEN-LAST:event_tblOpenOrdersMouseClicked

    private void tblOpenOrdersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOpenOrdersMouseReleased
        if (evt.isPopupTrigger()) {
            selectedOpenOrderTable(evt, true);
        }
    }//GEN-LAST:event_tblOpenOrdersMouseReleased

    private void cmdAddRemvoeSymbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddRemvoeSymbolActionPerformed

        DefaultListModel<String> model = (DefaultListModel<String>) this.lstSelectedSymbolsDlgSelectSymbols.getModel();
        model.removeAllElements();
        List selecteSymbols = traderAccount.getSelectedSymbols();
        if (selecteSymbols == null) {
            return;
        }
        model.addAll(0, selecteSymbols);

        this.dlgSelectSymbols.setSize(new Dimension(440, 600));
        this.dlgSelectSymbols.setLocationRelativeTo(this);

        this.dlgSelectSymbols.setVisible(true);
    }//GEN-LAST:event_cmdAddRemvoeSymbolActionPerformed

    private void tblPendingOrdersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPendingOrdersMousePressed
        if (evt.isPopupTrigger()) {
            selectedPendingOrderTable(evt, true);
        }
    }//GEN-LAST:event_tblPendingOrdersMousePressed

    private void tblPendingOrdersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPendingOrdersMouseReleased
        if (evt.isPopupTrigger()) {
            selectedPendingOrderTable(evt, true);
        }
    }//GEN-LAST:event_tblPendingOrdersMouseReleased

    private void tblPendingOrdersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPendingOrdersMouseClicked
        if (evt.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(evt)) {
            selectedPendingOrderTable(evt, false);
        } else if (evt.getClickCount() == 2) {//double click
            selectedPendingOrderTable(evt, true);
        }

    }//GEN-LAST:event_tblPendingOrdersMouseClicked

    private void mnuItmCloseOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmCloseOrderActionPerformed
        closeOpenOrderOnSelectedTableRow(null, 0, true);
    }//GEN-LAST:event_mnuItmCloseOrderActionPerformed

    private void mnuItmModifyPendingOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmModifyPendingOrderActionPerformed
        modifyPendingOrderOnSelectedTableRow(null, 0, 0, 0);
    }//GEN-LAST:event_mnuItmModifyPendingOrderActionPerformed

    private void cboActionDlgOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActionDlgOrderActionPerformed
        orderActionSelected();
    }//GEN-LAST:event_cboActionDlgOrderActionPerformed

    private void cmdModifyDlgOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdModifyDlgOrderActionPerformed
        lblResponseMsgDlgOrder.setText(ORDER_SENDING_WAIT_MSG);

        Order order = orderSelected;
        double open_price = Double.parseDouble(this.spnOpenPriceDlgOrder.getValue().toString());
        double target_price = Double.parseDouble(this.spnTakeProfitDlgOrder.getValue().toString());
        double stoploss_price = Double.parseDouble(this.spnStoplossDlgOrder.getValue().toString());

        if (order.getSide() == Order.Side.BUY
                || order.getSide() == Order.Side.SELL) {
            modifyOpenOrderOnSelectedTableRow(order, target_price, stoploss_price);
        } else {
            modifyPendingOrderOnSelectedTableRow(order, open_price, target_price, stoploss_price);
        }

        cmdModifyDlgOrder.setEnabled(false);
        cboActionDlgOrder.setEnabled(false);
    }//GEN-LAST:event_cmdModifyDlgOrderActionPerformed

    private void cmdCloseDlgOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCloseDlgOrderActionPerformed

        Order order = orderSelected;
        double lot_size = Double.parseDouble(this.spnLotSizeDlgOrder.getValue().toString());

        if (order.getSide() == Order.Side.BUY
                || order.getSide() == Order.Side.SELL) {
            if (lot_size == 0) {
                JOptionPane.showMessageDialog(dlgOrder, "Lot size too small", "Invalid", JOptionPane.OK_OPTION);
                return;
            }
            lblResponseMsgDlgOrder.setText(ORDER_SENDING_WAIT_MSG);
            closeOpenOrderOnSelectedTableRow(order, lot_size, true);
        } else {
            lblResponseMsgDlgOrder.setText(ORDER_SENDING_WAIT_MSG);
            deletePendingOrderOnSelectedTableRow(order);
        }

        cmdCloseDlgOrder.setEnabled(false);
        cboActionDlgOrder.setEnabled(false);

    }//GEN-LAST:event_cmdCloseDlgOrderActionPerformed

    private void cmdDoneDlgSelectSymbolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDoneDlgSelectSymbolsActionPerformed
        /*
        DefaultListModel<String> model = (DefaultListModel<String>) this.lstSelectedSymbolsDlgSelectSymbols.getModel();
        List lst = new LinkedList();
        model.elements().asIterator().forEachRemaining(entry -> {
            lst.add(entry);
        });
        traderAccount.setSelectedSymbols(lst);

        if (simulate2Clicked) {//TESTING - SIMULATING
            traderAccount.simulateSetSelectedSymbols(lst);
        }

        this.dlgSelectSymbols.setVisible(false);
         */
    }//GEN-LAST:event_cmdDoneDlgSelectSymbolsActionPerformed

    private void cboFilterSymbolsDlgSelectSymbolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFilterSymbolsDlgSelectSymbolsActionPerformed
        this.lblAllInstrumentsLabelDlgSelectSymbols.setText(this.cboFilterSymbolsDlgSelectSymbols.getSelectedItem().toString());

        //TODO  - Do the actually filter
    }//GEN-LAST:event_cboFilterSymbolsDlgSelectSymbolsActionPerformed

    private void cmdTransferToSelectedSymbolsDlgSelectSymbolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTransferToSelectedSymbolsDlgSelectSymbolsActionPerformed
        List selected_symbols = this.lstAllSymbolsDlgSelectSymbols.getSelectedValuesList();
        DefaultListModel<String> lst_model = (DefaultListModel) this.lstSelectedSymbolsDlgSelectSymbols.getModel();
        int lst_size = lst_model.getSize();

        outer:
        for (int i = 0; i < selected_symbols.size(); i++) {
            String symbol = (String) selected_symbols.get(i);
            for (int k = 0; k < lst_size; k++) {
                if (symbol.equals(lst_model.getElementAt(k))) {
                    continue outer;
                }
            }
            lst_model.addElement(symbol);
        }
    }//GEN-LAST:event_cmdTransferToSelectedSymbolsDlgSelectSymbolsActionPerformed

    private void cmdRemoveSelectedSymbolsDlgSelectSymbolsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveSelectedSymbolsDlgSelectSymbolsActionPerformed
        List selected_symbols = this.lstSelectedSymbolsDlgSelectSymbols.getSelectedValuesList();
        DefaultListModel<String> lst_model = (DefaultListModel) this.lstSelectedSymbolsDlgSelectSymbols.getModel();

        for (int i = 0; i < selected_symbols.size(); i++) {
            String symbol = (String) selected_symbols.get(i);
            for (int k = 0; k < lst_model.getSize(); k++) {
                if (symbol.equals(lst_model.getElementAt(k))) {
                    lst_model.removeElement(symbol);
                    break;
                }
            }

        }
    }//GEN-LAST:event_cmdRemoveSelectedSymbolsDlgSelectSymbolsActionPerformed

    private void cboSelectSymbolEnterTradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboSelectSymbolEnterTradeMouseClicked

    }//GEN-LAST:event_cboSelectSymbolEnterTradeMouseClicked

    private void spnLotSizeEnterTradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spnLotSizeEnterTradeMouseClicked
        System.out.println();
    }//GEN-LAST:event_spnLotSizeEnterTradeMouseClicked

    private void txtSymboldlgOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSymboldlgOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSymboldlgOrderActionPerformed

    private void mnuAddRemoveSymbolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAddRemoveSymbolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuAddRemoveSymbolActionPerformed

    private void txtEmailUsernameDlgSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailUsernameDlgSignUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailUsernameDlgSignUpActionPerformed

    private void cmdLoginDlgLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoginDlgLoginActionPerformed
        String srtAccNum = txtAccountNumberDlgLogin.getText();
        int account_number = 0;
        try {
            account_number = Integer.parseInt(srtAccNum);
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(dlgLogin,
                    "Acount number contains only digits",
                    "Invalid",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        byte[] hashPassword;
        try {
            hashPassword = SecurePasswordUtils.hashPassword(pwdPasswordDlgLogin.getPassword());
            traderAccount.login(account_number, hashPassword);
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(dlgLogin,
                    "Could not prepare data to server",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage(), ex);
        }

    }//GEN-LAST:event_cmdLoginDlgLoginActionPerformed

    private void chkRememberMeDlgLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRememberMeDlgLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkRememberMeDlgLoginActionPerformed

    private void mnuLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLoginActionPerformed
        this.dlgLogin.setSize(new Dimension(500, 400));
        this.dlgLogin.setLocationRelativeTo(this);

        this.dlgLogin.setVisible(true);
    }//GEN-LAST:event_mnuLoginActionPerformed

    private void mnuSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSignUpActionPerformed
        this.dlgSignUp.setSize(new Dimension(500, 400));
        this.dlgSignUp.setLocationRelativeTo(this);
        this.dlgSignUp.setVisible(true);
    }//GEN-LAST:event_mnuSignUpActionPerformed

    private void cmdSignUpDlgSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSignUpDlgSignUpActionPerformed
        try {
            String firstName = txtFirstNameDlgSignUp.getText();
            String lastName = txtLastNameDlgSignUp.getText();
            String email = txtEmailUsernameDlgSignUp.getText();
            char[] password = pwdPasswordDlgSignUp.getPassword();
            char[] passwordConfirm = pwdConfirmPasswordDlgSignUp.getPassword();

            if (firstName == null || firstName.isEmpty()) {
                JOptionPane.showMessageDialog(dlgSignUp,
                        "First name cannot be empty",
                        "Invalid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (lastName == null || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(dlgSignUp,
                        "Last name cannot be empty",
                        "Invalid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!EmailUtils.validate(email)) {
                JOptionPane.showMessageDialog(dlgSignUp,
                        "The email is invalid.\n\n" + EmailUtils.getRules(),
                        "Invalid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!SecurePasswordUtils.validate(password)) {
                JOptionPane.showMessageDialog(dlgSignUp,
                        "The password is invalid.\n\n" + SecurePasswordUtils.getRules(),
                        "Invalid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Arrays.equals(password, passwordConfirm)) {
                JOptionPane.showMessageDialog(dlgSignUp,
                        "Password confirmation must match password.",
                        "Invalid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte[] hashPassword = SecurePasswordUtils.hashPassword(pwdPasswordDlgLogin.getPassword());
            traderAccount.signUp(email, hashPassword, firstName, lastName);
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(dlgSignUp,
                    "Could not prepare data to server.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            logger.error(ex.getMessage(), ex);
        }
    }//GEN-LAST:event_cmdSignUpDlgSignUpActionPerformed

    private void cmdConnectionRetryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdConnectionRetryActionPerformed
        tradingClient.connectToServer();
    }//GEN-LAST:event_cmdConnectionRetryActionPerformed

    private void cmdEnableAutoTradingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEnableAutoTradingActionPerformed
        boolean isSelected = cmdEnableAutoTrading.isSelected();
        expertManager.setEnableAutoTrading(isSelected);
        if (isSelected) {
            prevEnableAutoTradingBackground = cmdEnableAutoTrading.getBackground();
            prevEnableAutoTradingForeground = cmdEnableAutoTrading.getForeground();

            cmdEnableAutoTrading.setBackground(Color.GREEN);
            cmdEnableAutoTrading.setForeground(Color.WHITE);
        } else {
            cmdEnableAutoTrading.setBackground(prevEnableAutoTradingBackground);
            cmdEnableAutoTrading.setForeground(prevEnableAutoTradingForeground);
        }

    }//GEN-LAST:event_cmdEnableAutoTradingActionPerformed

    void openDestopFileLocation(File file) {
        if (file == null) {
            return;
        }
        File dir = file;
        if (file.isFile()) {
            dir = file.getParentFile();
        }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(dir);
            } else {
                System.out.println("Desktop operations are not supported on this system.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void mnuOpenExpertLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOpenExpertLocationActionPerformed

        File expertsDir = createExpertLocationIfNotExist();

        if (expertsDir == null) {
            JOptionPane.showMessageDialog(this, "Could not open Experts location", "Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        openDestopFileLocation(expertsDir);

    }//GEN-LAST:event_mnuOpenExpertLocationActionPerformed

    private void treeExpertAdvisorsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeExpertAdvisorsMousePressed
        if (evt.isPopupTrigger()) {
            showPopupTreeExpert(evt);
        }
    }//GEN-LAST:event_treeExpertAdvisorsMousePressed

    private void treeExpertAdvisorsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeExpertAdvisorsMouseReleased
        if (evt.isPopupTrigger()) {
            showPopupTreeExpert(evt);
        }
    }//GEN-LAST:event_treeExpertAdvisorsMouseReleased

    private void mnuRefreshExpertTreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRefreshExpertTreeActionPerformed
        treeExpertAdvisors.updateUI();
    }//GEN-LAST:event_mnuRefreshExpertTreeActionPerformed

    private void mnuAttachExpertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAttachExpertActionPerformed

        showASelectedExpertPropertiesDialog();
    }//GEN-LAST:event_mnuAttachExpertActionPerformed

    void showASelectedExpertPropertiesDialog() {
        if (selectedExpertTreePath == null || selectedExpertTreePath.isDirectory()) {
            return;
        }

        this.dlgExpertProperties.setSize(new Dimension(500, 350));
        this.dlgExpertProperties.setLocationRelativeTo(this);
        dlgExpertProperties.setVisible(true);
    }

    void attachSelectedExpert() {

        File expert = new File(selectedExpertTreePath.getAbsolutePath());

        AttachededExpertModel model = (AttachededExpertModel) tblAttachedExperts.getModel();
        model.addExpert(expert, selectedSymbolForAttachExpert);
        try {
            expertManager.attach(expert, selectedSymbolForAttachExpert);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not attach expert - " + ExpertUtil.expertSimpleName(expert.getName()), "Failed", JOptionPane.ERROR_MESSAGE);
            model.removeExpert(expert);
            logger.error(ex.getMessage(), ex);
        }
    }

    private void mnuOpenExpertDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOpenExpertDirActionPerformed

        openDestopFileLocation(selectedExpertTreePath);
    }//GEN-LAST:event_mnuOpenExpertDirActionPerformed
    void removeExpert(boolean confirm) {
        if (confirm) {
            int option = JOptionPane.showConfirmDialog(this, "The you want to remove expert", "Confirm", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) {
                return;
            }
        }
        removeExpert(selectedAttachedExpert);
    }

    public static void removeExpert(File expertFile) {
        expertManager.remove(expertFile);
        attachededExpertModel.removeExpert(expertFile);
    }

    public static void removeExpert(IExpertService eaService) {
        File eaFile = expertManager.findExpertPathBy(eaService);
        if (eaFile != null) {
            removeExpert(eaFile);
        }
    }

    private void mnuRemoveRunningExpertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemoveRunningExpertActionPerformed
        removeExpert(false);
    }//GEN-LAST:event_mnuRemoveRunningExpertActionPerformed

    private void mnuReAttachExpertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReAttachExpertActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuReAttachExpertActionPerformed

    private void mnuRemoveExpertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRemoveExpertActionPerformed

        this.dlgRemoveExpertDialog.setSize(new Dimension(570, 350));
        this.dlgRemoveExpertDialog.setLocationRelativeTo(this);
        dlgRemoveExpertDialog.setVisible(true);

    }//GEN-LAST:event_mnuRemoveExpertActionPerformed

    private void treeExpertAdvisorsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeExpertAdvisorsMouseClicked
        if (evt.getClickCount() == 2) {//double click
            selectedExpertTreePath = getSelectedExpertTreePath(evt);
            showASelectedExpertPropertiesDialog();
        }
    }//GEN-LAST:event_treeExpertAdvisorsMouseClicked

    private void cmdDglCancelExpertAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDglCancelExpertAttachActionPerformed
        dlgExpertProperties.dispose();
    }//GEN-LAST:event_cmdDglCancelExpertAttachActionPerformed

    private void cmdDglAttachEpertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDglAttachEpertActionPerformed
        selectedSymbolForAttachExpert = lstDlgExpertPropertiesSelectedSymbol.getSelectedValue();
        if (selectedSymbolForAttachExpert == null) {
            tabPaneDlgExpertProperties.setSelectedComponent(scrollPaneDlgExpertPropertiesSelectedSymbol);
            JOptionPane.showMessageDialog(dlgExpertProperties,
                    "Choose a symbol to attach expert - "
                    + ExpertUtil.expertSimpleName(
                            selectedExpertTreePath.getName()),
                    "Input", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        attachSelectedExpert();
        dlgExpertProperties.dispose();
    }//GEN-LAST:event_cmdDglAttachEpertActionPerformed

    private void cmdDlgRemoveExpertDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDlgRemoveExpertDoneActionPerformed
        dlgRemoveExpertDialog.dispose();
    }//GEN-LAST:event_cmdDlgRemoveExpertDoneActionPerformed

    private void cmdDlgRemoveExpertRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDlgRemoveExpertRemoveActionPerformed
        int[] rows = tblDlgRemoveExpert.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            File expertFile = attachededExpertModel.removeExpertAt(i);
            expertManager.remove(expertFile);
        }
    }//GEN-LAST:event_cmdDlgRemoveExpertRemoveActionPerformed

    private void cmdDlgRemoveExpertSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDlgRemoveExpertSelectAllActionPerformed
        tblDlgRemoveExpert.selectAll();
    }//GEN-LAST:event_cmdDlgRemoveExpertSelectAllActionPerformed

    private void dlgRemoveExpertDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgRemoveExpertDialogWindowClosing
        tblDlgRemoveExpert.clearSelection();
    }//GEN-LAST:event_dlgRemoveExpertDialogWindowClosing

    private void dlgRemoveExpertDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_dlgRemoveExpertDialogWindowClosed
        tblDlgRemoveExpert.clearSelection();
    }//GEN-LAST:event_dlgRemoveExpertDialogWindowClosed

    private void tblAttachedExpertsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAttachedExpertsMousePressed
        showPopupForAttachedExpert(evt);
    }//GEN-LAST:event_tblAttachedExpertsMousePressed

    private void tblAttachedExpertsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAttachedExpertsMouseReleased
        showPopupForAttachedExpert(evt);
    }//GEN-LAST:event_tblAttachedExpertsMouseReleased

    private void showPopupForAttachedExpert(MouseEvent e) {

        if (e.isPopupTrigger()) { // Right-click detected
            int row = tblAttachedExperts.rowAtPoint(e.getPoint()); // Get the row where the click happened
            //tblAttachedExperts.setRowSelectionInterval(row, row); // Select the row
            selectedAttachedExpert = attachededExpertModel.getExpertAt(row);
            // Show the popup menu at the location of the mouse event
            popMnuRunningExpert.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void showPopupTreeExpert(MouseEvent e) {
        selectedExpertTreePath = getSelectedExpertTreePath(e);
        if (selectedExpertTreePath != null) {
            popMnuExpert.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    File getSelectedExpertTreePath(MouseEvent e) {
        int row = treeExpertAdvisors.getRowForLocation(e.getX(), e.getY());
        TreePath path = treeExpertAdvisors.getPathForLocation(e.getX(), e.getY());

        if (row != -1 && path != null) {
            // Select the clicked node
            treeExpertAdvisors.setSelectionPath(path);

            return (File) path.getLastPathComponent();

        }
        return null;
    }

    File createAppHomeIfNotExist() {
        // Create a File object representing the 'myapp' directory in the home directory
        File dir = new File(AppConfig.APP_HOME_DIR);

        if (!dir.exists()) {
            // If it doesn't exist, create it
            if (dir.mkdir()) {
                System.out.println("App Home directory created successfully at: " + dir.getAbsolutePath());
            } else {
                System.out.println("Failed to create App Home directory.");
                return null;
            }
        }

        return dir;
    }

    File createExpertLocationIfNotExist() {

        File appHome = createAppHomeIfNotExist();
        if (appHome == null) {
            return null;
        }

        File expertDirectory = new File(AppConfig.EXPERTS_DIR);

        // Check if the 'myapp' directory exists
        if (!expertDirectory.exists()) {
            // If it doesn't exist, create it
            if (expertDirectory.mkdir()) {
                System.out.println("Experts directory created successfully at: " + expertDirectory.getAbsolutePath());
            } else {
                System.out.println("Failed to create Experts directory.");
                return null;
            }
        }

        return expertDirectory;
    }

    private void init() {
        createExpertLocationIfNotExist();
    }

    class PriceFocusAdapter extends java.awt.event.FocusAdapter {

        JSpinner spinner;
        JComponent priceComp;

        PriceFocusAdapter(JSpinner spinner, JComponent priceComp) {
            this.spinner = spinner;
            this.priceComp = priceComp;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (priceComp instanceof JComboBox) {
                showPriceOnSpinner(spinner, (String) cboSelectSymbolEnterTrade.getSelectedItem());
            } else {
                showPriceOnSpinner(spinner, orderSelected.getSymbol());
            }
        }

    }

    private void makePriceSpinnerComponentsPriceCapturable() {
        JSpinner[] spinners = {spnTakeProfitDlgOrder,
            spnStoplossDlgOrder,
            spnEntryPriceForPendingOrderEnterTrade,
            spnTakeProfitEnterTrade,
            spnStoplossEnterTrade};

        for (int i = 0; i < spinners.length; i++) {
            JComponent editor = spinners[i].getEditor();
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();

            textField.setDocument(new PositiveNumericDocument());

            if (spinners[i].equals(spnEntryPriceForPendingOrderEnterTrade)
                    || spinners[i].equals(spnTakeProfitEnterTrade)
                    || spinners[i].equals(spnStoplossEnterTrade)) {
                textField.addFocusListener(new PriceFocusAdapter(spinners[i], cboSelectSymbolEnterTrade));
            } else if (spinners[i].equals(spnTakeProfitDlgOrder)
                    || spinners[i].equals(spnStoplossDlgOrder)) {
                textField.addFocusListener(new PriceFocusAdapter(spinners[i], null));
            }

        }

    }

    static String formatPrice(double price, SymbolInfo symbolInfo) {
        if (symbolInfo.isFiveDigits()) {
            return fiveDecimalFormat.format(price);
        } else if (symbolInfo.isThreeDigits()) {
            return threeDecimalFormat.format(price);
        } else if (symbolInfo.isTwoDigits()) {
            return twoDecimalFormat.format(price);
        }

        return "0";
    }

    void showPriceOnSpinner(JSpinner spinner, String symbol) {

        Number price = (Number) spinner.getValue();
        if (price.doubleValue() > 0) {
            return;
        }

        SymbolInfo symbolInfo = traderAccount.getSymbolInfo(symbol);
        if (symbolInfo == null) {
            return;
        }

        validatePriceInputControl(spinner, symbolInfo.getDigits());

        spinner.setValue(symbolInfo.getBid());// as it is in MT4
    }

    private void validateLotSizeInputControl(JSpinner spinner) {
        JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        // Create a custom number formatter

        // Set the custom formatter to the spinner's editor
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();

        NumberFormatter numberFormatter = new NumberFormatter(twoDecimalFormat);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setMinimum(0.01);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);
        textField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
        model.setStepSize(0.1);

    }

    private void validatePriceInputControl(JSpinner spinner, int symbol_digits) {
        JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        // Create a custom number formatter

        // Set the custom formatter to the spinner's editor
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        if (symbol_digits == 5) {
            NumberFormatter numberFormatter = new NumberFormatter(fiveDecimalFormat);
            numberFormatter.setValueClass(Double.class);
            numberFormatter.setMinimum(0.0);
            numberFormatter.setAllowsInvalid(false);
            numberFormatter.setCommitsOnValidEdit(true);
            textField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
            model.setStepSize(0.00001);
        } else if (symbol_digits == 3) {
            NumberFormatter numberFormatter = new NumberFormatter(threeDecimalFormat);
            numberFormatter.setValueClass(Double.class);
            numberFormatter.setMinimum(0.0);
            numberFormatter.setAllowsInvalid(false);
            numberFormatter.setCommitsOnValidEdit(true);
            textField.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));
            model.setStepSize(0.001);
        }
    }

    void showDlgModifyOrder(Order order, int action_index) {

        cboActionDlgOrder.setEnabled(true); //enable if was disable by previous order processing
        cboActionDlgOrder.removeAllItems();

        if (order.isPendingOrder()) {
            cboActionDlgOrder.addItem(ORDER_ACTION_MODIFY);
            cboActionDlgOrder.addItem(ORDER_ACTION_DELETE);
            cmdCloseDlgOrder.setText(ORDER_ACTION_DELETE);
        } else if (order.isMarketOrder()) {
            cboActionDlgOrder.addItem(ORDER_ACTION_MODIFY);
            cboActionDlgOrder.addItem(ORDER_ACTION_CLOSE);
            cmdCloseDlgOrder.setText(ORDER_ACTION_CLOSE);
        }

        lblOrderIDdlgOrder.setText("#" + order.getOrderID());
        txtSymboldlgOrder.setText(order.getSymbol());
        spnLotSizeDlgOrder.setValue(order.getLotSize());

        validatePriceInputControl(spnTakeProfitDlgOrder, order.getSymbolDigits());
        validatePriceInputControl(spnStoplossDlgOrder, order.getSymbolDigits());

        spnTakeProfitDlgOrder.setValue(order.getTargetPrice());
        spnStoplossDlgOrder.setValue(order.getStoplossPrice());

        cboActionDlgOrder.setSelectedIndex(action_index);
        orderActionSelected();

        lblResponseMsgDlgOrder.setText("");//show noting

        this.orderSelected = order;

        dlgOrder.setSize(new Dimension(500, 400));
        dlgOrder.setLocationRelativeTo(this);

        dlgOrder.setVisible(true);
    }

    void orderActionSelected() {
        String strAction = (String) cboActionDlgOrder.getSelectedItem();

        if (ORDER_ACTION_MODIFY.equals(strAction)) {
            spnLotSizeDlgOrder.setEnabled(false);
            cmdModifyDlgOrder.setEnabled(true);
            cmdCloseDlgOrder.setEnabled(false);
        } else if (ORDER_ACTION_CLOSE.equals(strAction)) {
            spnLotSizeDlgOrder.setEnabled(true);
            cmdModifyDlgOrder.setEnabled(false);
            cmdCloseDlgOrder.setEnabled(true);
        } else if (ORDER_ACTION_DELETE.equals(strAction)) {
            spnLotSizeDlgOrder.setEnabled(false);
            cmdModifyDlgOrder.setEnabled(false);
            cmdCloseDlgOrder.setEnabled(true);
        } else {
            // Default case, do nothing
        }
    }

    void selectedOpenOrderTable(java.awt.event.MouseEvent evt, boolean show_popup_menu) {
        selectedOpenOrderTableRow = tblOpenOrders.rowAtPoint(evt.getPoint());
        selectedOpenOrderTableColumn = tblOpenOrders.columnAtPoint(evt.getPoint());

        boolean is_exit_column = ((OpenOrderTableModel) tblOpenOrders.getModel()).isEixtColumn(selectedOpenOrderTableColumn);

        if (show_popup_menu && !is_exit_column) {
            popMnuOpenOrder.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if (!show_popup_menu && is_exit_column) {
            Order order = ((OpenOrderTableModel) tblOpenOrders.getModel()).getOrderAt(selectedOpenOrderTableRow);
            closeOpenOrderOnSelectedTableRow(order, order.getLotSize(), false);
        }
    }

    void selectedPendingOrderTable(java.awt.event.MouseEvent evt, boolean show_popup_menu) {
        selectedPendingOrderTableRow = tblPendingOrders.rowAtPoint(evt.getPoint());
        selectedPendingOrderTableColumn = tblPendingOrders.columnAtPoint(evt.getPoint());

        boolean is_exit_column = ((PendingOrderTableModel) tblPendingOrders.getModel()).isEixtColumn(selectedPendingOrderTableColumn);

        if (show_popup_menu && !is_exit_column) {
            popMnuPendingOrder.show(evt.getComponent(), evt.getX(), evt.getY());
        } else if (!show_popup_menu && is_exit_column) {
            Order order = ((PendingOrderTableModel) tblPendingOrders.getModel()).getOrderAt(selectedPendingOrderTableRow);
            deletePendingOrderOnSelectedTableRow(order);
        }
    }

    private void closeOpenOrderOnSelectedTableRow(Order order, double lot_size, boolean dont_ask) {

        OpenOrderTableModel model = (OpenOrderTableModel) tblOpenOrders.getModel();

        if (order == null) {
            Order open_order = model.getOrderAt(selectedOpenOrderTableRow);
            showDlgModifyOrder(open_order, 1);
            return;
        }

        int orderID_index = model.indexOfColumn(model.ID);
        String orderID = (String) model.getValueAt(selectedOpenOrderTableRow, orderID_index);

        int option = JOptionPane.YES_OPTION; //initialize for dont_ask

        if (!dont_ask) {
            Object[] options = {"Yes Close it", "Cancel"};

            // Show the confirm dialog
            option = JOptionPane.showOptionDialog(
                    this, "Are you sure you want to close order #" + orderID,
                    "Confirm Action",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        }

        if (option == JOptionPane.YES_OPTION) {
            traderAccount.sendClosePosition(orderID, lot_size);
        }

    }

    private void modifyOpenOrderOnSelectedTableRow(Order order, double target_price, double stoploss_price) {

        OpenOrderTableModel model = (OpenOrderTableModel) tblOpenOrders.getModel();
        if (order == null) {
            Order open_order = model.getOrderAt(selectedOpenOrderTableRow);
            showDlgModifyOrder(open_order, 0);
            return;
        }

        //at this point modify the open order        
        traderAccount.modifyOpenOrder(order.getOrderID(), target_price, stoploss_price);
    }

    private void deletePendingOrderOnSelectedTableRow(Order order) {
        PendingOrderTableModel model = (PendingOrderTableModel) tblPendingOrders.getModel();
        if (order == null) {
            Order pending_order = model.getOrderAt(selectedPendingOrderTableRow);
            showDlgModifyOrder(pending_order, 1);
            return;
        }
        traderAccount.deletePendingOrder(order.getOrderID());
    }

    private void modifyPendingOrderOnSelectedTableRow(Order order, double open_price, double target_price, double stoploss_price) {

        PendingOrderTableModel model = (PendingOrderTableModel) tblPendingOrders.getModel();
        if (order == null) {
            Order pending_order = model.getOrderAt(selectedPendingOrderTableRow);
            showDlgModifyOrder(pending_order, 0);
            return;
        }
        //at this point modify the pending order
        traderAccount.modifyPendingOrder(order.getOrderID(), open_price, target_price, stoploss_price);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            logger.error(ex.getMessage(), ex);
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {

            try {
                mainGUI = new MainGUI();
                userDlgOrderAction = new UserOrderAction(mainGUI);

                traderAccount = new TraderAccountManager();

                expertManager = new ExpertManager(traderAccount);

                traderAccount.addOrderActionListener(activity);
                traderAccount.addOrderActionListener(openOrderTableModel);
                traderAccount.addOrderActionListener(openOrderTableModel);
                traderAccount.addOrderActionListener(historyOrderTableModel);
                traderAccount.addOrderActionListener(pendingOrderTableModel);
                traderAccount.addOrderActionListener(userDlgOrderAction);

                traderAccount.addSymbolUpdateListener(activity);
                traderAccount.addSymbolUpdateListener(marketWatchTableModel);
                traderAccount.addSymbolUpdateListener(uISymbolUpdateHandler);

                traderAccount.addConnectionListener(activity);
                traderAccount.addConnectionListener(connectionListerner);

                traderAccount.addAccountListener(activity);
                traderAccount.addAccountListener(accountListerner);

                //traderAccount.refreshContent();//NOT NECCESSARY - AUTOMATICALLY DONE FROM THE SERVER
                (tradingClient = new TradingClient(traderAccount,
                        "localhost",
                        8080)).start();

                mainGUI.setVisible(true);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Status;
    private static javax.swing.JComboBox<String> cboActionDlgOrder;
    private javax.swing.JComboBox<String> cboFilterSymbolsDlgSelectSymbols;
    private javax.swing.JComboBox<String> cboSelectSideEnterTrade;
    private static javax.swing.JComboBox<String> cboSelectSymbolEnterTrade;
    private javax.swing.JCheckBox chkRememberMeDlgLogin;
    private javax.swing.JButton cmdAddRemvoeSymbol;
    private static javax.swing.JButton cmdCloseDlgOrder;
    private javax.swing.JButton cmdConfirmEnterTrade;
    private static javax.swing.JButton cmdConnectionRetry;
    private javax.swing.JButton cmdDglAttachEpert;
    private javax.swing.JButton cmdDglCancelExpertAttach;
    private javax.swing.JButton cmdDlgRemoveExpertDone;
    private javax.swing.JButton cmdDlgRemoveExpertRemove;
    private javax.swing.JButton cmdDlgRemoveExpertSelectAll;
    private javax.swing.JButton cmdDoneDlgSelectSymbols;
    private javax.swing.JToggleButton cmdEnableAutoTrading;
    private javax.swing.JButton cmdForgotPasswordDlgLogin;
    private javax.swing.JButton cmdIDontHaveAnAccountDlgLogin;
    private javax.swing.JButton cmdLoginDlgLogin;
    private static javax.swing.JButton cmdModifyDlgOrder;
    private javax.swing.JButton cmdRemoveSelectedSymbolsDlgSelectSymbols;
    private javax.swing.JButton cmdSignUpDlgSignUp;
    private javax.swing.JButton cmdTransferToSelectedSymbolsDlgSelectSymbols;
    private javax.swing.JDialog dlgExpertProperties;
    private javax.swing.JDialog dlgLogin;
    private static javax.swing.JDialog dlgOrder;
    private javax.swing.JDialog dlgRemoveExpertDialog;
    private javax.swing.JDialog dlgSelectSymbols;
    private javax.swing.JDialog dlgSignUp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblAllInstrumentsLabelDlgSelectSymbols;
    private static javax.swing.JLabel lblAskEnterTrade;
    private static javax.swing.JLabel lblBidEnterTrade;
    private static javax.swing.JLabel lblConnectionStatusDisplay;
    private javax.swing.JLabel lblOrderIDdlgOrder;
    private static javax.swing.JLabel lblResponseMsgDlgOrder;
    private javax.swing.JList<String> lstAllSymbolsDlgSelectSymbols;
    private javax.swing.JList<String> lstDlgExpertPropertiesSelectedSymbol;
    private javax.swing.JList<String> lstSelectedSymbolsDlgSelectSymbols;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenu mnuAction;
    private javax.swing.JMenuItem mnuAddRemoveSymbol;
    private javax.swing.JMenuItem mnuAttachExpert;
    private javax.swing.JMenuItem mnuEixt;
    private javax.swing.JMenuItem mnuExpertLog;
    private javax.swing.JMenuItem mnuHistoryOrders;
    private javax.swing.JMenuItem mnuItmCloseOrder;
    private javax.swing.JMenuItem mnuItmDeletePendingOrder;
    private javax.swing.JMenuItem mnuItmModifyOrder;
    private javax.swing.JMenuItem mnuItmModifyPendingOrder;
    private javax.swing.JMenuItem mnuLogin;
    private javax.swing.JMenuItem mnuLogout;
    private javax.swing.JMenuItem mnuOpenExpertDir;
    private javax.swing.JMenuItem mnuOpenExpertLocation;
    private javax.swing.JMenuItem mnuOpenPositions;
    private javax.swing.JMenuItem mnuPendingOrders;
    private javax.swing.JMenuItem mnuReAttachExpert;
    private javax.swing.JMenuItem mnuRefreshExpertTree;
    private javax.swing.JMenuItem mnuRemoveExpert;
    private javax.swing.JMenuItem mnuRemoveRunningExpert;
    private javax.swing.JMenuItem mnuSignUp;
    private javax.swing.JPanel panelTop;
    private javax.swing.JPanel pnlDlgExpertPropertiesInputs;
    private javax.swing.JPanel pnlEnterTrade;
    private javax.swing.JPopupMenu popMnuExpert;
    private javax.swing.JPopupMenu popMnuOpenOrder;
    private javax.swing.JPopupMenu popMnuPendingOrder;
    private javax.swing.JPopupMenu popMnuRunningExpert;
    private javax.swing.JPasswordField pwdConfirmPasswordDlgSignUp;
    private javax.swing.JPasswordField pwdPasswordDlgLogin;
    private javax.swing.JPasswordField pwdPasswordDlgSignUp;
    private javax.swing.JScrollPane scrollPaneDlgExpertPropertiesSelectedSymbol;
    private javax.swing.JSplitPane splitPaneTop;
    private javax.swing.JSpinner spnEntryPriceForPendingOrderEnterTrade;
    private javax.swing.JSpinner spnLotSizeDlgOrder;
    private javax.swing.JSpinner spnLotSizeEnterTrade;
    private javax.swing.JSpinner spnOpenPriceDlgOrder;
    private javax.swing.JSpinner spnStoplossDlgOrder;
    private javax.swing.JSpinner spnStoplossEnterTrade;
    private javax.swing.JSpinner spnTakeProfitDlgOrder;
    private javax.swing.JSpinner spnTakeProfitEnterTrade;
    private javax.swing.JTabbedPane tabPaneDlgExpertProperties;
    private javax.swing.JTabbedPane tabPaneOrders;
    private javax.swing.JTable tblAttachedExperts;
    private javax.swing.JTable tblDlgRemoveExpert;
    private javax.swing.JTable tblHistoryOrders;
    private javax.swing.JTable tblMarketWatch;
    private javax.swing.JTable tblOpenOrders;
    private javax.swing.JTable tblPendingOrders;
    private javax.swing.JToolBar toolBarFooter;
    private javax.swing.JTree treeExpertAdvisors;
    private javax.swing.JTextField txtAccountNumberDlgLogin;
    private javax.swing.JTextField txtEmailUsernameDlgSignUp;
    private javax.swing.JTextField txtFirstNameDlgSignUp;
    private javax.swing.JTextField txtLastNameDlgSignUp;
    private javax.swing.JTextField txtSymboldlgOrder;
    // End of variables declaration//GEN-END:variables

}

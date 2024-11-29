/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.expert;

import chuks.flatbook.fx.trader.account.contract.TraderAccount;
import chuks.flatbook.fx.trader.expert.inject.DependencyRegistry;
import chuks.flatbook.fx.trader.expert.inject.Injector;
import chuks.flatbook.fx.trader.main.MainGUI;
import chuks.flatbook.fx.trader.main.Timeframe;
import expert.ExpertAdvisorMQ4;
import expert.contract.IExpertAdvisor;
import static expert.contract.IExpertAdvisor.INIT_FAILED;
import static expert.contract.IExpertAdvisor.REASON_INITFAILED;
import expert.contract.IExpertService;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user
 */
public class ExpertManager {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExpertManager.class.getName());
    private boolean IsEnableAutoTrading;
    DependencyRegistry registry = new DependencyRegistry();

    static Map<File, ExpertServiceImpl> eaMap = Collections.synchronizedMap(new LinkedHashMap());
    private static TraderAccount traderAccount;
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public ExpertManager(TraderAccount trader_account) {
        traderAccount = trader_account;
        registry.register(IExpertService.class, ExpertServiceImpl.class);
    }

    public static void submitOnTickEventOnAllExperts(String symbol) {
        eaMap.values().forEach((ExpertServiceImpl expert) -> {
            if (expert.Symbol().equals(symbol)) {
                executor.submit(() -> {
                    try {
                        expert.OnTick();
                    } catch (NullPointerException e) {
                        logger.error(e.getMessage(), e);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });
            }
        });
    }
    
    public File findExpertPathBy(IExpertService eaService) {
        for (Map.Entry<File, ExpertServiceImpl> entry : eaMap.entrySet()) {
            if(entry.getValue().equals(eaService)){
                return entry.getKey();
            }
        }
        return null;
    }
    
    static ExecutorService getExecutor() {
        return executor;
    }

    public static TraderAccount getTraderAccount() {
        return traderAccount;
    }

    public void setEnableAutoTrading(boolean b) {
        IsEnableAutoTrading = b;
        eaMap.values().forEach((IExpertService service) -> {
            service.setIsExpertEnabled(b);
        });
    }

    public void attach(File expertFile, String expertSymbol, Timeframe timeframe) throws Exception {
        ExpertAdvisorMQ4 ea = ExpertUtil.createExpert(expertFile.getAbsolutePath());

        // Create a dependency injector
        Injector<ExpertAdvisorMQ4, ExpertServiceImpl> injector = new Injector(registry, ExpertAdvisorMQ4.class);

        ExpertServiceImpl eaService = injector.injectFieldAndGet(ea);
        
        //set internal expert properites
        eaService.setSymbol(expertSymbol);
        eaService.setTimeframe(timeframe);
        eaService.SET__FILE__(expertFile.getName());
        eaService.SET__PATH__(expertFile.getAbsolutePath());
        //more goes below
        
        int init_result = ea.OnInit();
        
        eaMap.put(expertFile, eaService);
        
        if(init_result == INIT_FAILED){
            MainGUI.removeExpert(expertFile, REASON_INITFAILED);
        }
    }

    public void remove(File expertFile, int reason) {
        IExpertService service = eaMap.remove(expertFile);
        service.getExpert().OnDeinit(reason);
    }


}

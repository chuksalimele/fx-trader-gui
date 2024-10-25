/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbok.fx.trader.expert;

import chuks.flatbok.fx.trader.account.contract.TraderAccount;
import chuks.flatbok.fx.trader.expert.inject.DependencyRegistry;
import chuks.flatbok.fx.trader.expert.inject.Injector;
import expert.ExpertAdvisorMQ4;
import expert.contract.IExpertService;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    public void attach(File expertFile, String expertSymbol) throws Exception {
        ExpertAdvisorMQ4 ea = ExpertUtil.createExpert(expertFile.getAbsolutePath());

        // Create a dependency injector
        Injector<ExpertAdvisorMQ4, ExpertServiceImpl> injector = new Injector(registry, ExpertAdvisorMQ4.class);

        ExpertServiceImpl eaService = injector.injectFieldAndGet(ea);
        
        //set internal expert properites
        eaService.setSymbol(expertSymbol);
        eaService.SET__FILE__(expertFile.getName());
        eaService.SET__PATH__(expertFile.getAbsolutePath());
        //more goes below
        
        ea.OnInit();
        eaMap.put(expertFile, eaService);
    }

    public void remove(File expertFile) {
        IExpertService service = eaMap.remove(expertFile);
        int reason = 0;//TODO
        service.getExpert().OnDeinit(reason);
    }


}

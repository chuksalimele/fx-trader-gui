/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.config;

import java.io.File;

/**
 *
 * @author user
 */
public interface AppConfig {
    
    String APP_HOME_NAME = ".flatbook_tm";
    String APP_HOME_DIR = System.getProperty("user.home")+ File.separator +APP_HOME_NAME;
    String EXPERTS_DIR = APP_HOME_DIR+ File.separator+"Experts";
    int MAX_RESPONSE_WAIT_TIME = 30;
}

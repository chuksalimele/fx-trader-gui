/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;

/**
 *
 * @author user
 */
public class AppConfig {

    public final static String APP_HOME_NAME = ".flatbook_tm";
    public final static String APP_HOME_DIR = System.getProperty("user.home") + File.separator + APP_HOME_NAME;
    public final static String EXPERTS_DIR = APP_HOME_DIR + File.separator + "Experts";
    public final static int MAX_RESPONSE_WAIT_TIME = 30;
    private static final String CONFIG_FILE_NAME = "config.properties";
    final private static File CONFIG_FILE = new File(APP_HOME_DIR, CONFIG_FILE_NAME);
    private static FileBasedConfigurationBuilder<PropertiesConfiguration> builder;

    final private static String SELECTED_SYMBOLS = "selected_symbols";
    private static PropertiesConfiguration config;
    static {

        try {
            File AppDir = new File(APP_HOME_DIR);
            // Check if the 'myapp' directory exists
            if (!AppDir.exists()) {
                // If it doesn't exist, create it
                if (AppDir.createNewFile()) {
                    System.out.println("App home directory created successfully at: " + AppDir.getAbsolutePath());
                } else {
                    System.out.println("Failed to create App home directory.");
                }
            }
            // Check if the config file exist
            if (!CONFIG_FILE.exists()) {
                // If it doesn't exist, create it
                if (CONFIG_FILE.createNewFile()) {
                    System.out.println("Config file created successfully at: " + CONFIG_FILE.getAbsolutePath());
                } else {
                    System.out.println("Failed to create config file.");
                }
            }
            // Load or create configuration file
            builder = loadOrCreateConfig(CONFIG_FILE);
        } catch (ConfigurationException | IOException ex) {
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String[] getSelectedSymbols() throws ConfigurationException {
        return config.getStringArray(SELECTED_SYMBOLS);
    }

    public static void saveSelectedSymbols(String[] symbols) throws ConfigurationException {

        config.setProperty(SELECTED_SYMBOLS, String.join(",", symbols));

        // Save the updated configuration
        builder.save();

    }

    private static FileBasedConfigurationBuilder<PropertiesConfiguration> loadOrCreateConfig(File configFile) throws ConfigurationException {
        // Define parameters for the config_builder
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<PropertiesConfiguration> config_builder
                = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFile(configFile)
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));

        createDefaultConfig(configFile, config_builder);
        return config_builder;
    }

    private static void createDefaultConfig(File configFile, FileBasedConfigurationBuilder<PropertiesConfiguration> builder)
            throws ConfigurationException {
        // Get the configuration object
         config = builder.getConfiguration();

        if (config.isEmpty()) {
            // Set default properties
            config.setProperty(SELECTED_SYMBOLS, "");
            //more properties may go below

            // Save the default configuration to file
            builder.save();
            System.out.println("Default configuration file created at: " + configFile.getAbsolutePath());
        }
    }
}

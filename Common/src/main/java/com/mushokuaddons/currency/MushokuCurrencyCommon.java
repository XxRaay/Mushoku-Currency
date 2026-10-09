package com.mushokuaddons.currency;

import com.mushokuaddons.currency.command.CurrencyCommands;
import com.mushokuaddons.currency.event.CurrencyGameEvents;
import com.mushokuaddons.currency.init.ModCreativeTabs;
import com.mushokuaddons.currency.init.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MushokuCurrencyCommon {
    public static final String MOD_ID = "mushokucurrency";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Initializing Mushoku Currency Common...");

        ModItems.register();
        ModCreativeTabs.register();
        CurrencyGameEvents.register();
        CurrencyCommands.register();

        LOGGER.info("Mushoku Currency Common initialized successfully.");
    }
}

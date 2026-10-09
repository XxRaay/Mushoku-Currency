package com.mushokuaddons.currency.fabric;

import com.mushokuaddons.currency.MushokuCurrencyCommon;
import net.fabricmc.api.ModInitializer;

public class MushokuCurrencyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MushokuCurrencyCommon.init();
    }
}

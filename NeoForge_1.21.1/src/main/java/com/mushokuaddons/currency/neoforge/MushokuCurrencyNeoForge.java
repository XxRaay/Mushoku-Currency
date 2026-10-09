package com.mushokuaddons.currency.neoforge;

import com.mushokuaddons.currency.MushokuCurrencyCommon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(MushokuCurrencyCommon.MOD_ID)
public class MushokuCurrencyNeoForge {
    public MushokuCurrencyNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        MushokuCurrencyCommon.init();
    }
}

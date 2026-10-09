package com.mushokuaddons.currency.init;

import com.mushokuaddons.currency.MushokuCurrencyCommon;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MushokuCurrencyCommon.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> CURRENCY_TAB = TABS.register("mushoku_currency", () ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("itemGroup.mushokucurrency.main"))
                    .icon(() -> new ItemStack(ModItems.ASURA_GOLD_COIN.get()))
                    .displayItems((params, output) -> {
                        // Asura Coins
                        output.accept(ModItems.ASURA_IRON_COIN.get());
                        output.accept(ModItems.ASURA_COPPER_COIN.get());
                        output.accept(ModItems.ASURA_LARGE_COPPER_COIN.get());
                        output.accept(ModItems.ASURA_SILVER_COIN.get());
                        output.accept(ModItems.ASURA_GOLD_COIN.get());

                        // Demon Continent Coins
                        output.accept(ModItems.DEMON_SCRAP_COIN.get());
                        output.accept(ModItems.DEMON_STONE_COIN.get());
                        output.accept(ModItems.DEMON_LARGE_STONE_COIN.get());
                        output.accept(ModItems.DEMON_GREEN_ORE_COIN.get());

                        // Millis Continent Coins
                        output.accept(ModItems.MILLIS_COPPER_COIN.get());
                        output.accept(ModItems.MILLIS_SILVER_COIN.get());
                        output.accept(ModItems.MILLIS_GOLD_COIN.get());

                        // Begaritt Continent Coins
                        output.accept(ModItems.BEGARITT_COPPER_COIN.get());
                        output.accept(ModItems.BEGARITT_SILVER_COIN.get());
                        output.accept(ModItems.BEGARITT_GOLD_COIN.get());
                    })
                    .build()
    );

    public static void register() {
        TABS.register();
    }
}

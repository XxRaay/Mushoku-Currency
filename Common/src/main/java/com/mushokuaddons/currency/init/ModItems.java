package com.mushokuaddons.currency.init;

import com.mushokuaddons.currency.MushokuCurrencyCommon;
import com.mushokuaddons.currency.economy.CurrencyRegion;
import com.mushokuaddons.currency.item.CoinItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MushokuCurrencyCommon.MOD_ID, Registries.ITEM);

    private static final List<RegistrySupplier<CoinItem>> ALL_COINS = new ArrayList<>();

    // === ASURA COINS ===
    public static final RegistrySupplier<CoinItem> ASURA_IRON_COIN = registerCoin(
            "asura_iron_coin", CurrencyRegion.ASURA, 1L, "tooltip.mushokucurrency.desc.asura_iron_coin",
            item -> item.setHigherCoin(() -> ModItems.ASURA_COPPER_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> ASURA_COPPER_COIN = registerCoin(
            "asura_copper_coin", CurrencyRegion.ASURA, 10L, "tooltip.mushokucurrency.desc.asura_copper_coin",
            item -> item.setHigherCoin(() -> ModItems.ASURA_LARGE_COPPER_COIN.get(), 10).setLowerCoin(() -> ModItems.ASURA_IRON_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> ASURA_LARGE_COPPER_COIN = registerCoin(
            "asura_large_copper_coin", CurrencyRegion.ASURA, 100L, "tooltip.mushokucurrency.desc.asura_large_copper_coin",
            item -> item.setHigherCoin(() -> ModItems.ASURA_SILVER_COIN.get(), 10).setLowerCoin(() -> ModItems.ASURA_COPPER_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> ASURA_SILVER_COIN = registerCoin(
            "asura_silver_coin", CurrencyRegion.ASURA, 1000L, "tooltip.mushokucurrency.desc.asura_silver_coin",
            item -> item.setHigherCoin(() -> ModItems.ASURA_GOLD_COIN.get(), 10).setLowerCoin(() -> ModItems.ASURA_LARGE_COPPER_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> ASURA_GOLD_COIN = registerCoin(
            "asura_gold_coin", CurrencyRegion.ASURA, 10000L, "tooltip.mushokucurrency.desc.asura_gold_coin",
            item -> item.setLowerCoin(() -> ModItems.ASURA_SILVER_COIN.get(), 10)
    );

    // === DEMON CONTINENT COINS ===
    public static final RegistrySupplier<CoinItem> DEMON_SCRAP_COIN = registerCoin(
            "demon_scrap_coin", CurrencyRegion.DEMON, 1L, "tooltip.mushokucurrency.desc.demon_scrap_coin",
            item -> item.setHigherCoin(() -> ModItems.DEMON_STONE_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> DEMON_STONE_COIN = registerCoin(
            "demon_stone_coin", CurrencyRegion.DEMON, 10L, "tooltip.mushokucurrency.desc.demon_stone_coin",
            item -> item.setHigherCoin(() -> ModItems.DEMON_LARGE_STONE_COIN.get(), 10).setLowerCoin(() -> ModItems.DEMON_SCRAP_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> DEMON_LARGE_STONE_COIN = registerCoin(
            "demon_large_stone_coin", CurrencyRegion.DEMON, 100L, "tooltip.mushokucurrency.desc.demon_large_stone_coin",
            item -> item.setHigherCoin(() -> ModItems.DEMON_GREEN_ORE_COIN.get(), 50).setLowerCoin(() -> ModItems.DEMON_STONE_COIN.get(), 10)
    );
    public static final RegistrySupplier<CoinItem> DEMON_GREEN_ORE_COIN = registerCoin(
            "demon_green_ore_coin", CurrencyRegion.DEMON, 5000L, "tooltip.mushokucurrency.desc.demon_green_ore_coin",
            item -> item.setLowerCoin(() -> ModItems.DEMON_LARGE_STONE_COIN.get(), 50)
    );

    // === MILLIS CONTINENT COINS ===
    public static final RegistrySupplier<CoinItem> MILLIS_COPPER_COIN = registerCoin(
            "millis_copper_coin", CurrencyRegion.MILLIS, 10L, "tooltip.mushokucurrency.desc.millis_copper_coin",
            item -> item.setHigherCoin(() -> ModItems.MILLIS_SILVER_COIN.get(), 64)
    );
    public static final RegistrySupplier<CoinItem> MILLIS_SILVER_COIN = registerCoin(
            "millis_silver_coin", CurrencyRegion.MILLIS, 1000L, "tooltip.mushokucurrency.desc.millis_silver_coin",
            item -> item.setHigherCoin(() -> ModItems.MILLIS_GOLD_COIN.get(), 10).setLowerCoin(() -> ModItems.MILLIS_COPPER_COIN.get(), 64)
    );
    public static final RegistrySupplier<CoinItem> MILLIS_GOLD_COIN = registerCoin(
            "millis_gold_coin", CurrencyRegion.MILLIS, 10000L, "tooltip.mushokucurrency.desc.millis_gold_coin",
            item -> item.setLowerCoin(() -> ModItems.MILLIS_SILVER_COIN.get(), 10)
    );

    // === BEGARITT CONTINENT COINS ===
    public static final RegistrySupplier<CoinItem> BEGARITT_COPPER_COIN = registerCoin(
            "begaritt_copper_coin", CurrencyRegion.BEGARITT, 10L, "tooltip.mushokucurrency.desc.begaritt_copper_coin",
            item -> item.setHigherCoin(() -> ModItems.BEGARITT_SILVER_COIN.get(), 64)
    );
    public static final RegistrySupplier<CoinItem> BEGARITT_SILVER_COIN = registerCoin(
            "begaritt_silver_coin", CurrencyRegion.BEGARITT, 1000L, "tooltip.mushokucurrency.desc.begaritt_silver_coin",
            item -> item.setHigherCoin(() -> ModItems.BEGARITT_GOLD_COIN.get(), 10).setLowerCoin(() -> ModItems.BEGARITT_COPPER_COIN.get(), 64)
    );
    public static final RegistrySupplier<CoinItem> BEGARITT_GOLD_COIN = registerCoin(
            "begaritt_gold_coin", CurrencyRegion.BEGARITT, 10000L, "tooltip.mushokucurrency.desc.begaritt_gold_coin",
            item -> item.setLowerCoin(() -> ModItems.BEGARITT_SILVER_COIN.get(), 10)
    );

    private static RegistrySupplier<CoinItem> registerCoin(String name, CurrencyRegion region, long valueUnit, String descKey,
                                                           Consumer<CoinItem> config) {
        RegistrySupplier<CoinItem> sup = ITEMS.register(name, () -> {
            CoinItem item = new CoinItem(region, valueUnit, descKey, new Item.Properties().stacksTo(64));
            if (config != null) {
                config.accept(item);
            }
            return item;
        });
        ALL_COINS.add(sup);
        return sup;
    }

    public static List<RegistrySupplier<CoinItem>> getAllCoins() {
        return Collections.unmodifiableList(ALL_COINS);
    }

    public static void register() {
        ITEMS.register();
    }
}

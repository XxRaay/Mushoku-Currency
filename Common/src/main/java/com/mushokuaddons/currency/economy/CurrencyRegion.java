package com.mushokuaddons.currency.economy;

import com.mushokuaddons.currency.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum CurrencyRegion {
    ASURA("asura", ChatFormatting.GOLD, "currency.region.asura", new String[]{
            "Провинция Фиттоа", "Герцогство Милботс", "Баронство Буэна", "Столичный Округ Арс", "Торговый Город Роа"
    }),
    DEMON("demon", ChatFormatting.DARK_RED, "currency.region.demon", new String[]{
            "Пустоши Рикариса", "Побережье Занта", "Долина Нокапара", "Земли Клана Мигурд", "Степь Вечной Войны"
    }),
    MILLIS("millis", ChatFormatting.AQUA, "currency.region.millis", new String[]{
            "Святая Епархия Миллишиона", "Приходы Святой Земли", "Префектура Порта Зауто", "Орден Храма Миллис", "Западные Долины Миллиса"
    }),
    BEGARITT("begaritt", ChatFormatting.YELLOW, "currency.region.begaritt", new String[]{
            "Торговый Оазис Рапан", "Базары Сухих Дюн", "Лабиринты Золотых Песков", "Караванные Пути Бегаритта", "Кочевья Пустыни"
    });

    private final String id;
    private final ChatFormatting color;
    private final String translationKey;
    private final String[] sampleCommunityNames;

    CurrencyRegion(String id, ChatFormatting color, String translationKey, String[] sampleCommunityNames) {
        this.id = id;
        this.color = color;
        this.translationKey = translationKey;
        this.sampleCommunityNames = sampleCommunityNames;
    }

    public String getId() {
        return id;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public Component getDisplayName() {
        return Component.translatable(translationKey).withStyle(color);
    }

    public String getRandomCommunityName(long seedIndex) {
        int idx = (int) (Math.abs(seedIndex) % sampleCommunityNames.length);
        return sampleCommunityNames[idx];
    }

    public static CurrencyRegion byId(String id) {
        for (CurrencyRegion r : values()) {
            if (r.id.equalsIgnoreCase(id)) {
                return r;
            }
        }
        return ASURA;
    }

    /**
     * Returns the standard coin equivalent to 1 vanilla Emerald (100 value units).
     */
    public Item getStandardTradeCoin() {
        return switch (this) {
            case ASURA -> ModItems.ASURA_LARGE_COPPER_COIN.get();
            case DEMON -> ModItems.DEMON_LARGE_STONE_COIN.get();
            case MILLIS -> ModItems.MILLIS_COPPER_COIN.get(); // 10 copper = 100 units = 1 emerald
            case BEGARITT -> ModItems.BEGARITT_COPPER_COIN.get(); // 10 copper = 100 units = 1 emerald
        };
    }

    public int getStandardTradeCoinCountForOneEmerald() {
        return switch (this) {
            case ASURA -> 1;     // 1 Asura Large Copper = 100 units
            case DEMON -> 1;     // 1 Demon Large Stone = 100 units
            case MILLIS -> 10;   // 10 Millis Copper = 100 units
            case BEGARITT -> 10; // 10 Begaritt Copper = 100 units
        };
    }

    /**
     * Converts an amount of vanilla emeralds into an equivalent list of regional coins.
     */
    public List<ItemStack> convertEmeralds(int emeraldCount) {
        long totalUnits = (long) emeraldCount * 100L;
        return convertUnitsToCoins(totalUnits);
    }

    /**
     * Converts a total value into the best combination of regional coin denominations.
     */
    public List<ItemStack> convertUnitsToCoins(long totalUnits) {
        List<ItemStack> result = new ArrayList<>();
        if (totalUnits <= 0) return result;

        switch (this) {
            case ASURA -> {
                // Gold (10,000), Silver (1,000), Large Copper (100), Copper (10), Iron (1)
                long gold = totalUnits / 10000;
                totalUnits %= 10000;
                long silver = totalUnits / 1000;
                totalUnits %= 1000;
                long largeCopper = totalUnits / 100;
                totalUnits %= 100;
                long copper = totalUnits / 10;
                totalUnits %= 10;
                long iron = totalUnits;

                addStacked(result, ModItems.ASURA_GOLD_COIN.get(), (int) gold);
                addStacked(result, ModItems.ASURA_SILVER_COIN.get(), (int) silver);
                addStacked(result, ModItems.ASURA_LARGE_COPPER_COIN.get(), (int) largeCopper);
                addStacked(result, ModItems.ASURA_COPPER_COIN.get(), (int) copper);
                addStacked(result, ModItems.ASURA_IRON_COIN.get(), (int) iron);
            }
            case DEMON -> {
                // Green Ore (5,000), Large Stone (100), Stone (10), Scrap (1)
                long greenOre = totalUnits / 5000;
                totalUnits %= 5000;
                long largeStone = totalUnits / 100;
                totalUnits %= 100;
                long stone = totalUnits / 10;
                totalUnits %= 10;
                long scrap = totalUnits;

                addStacked(result, ModItems.DEMON_GREEN_ORE_COIN.get(), (int) greenOre);
                addStacked(result, ModItems.DEMON_LARGE_STONE_COIN.get(), (int) largeStone);
                addStacked(result, ModItems.DEMON_STONE_COIN.get(), (int) stone);
                addStacked(result, ModItems.DEMON_SCRAP_COIN.get(), (int) scrap);
            }
            case MILLIS -> {
                // Gold (10,000), Silver (1,000), Copper (10)
                long gold = totalUnits / 10000;
                totalUnits %= 10000;
                long silver = totalUnits / 1000;
                totalUnits %= 1000;
                long copper = totalUnits / 10;

                addStacked(result, ModItems.MILLIS_GOLD_COIN.get(), (int) gold);
                addStacked(result, ModItems.MILLIS_SILVER_COIN.get(), (int) silver);
                addStacked(result, ModItems.MILLIS_COPPER_COIN.get(), (int) copper);
            }
            case BEGARITT -> {
                // Gold (10,000), Silver (1,000), Copper (10)
                long gold = totalUnits / 10000;
                totalUnits %= 10000;
                long silver = totalUnits / 1000;
                totalUnits %= 1000;
                long copper = totalUnits / 10;

                addStacked(result, ModItems.BEGARITT_GOLD_COIN.get(), (int) gold);
                addStacked(result, ModItems.BEGARITT_SILVER_COIN.get(), (int) silver);
                addStacked(result, ModItems.BEGARITT_COPPER_COIN.get(), (int) copper);
            }
        }
        return result;
    }

    private static void addStacked(List<ItemStack> list, Item item, int count) {
        while (count > 0) {
            int stack = Math.min(count, item.getDefaultMaxStackSize());
            list.add(new ItemStack(item, stack));
            count -= stack;
        }
    }
}

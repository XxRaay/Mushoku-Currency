package com.mushokuaddons.currency.economy;

import com.mushokuaddons.currency.init.ModItems;
import com.mushokuaddons.currency.item.CoinItem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.Optional;

public class VillagerTradeConverter {

    public static void convertOffersForVillager(Villager villager, CurrencyRegion region) {
        MerchantOffers offers = villager.getOffers();
        if (offers == null || offers.isEmpty()) return;

        for (int i = 0; i < offers.size(); i++) {
            MerchantOffer original = offers.get(i);
            MerchantOffer converted = convertOffer(original, region);
            if (converted != original) {
                offers.set(i, converted);
            }
        }
    }

    public static MerchantOffer convertOffer(MerchantOffer offer, CurrencyRegion targetRegion) {
        ItemCost costA = offer.getItemCostA();
        Optional<ItemCost> costB = offer.getItemCostB();
        ItemStack result = offer.getResult();

        boolean changed = false;

        // Check Cost A
        ItemCost newCostA = costA;
        if (isEmerald(costA) || isForeignCoin(costA, targetRegion)) {
            int emValue = getEmeraldEquivalent(costA);
            newCostA = createTradeItemCost(targetRegion, emValue);
            changed = true;
        }

        // Check Cost B
        Optional<ItemCost> newCostB = costB;
        if (costB.isPresent()) {
            ItemCost b = costB.get();
            if (isEmerald(b) || isForeignCoin(b, targetRegion)) {
                int emValue = getEmeraldEquivalent(b);
                newCostB = Optional.of(createTradeItemCost(targetRegion, emValue));
                changed = true;
            }
        }

        // Check Result
        ItemStack newResult = result;
        if (result.is(Items.EMERALD) || isForeignCoin(result, targetRegion)) {
            int emValue = getEmeraldEquivalent(result);
            newResult = createTradeItemStack(targetRegion, emValue);
            changed = true;
        }

        if (!changed) {
            return offer;
        }

        // Create new offer retaining original parameters
        MerchantOffer newOffer = new MerchantOffer(
                newCostA,
                newCostB,
                newResult,
                offer.getUses(),
                offer.getMaxUses(),
                offer.getXp(),
                offer.getPriceMultiplier(),
                offer.getDemand()
        );
        newOffer.setSpecialPriceDiff(offer.getSpecialPriceDiff());
        return newOffer;
    }

    private static boolean isEmerald(ItemCost cost) {
        return cost.item().value() == Items.EMERALD;
    }

    private static boolean isForeignCoin(ItemCost cost) {
        Item item = cost.item().value();
        return item instanceof CoinItem;
    }

    private static boolean isForeignCoin(ItemCost cost, CurrencyRegion targetRegion) {
        Item item = cost.item().value();
        if (item instanceof CoinItem coin) {
            return coin.getRegion() != targetRegion;
        }
        return false;
    }

    private static boolean isForeignCoin(ItemStack stack, CurrencyRegion targetRegion) {
        if (stack.getItem() instanceof CoinItem coin) {
            return coin.getRegion() != targetRegion;
        }
        return false;
    }

    private static int getEmeraldEquivalent(ItemCost cost) {
        Item item = cost.item().value();
        if (item.equals(Items.EMERALD)) {
            return cost.count();
        }
        if (item instanceof CoinItem coin) {
            long totalUnits = coin.getValueUnit() * cost.count();
            return (int) Math.max(1, totalUnits / 100);
        }
        return cost.count();
    }

    private static int getEmeraldEquivalent(ItemStack stack) {
        if (stack.is(Items.EMERALD)) {
            return stack.getCount();
        }
        if (stack.getItem() instanceof CoinItem coin) {
            long totalUnits = coin.getValueUnit() * stack.getCount();
            return (int) Math.max(1, totalUnits / 100);
        }
        return stack.getCount();
    }

    public static ItemCost createTradeItemCost(CurrencyRegion region, int emeraldCount) {
        CoinCostData data = computeCoinAndCount(region, emeraldCount);
        return new ItemCost(data.item, data.count);
    }

    public static ItemStack createTradeItemStack(CurrencyRegion region, int emeraldCount) {
        CoinCostData data = computeCoinAndCount(region, emeraldCount);
        return new ItemStack(data.item, data.count);
    }

    private record CoinCostData(Item item, int count) {}

    private static CoinCostData computeCoinAndCount(CurrencyRegion region, int emeraldCount) {
        int count = Math.max(1, emeraldCount);

        switch (region) {
            case ASURA -> {
                // 1 emerald = 1 Asura Large Copper
                if (count >= 10 && count % 10 == 0) {
                    return new CoinCostData(ModItems.ASURA_SILVER_COIN.get(), count / 10);
                }
                return new CoinCostData(ModItems.ASURA_LARGE_COPPER_COIN.get(), Math.min(64, count));
            }
            case DEMON -> {
                // 1 emerald = 1 Demon Large Stone
                if (count >= 50 && count % 50 == 0) {
                    return new CoinCostData(ModItems.DEMON_GREEN_ORE_COIN.get(), count / 50);
                }
                return new CoinCostData(ModItems.DEMON_LARGE_STONE_COIN.get(), Math.min(64, count));
            }
            case MILLIS -> {
                // 1 emerald = 10 Millis Copper (or 1/10 Silver)
                if (count >= 10) {
                    return new CoinCostData(ModItems.MILLIS_SILVER_COIN.get(), Math.min(64, count / 10));
                }
                return new CoinCostData(ModItems.MILLIS_COPPER_COIN.get(), Math.min(64, count * 10));
            }
            case BEGARITT -> {
                // 1 emerald = 10 Begaritt Copper (or 1/10 Silver)
                if (count >= 10) {
                    return new CoinCostData(ModItems.BEGARITT_SILVER_COIN.get(), Math.min(64, count / 10));
                }
                return new CoinCostData(ModItems.BEGARITT_COPPER_COIN.get(), Math.min(64, count * 10));
            }
        }
        return new CoinCostData(ModItems.ASURA_LARGE_COPPER_COIN.get(), count);
    }
}

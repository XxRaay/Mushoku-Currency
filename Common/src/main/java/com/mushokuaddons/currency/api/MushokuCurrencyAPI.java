package com.mushokuaddons.currency.api;

import com.mushokuaddons.currency.economy.CurrencyRegion;
import com.mushokuaddons.currency.economy.VillageCommunityInfo;
import com.mushokuaddons.currency.economy.VillageEconomyManager;
import com.mushokuaddons.currency.init.ModItems;
import com.mushokuaddons.currency.item.CoinItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class MushokuCurrencyAPI {

    /**
     * Gets the currency region for a given world position.
     */
    public static CurrencyRegion getRegionAt(Level level, BlockPos pos) {
        return VillageEconomyManager.getRegionAt(level, pos);
    }

    /**
     * Gets the currency region for a specific villager based on its location.
     */
    public static CurrencyRegion getRegionForVillager(Villager villager) {
        return VillageEconomyManager.getRegionForVillager(villager);
    }

    /**
     * Gets community info (name, cluster, region) for a villager.
     */
    public static VillageCommunityInfo getCommunityForVillager(Villager villager) {
        return VillageEconomyManager.getCommunityForVillager(villager);
    }

    /**
     * Calculates total currency value of a single ItemStack in standard units.
     */
    public static long getItemValue(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        if (stack.getItem() instanceof CoinItem coin) {
            return coin.getValueUnit() * stack.getCount();
        }
        if (stack.is(Items.EMERALD)) {
            return 100L * stack.getCount();
        }
        return 0;
    }

    /**
     * Returns the region associated with the given item stack, or null if not a coin.
     */
    public static CurrencyRegion getItemRegion(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof CoinItem coin) {
            return coin.getRegion();
        }
        return null;
    }

    /**
     * Counts the total value of coins belonging to a specific region in the player's inventory.
     */
    public static long getPlayerBalance(Player player, CurrencyRegion region) {
        long total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof CoinItem coin && coin.getRegion() == region) {
                total += coin.getValueUnit() * stack.getCount();
            }
        }
        return total;
    }

    /**
     * Counts total value of ANY coins and emeralds in player's inventory in standard units.
     */
    public static long getTotalInventoryValue(Player player) {
        long total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            total += getItemValue(stack);
        }
        return total;
    }

    /**
     * Deducts currency of a specific region from the player's inventory.
     * Returns true if successfully deducted, or false if insufficient balance.
     * If change is needed, gives back the change in smaller coins.
     */
    public static boolean deductCurrency(Player player, CurrencyRegion region, long requiredUnits) {
        long currentBalance = getPlayerBalance(player, region);
        if (currentBalance < requiredUnits) {
            return false;
        }

        long remainingToDeduct = requiredUnits;

        // Pass 1: remove coins directly
        for (int i = 0; i < player.getInventory().getContainerSize() && remainingToDeduct > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof CoinItem coin && coin.getRegion() == region) {
                long coinVal = coin.getValueUnit();
                int countToTake = (int) Math.min(stack.getCount(), (remainingToDeduct + coinVal - 1) / coinVal);
                long takenValue = countToTake * coinVal;

                stack.shrink(countToTake);
                remainingToDeduct -= takenValue;
            }
        }

        // If over-deducted, give change
        if (remainingToDeduct < 0) {
            long change = -remainingToDeduct;
            giveCurrency(player, region, change);
        }

        return true;
    }

    /**
     * Gives currency to a player in optimal coin denominations.
     */
    public static void giveCurrency(Player player, CurrencyRegion region, long totalUnits) {
        if (totalUnits <= 0) return;
        List<ItemStack> coins = region.convertUnitsToCoins(totalUnits);
        for (ItemStack coinStack : coins) {
            if (!player.getInventory().add(coinStack)) {
                player.drop(coinStack, false);
            }
        }
    }

    /**
     * Pays a guild quest reward or similar in local village currency based on standard emerald value.
     */
    public static void payGuildReward(ServerPlayer player, BlockPos location, int emeraldEquivalent) {
        CurrencyRegion region = getRegionAt(player.serverLevel(), location);
        long totalUnits = (long) emeraldEquivalent * 100L;
        giveCurrency(player, region, totalUnits);
    }

    /**
     * Exchanges currency from one region to another with optional commission (e.g., 0.05 for 5% guild commission).
     * Returns the amount of target currency units given to player.
     */
    public static long exchange(Player player, CurrencyRegion fromRegion, CurrencyRegion toRegion, long fromUnits, double feeRate) {
        if (fromUnits <= 0) return 0;
        if (!deductCurrency(player, fromRegion, fromUnits)) {
            return 0;
        }

        long feeUnits = (long) (fromUnits * Math.max(0.0, Math.min(0.5, feeRate)));
        long netUnits = fromUnits - feeUnits;

        giveCurrency(player, toRegion, netUnits);
        return netUnits;
    }
}

package com.mushokuaddons.currency.item;

import com.mushokuaddons.currency.economy.CurrencyRegion;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class CoinItem extends Item {
    private final CurrencyRegion region;
    private final long valueUnit;
    private final String descKey;

    private Supplier<? extends Item> higherCoinSupplier;
    private int higherCoinCost = 10;

    private Supplier<? extends Item> lowerCoinSupplier;
    private int lowerCoinYield = 10;

    public CoinItem(CurrencyRegion region, long valueUnit, String descKey, Properties properties) {
        super(properties);
        this.region = region;
        this.valueUnit = valueUnit;
        this.descKey = descKey;
    }

    public CoinItem setHigherCoin(Supplier<? extends Item> higherSupplier, int cost) {
        this.higherCoinSupplier = higherSupplier;
        this.higherCoinCost = cost;
        return this;
    }

    public CoinItem setLowerCoin(Supplier<? extends Item> lowerSupplier, int yield) {
        this.lowerCoinSupplier = lowerSupplier;
        this.lowerCoinYield = yield;
        return this;
    }

    public CurrencyRegion getRegion() {
        return region;
    }

    public long getValueUnit() {
        return valueUnit;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);

        if (!level.isClientSide && player.isShiftKeyDown()) {
            // Priority 1: Combine into higher denomination if enough coins
            if (higherCoinSupplier != null && higherCoinSupplier.get() != null && held.getCount() >= higherCoinCost) {
                held.shrink(higherCoinCost);
                ItemStack higherStack = new ItemStack(higherCoinSupplier.get(), 1);
                if (!player.getInventory().add(higherStack)) {
                    player.drop(higherStack, false);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7f, 1.2f);
                return InteractionResultHolder.sidedSuccess(held, false);
            }

            // Priority 2: Split into lower denomination if 1 item in hand
            if (lowerCoinSupplier != null && lowerCoinSupplier.get() != null) {
                held.shrink(1);
                ItemStack lowerStack = new ItemStack(lowerCoinSupplier.get(), lowerCoinYield);
                if (!player.getInventory().add(lowerStack)) {
                    player.drop(lowerStack, false);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.7f, 1.0f);
                return InteractionResultHolder.sidedSuccess(held, false);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.mushokucurrency.region", region.getDisplayName()));
        tooltipComponents.add(Component.translatable("tooltip.mushokucurrency.value", String.format("%,d", valueUnit)));

        if (valueUnit >= 100) {
            long em = valueUnit / 100;
            tooltipComponents.add(Component.translatable("tooltip.mushokucurrency.emerald_eq", em));
        }

        tooltipComponents.add(Component.translatable(descKey).withStyle(ChatFormatting.GRAY));

        if (higherCoinSupplier != null || lowerCoinSupplier != null) {
            tooltipComponents.add(Component.translatable("tooltip.mushokucurrency.shift_hint"));
        }
    }
}

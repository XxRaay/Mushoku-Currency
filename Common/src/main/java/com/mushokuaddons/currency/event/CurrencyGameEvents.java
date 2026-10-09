package com.mushokuaddons.currency.event;

import com.mushokuaddons.currency.economy.CurrencyRegion;
import com.mushokuaddons.currency.economy.VillageCommunityInfo;
import com.mushokuaddons.currency.economy.VillageEconomyManager;
import com.mushokuaddons.currency.economy.VillagerTradeConverter;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyGameEvents {
    private static final Map<UUID, Long> LAST_NOTIFICATION_TIME = new ConcurrentHashMap<>();

    public static void register() {
        // Intercept right-clicking on Villagers to convert emerald trades to regional coins
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (entity instanceof Villager villager && !player.level().isClientSide()) {
                CurrencyRegion region = VillageEconomyManager.getRegionForVillager(villager);
                VillageCommunityInfo community = VillageEconomyManager.getCommunityForVillager(villager);

                // Convert all trades to use local community currency
                VillagerTradeConverter.convertOffersForVillager(villager, region);

                // Notify player in action bar (with debounce of 15 seconds)
                long now = System.currentTimeMillis();
                UUID pId = player.getUUID();
                Long last = LAST_NOTIFICATION_TIME.get(pId);
                if (last == null || (now - last) > 15000L) {
                    LAST_NOTIFICATION_TIME.put(pId, now);
                    player.displayClientMessage(
                            Component.translatable("message.mushokucurrency.trade_converted", community.getName(), region.getDisplayName()),
                            true
                    );
                }
            }
            return EventResult.pass();
        });
    }
}

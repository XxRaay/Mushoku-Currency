package com.mushokuaddons.currency.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mushokuaddons.currency.api.MushokuCurrencyAPI;
import com.mushokuaddons.currency.economy.CurrencyRegion;
import com.mushokuaddons.currency.economy.VillageCommunityInfo;
import com.mushokuaddons.currency.economy.VillageEconomyManager;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CurrencyCommands {

    public static void register() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("currency")
                    // /currency balance
                    .then(Commands.literal("balance")
                            .executes(CurrencyCommands::showBalance))
                    // /currency village info
                    .then(Commands.literal("village")
                            .then(Commands.literal("info")
                                    .executes(CurrencyCommands::villageInfo))
                            .then(Commands.literal("set")
                                    .requires(src -> src.hasPermission(2))
                                    .then(Commands.argument("region", StringArgumentType.word())
                                            .executes(ctx -> setVillageRegion(ctx, null))
                                            .then(Commands.argument("customName", StringArgumentType.greedyString())
                                                    .executes(ctx -> setVillageRegion(ctx, StringArgumentType.getString(ctx, "customName")))))))
                    // /currency give <targets> <region> <amount> (admin)
                    .then(Commands.literal("give")
                            .requires(src -> src.hasPermission(2))
                            .then(Commands.argument("targets", EntityArgument.players())
                                    .then(Commands.argument("region", StringArgumentType.word())
                                            .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                    .executes(CurrencyCommands::giveCurrency)))))
            );
        });
    }

    private static int showBalance(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        player.sendSystemMessage(Component.translatable("message.mushokucurrency.balance_title"));
        for (CurrencyRegion r : CurrencyRegion.values()) {
            long bal = MushokuCurrencyAPI.getPlayerBalance(player, r);
            player.sendSystemMessage(Component.literal("• ").append(r.getDisplayName()).append(Component.literal(": §f" + String.format("%,d", bal) + " §7ед.")));
        }
        return 1;
    }

    private static int villageInfo(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        BlockPos pos = player.blockPosition();
        VillageCommunityInfo info = VillageEconomyManager.getOrCreateCommunity(player.serverLevel(), pos);
        int cx = Math.floorDiv(pos.getX(), VillageEconomyManager.CLUSTER_SIZE);
        int cz = Math.floorDiv(pos.getZ(), VillageEconomyManager.CLUSTER_SIZE);

        player.sendSystemMessage(Component.translatable("message.mushokucurrency.village_info",
                info.getName(),
                info.getRegion().getDisplayName(),
                cx, cz
        ));
        return 1;
    }

    private static int setVillageRegion(CommandContext<CommandSourceStack> ctx, String customName) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        String regStr = StringArgumentType.getString(ctx, "region");
        CurrencyRegion reg = CurrencyRegion.byId(regStr);
        BlockPos pos = player.blockPosition();

        VillageEconomyManager.setRegionForCluster(player.serverLevel(), pos, reg, customName);
        VillageCommunityInfo updated = VillageEconomyManager.getOrCreateCommunity(player.serverLevel(), pos);

        ctx.getSource().sendSuccess(() ->
                Component.translatable("command.mushokucurrency.region_updated", updated.getName(), reg.getDisplayName()),
                true
        );
        return 1;
    }

    private static int giveCurrency(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String regStr = StringArgumentType.getString(ctx, "region");
        CurrencyRegion reg = CurrencyRegion.byId(regStr);
        int amount = IntegerArgumentType.getInteger(ctx, "amount");

        for (ServerPlayer player : EntityArgument.getPlayers(ctx, "targets")) {
            MushokuCurrencyAPI.giveCurrency(player, reg, amount);
            player.sendSystemMessage(Component.translatable("message.mushokucurrency.received_currency", amount, reg.getDisplayName()));
        }

        ctx.getSource().sendSuccess(() -> Component.translatable("command.mushokucurrency.give_success"), true);
        return 1;
    }
}

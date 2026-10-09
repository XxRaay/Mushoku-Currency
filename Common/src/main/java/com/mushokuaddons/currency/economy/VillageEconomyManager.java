package com.mushokuaddons.currency.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Random;

public class VillageEconomyManager {
    public static final int CLUSTER_SIZE = 1024; // 1024 blocks per economic province/community cluster

    public static String getClusterKey(BlockPos pos) {
        int cx = Math.floorDiv(pos.getX(), CLUSTER_SIZE);
        int cz = Math.floorDiv(pos.getZ(), CLUSTER_SIZE);
        return cx + "_" + cz;
    }

    public static VillageCommunityInfo getOrCreateCommunity(ServerLevel level, BlockPos pos) {
        VillageEconomySavedData data = VillageEconomySavedData.get(level);
        String key = getClusterKey(pos);
        VillageCommunityInfo info = data.getCommunity(key);

        if (info == null) {
            info = generateCommunity(level, pos, key);
            data.putCommunity(info);
        }

        return info;
    }

    public static CurrencyRegion getRegionAt(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            return getOrCreateCommunity(serverLevel, pos).getRegion();
        }
        return CurrencyRegion.ASURA;
    }

    public static VillageCommunityInfo getCommunityForVillager(Villager villager) {
        if (villager.level() instanceof ServerLevel serverLevel) {
            return getOrCreateCommunity(serverLevel, villager.blockPosition());
        }
        return new VillageCommunityInfo("client", "Община Асуры", CurrencyRegion.ASURA);
    }

    public static CurrencyRegion getRegionForVillager(Villager villager) {
        return getCommunityForVillager(villager).getRegion();
    }

    public static void setRegionForCluster(ServerLevel level, BlockPos pos, CurrencyRegion region, String customName) {
        VillageEconomySavedData data = VillageEconomySavedData.get(level);
        String key = getClusterKey(pos);
        VillageCommunityInfo info = data.getCommunity(key);

        if (info == null) {
            info = new VillageCommunityInfo(key, customName != null ? customName : region.getRandomCommunityName(0), region);
        } else {
            info.setRegion(region);
            if (customName != null && !customName.isBlank()) {
                info.setName(customName);
            }
        }
        data.putCommunity(info);
    }

    private static VillageCommunityInfo generateCommunity(ServerLevel level, BlockPos pos, String key) {
        long seed = level.getSeed();
        int cx = Math.floorDiv(pos.getX(), CLUSTER_SIZE);
        int cz = Math.floorDiv(pos.getZ(), CLUSTER_SIZE);

        // Deterministic hash combining seed and cluster coordinates
        long hash = seed ^ ((long) cx * 341873128712L + (long) cz * 132897987541L);
        Random random = new Random(hash);

        Holder<Biome> biomeHolder = level.getBiome(pos);
        CurrencyRegion region = determineRegionByBiome(biomeHolder, random);
        String communityName = region.getRandomCommunityName(hash);

        return new VillageCommunityInfo(key, communityName, region);
    }

    private static CurrencyRegion determineRegionByBiome(Holder<Biome> biomeHolder, Random random) {
        int roll = random.nextInt(100);

        if (biomeHolder.is(Biomes.DESERT) || biomeHolder.is(Biomes.BADLANDS) || biomeHolder.is(Biomes.ERODED_BADLANDS)) {
            // Hot dry wasteland -> Begaritt (70%) or Demon (30%)
            return (roll < 70) ? CurrencyRegion.BEGARITT : CurrencyRegion.DEMON;
        }

        if (biomeHolder.is(Biomes.SNOWY_PLAINS) || biomeHolder.is(Biomes.SNOWY_TAIGA) ||
            biomeHolder.is(Biomes.SNOWY_SLOPES) || biomeHolder.is(Biomes.JAGGED_PEAKS) ||
            biomeHolder.is(Biomes.FROZEN_PEAKS) || biomeHolder.is(Biomes.GROVE)) {
            // Cold mountainous / holy lands -> Millis (70%) or Asura (30%)
            return (roll < 70) ? CurrencyRegion.MILLIS : CurrencyRegion.ASURA;
        }

        if (biomeHolder.is(Biomes.SWAMP) || biomeHolder.is(Biomes.MANGROVE_SWAMP) ||
            biomeHolder.is(Biomes.DARK_FOREST) || biomeHolder.is(Biomes.NETHER_WASTES) ||
            biomeHolder.is(Biomes.SOUL_SAND_VALLEY) || biomeHolder.is(Biomes.BASALT_DELTAS)) {
            // Dark / dangerous lands -> Demon Continent (75%) or Asura (25%)
            return (roll < 75) ? CurrencyRegion.DEMON : CurrencyRegion.ASURA;
        }

        // Fertile plains / forests / savannas / rivers:
        // Asura is the dominant wealthy kingdom (60%), followed by Millis (15%), Begaritt (15%), Demon (10%)
        if (roll < 60) return CurrencyRegion.ASURA;
        if (roll < 75) return CurrencyRegion.MILLIS;
        if (roll < 90) return CurrencyRegion.BEGARITT;
        return CurrencyRegion.DEMON;
    }
}

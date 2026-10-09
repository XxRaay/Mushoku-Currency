package com.mushokuaddons.currency.economy;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VillageEconomySavedData extends SavedData {
    public static final String DATA_NAME = "mushoku_village_economy";

    private final Map<String, VillageCommunityInfo> communities = new ConcurrentHashMap<>();

    public static VillageEconomySavedData get(ServerLevel level) {
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new Factory<>(VillageEconomySavedData::new, VillageEconomySavedData::load, null),
                DATA_NAME
        );
    }

    public VillageCommunityInfo getCommunity(String clusterKey) {
        return communities.get(clusterKey);
    }

    public void putCommunity(VillageCommunityInfo info) {
        communities.put(info.getClusterKey(), info);
        setDirty();
    }

    public Map<String, VillageCommunityInfo> getAllCommunities() {
        return communities;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (VillageCommunityInfo info : communities.values()) {
            list.add(info.saveToTag());
        }
        tag.put("communities", list);
        return tag;
    }

    public static VillageEconomySavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        VillageEconomySavedData data = new VillageEconomySavedData();
        if (tag.contains("communities", Tag.TAG_LIST)) {
            ListTag list = tag.getList("communities", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                VillageCommunityInfo info = VillageCommunityInfo.loadFromTag(list.getCompound(i));
                data.communities.put(info.getClusterKey(), info);
            }
        }
        return data;
    }
}

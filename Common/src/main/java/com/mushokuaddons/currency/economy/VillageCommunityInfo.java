package com.mushokuaddons.currency.economy;

import net.minecraft.nbt.CompoundTag;

public class VillageCommunityInfo {
    private final String clusterKey;
    private String name;
    private CurrencyRegion region;

    public VillageCommunityInfo(String clusterKey, String name, CurrencyRegion region) {
        this.clusterKey = clusterKey;
        this.name = name;
        this.region = region;
    }

    public String getClusterKey() {
        return clusterKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrencyRegion getRegion() {
        return region;
    }

    public void setRegion(CurrencyRegion region) {
        this.region = region;
    }

    public CompoundTag saveToTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("clusterKey", clusterKey);
        tag.putString("name", name);
        tag.putString("region", region.getId());
        return tag;
    }

    public static VillageCommunityInfo loadFromTag(CompoundTag tag) {
        String key = tag.getString("clusterKey");
        String name = tag.getString("name");
        CurrencyRegion reg = CurrencyRegion.byId(tag.getString("region"));
        return new VillageCommunityInfo(key, name, reg);
    }
}

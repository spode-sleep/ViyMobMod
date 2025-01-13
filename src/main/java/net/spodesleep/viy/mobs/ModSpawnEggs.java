package net.spodesleep.viy.mobs;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.spodesleep.viy.ViyMobMod;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModSpawnEggs {
    public static final RegistryKey<ItemGroup> MOBS = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(ViyMobMod.MOD_ID, "viy_eggs"));

    public static final Item VIY_SPAWN_EGG = new SpawnEggItem(ModMobs.VIY, 16382451, 12369044, new Item.Settings());

    public static void registerSpawnEggs(){
       
        Registry.register(Registries.ITEM, new Identifier(ViyMobMod.MOD_ID, "viy_spawn_egg"), VIY_SPAWN_EGG);

        Registry.register(Registries.ITEM_GROUP, MOBS, FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.SNOW_GOLEM_SPAWN_EGG))
                .displayName(Text.literal("Viy"))
                .entries((enabledFeatures, entries) -> {
                    entries.add(ModSpawnEggs.VIY_SPAWN_EGG);
                })
                .build());
    }
}

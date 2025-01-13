package net.spodesleep.viy.mobs;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.spodesleep.viy.ViyMobMod;
import net.spodesleep.viy.mobs.overworld.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModMobs {

    public static final EntityType<ViyEntity> VIY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ViyMobMod.MOD_ID, "viy"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ViyEntity::new)
                    .dimensions(EntityDimensions.fixed(4.0F, 4.0F)).build());

    public static final EntityType<SnowballProjectile> SNOWBALL_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(ViyMobMod.MOD_ID, "snowball_projectile"),
            FabricEntityTypeBuilder.<SnowballProjectile>create(SpawnGroup.MISC, SnowballProjectile::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .build());

    public static void registerModMobs() {
        FabricDefaultAttributeRegistry.register(VIY, ViyEntity.createNightmareAttributes());
        SpawnRules.initialize();
    }
}
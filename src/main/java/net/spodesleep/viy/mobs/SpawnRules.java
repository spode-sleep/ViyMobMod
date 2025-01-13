package net.spodesleep.viy.mobs;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.Heightmap;

public class SpawnRules {
    public static void initialize() {

        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.tag(BiomeTags.IS_OCEAN).or(BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN)))
                        .and(BiomeSelectors.tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)),
                SpawnGroup.MONSTER,
                ModMobs.VIY,
                50,
                1,
                1);

        SpawnRestriction.register(
                ModMobs.VIY,
                SpawnRestriction.Location.NO_RESTRICTIONS,
                Heightmap.Type.WORLD_SURFACE,
                (type, world, spawnReason, pos, random) -> pos.getY() > 60);
    }
}
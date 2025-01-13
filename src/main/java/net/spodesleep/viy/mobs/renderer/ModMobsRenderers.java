package net.spodesleep.viy.mobs.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.spodesleep.viy.mobs.ModMobs;
import net.spodesleep.viy.mobs.renderer.overworld.*;

@Environment(EnvType.CLIENT)
public class ModMobsRenderers {

    public static void registerMobRenderers(){

        EntityRendererRegistry.register(ModMobs.VIY, (context) -> {
            return new ViyEntityRenderer(context);
        });

        EntityRendererRegistry.register(ModMobs.SNOWBALL_PROJECTILE, SnowballProjectileRenderer::new);

    }
}

package net.spodesleep.viy.mobs.renderer.overworld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.spodesleep.viy.ViyMobMod;
import net.minecraft.entity.projectile.DragonFireballEntity;

@Environment(EnvType.CLIENT)
public class SnowballProjectileRenderer extends DragonFireballEntityRenderer {
    private static final Identifier TEXTURE = new Identifier(ViyMobMod.MOD_ID, "textures/entity/projectiles/snowball_projectile.png");

    public SnowballProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(DragonFireballEntity entity) {
        return TEXTURE;
    }
}
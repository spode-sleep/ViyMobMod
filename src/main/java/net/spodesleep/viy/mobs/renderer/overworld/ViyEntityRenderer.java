package net.spodesleep.viy.mobs.renderer.overworld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.spodesleep.viy.ViyMobMod;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ViyEntityRenderer extends GhastEntityRenderer {
    private static final Identifier TEXTURE =
            new Identifier(ViyMobMod.MOD_ID, "textures/entity/ghast/viy.png");

    private static final Identifier ANGRY_TEXTURE =
            new Identifier(ViyMobMod.MOD_ID, "textures/entity/ghast/viy_angry.png");

    public ViyEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }


    public Identifier getTexture(GhastEntity ghastEntity) {
        return ghastEntity.isShooting() ? ANGRY_TEXTURE : TEXTURE;
    }

}

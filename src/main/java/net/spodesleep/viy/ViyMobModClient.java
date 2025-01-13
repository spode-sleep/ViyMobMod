package net.spodesleep.viy;

import net.fabricmc.api.ClientModInitializer;
import net.spodesleep.viy.mobs.renderer.ModMobsRenderers;

public class ViyMobModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMobsRenderers.registerMobRenderers();

    }
}

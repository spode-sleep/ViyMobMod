package net.spodesleep.viy;

import net.fabricmc.api.ModInitializer;
import net.spodesleep.viy.mobs.ModMobs;
import net.spodesleep.viy.mobs.ModSpawnEggs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViyMobMod implements ModInitializer {
	public static final String MOD_ID = "viy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModMobs.registerModMobs();
		ModSpawnEggs.registerSpawnEggs();
	}
}

package com.birblett;

import com.birblett.network.AttachedDataRegistry;
import com.birblett.network.PayloadRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Orchid implements ModInitializer {

	public static final String MOD_ID = "orchid";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PayloadRegistry.register();
		AttachedDataRegistry.init();
	}

}
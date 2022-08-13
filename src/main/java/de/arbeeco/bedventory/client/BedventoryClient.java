package de.arbeeco.bedventory.client;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import de.arbeeco.bedventory.config.BedventoryConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class BedventoryClient implements ClientModInitializer {
	public static final Logger logger = LogManager.getLogger();
	public static volatile BedventoryConfig config;

	public static final Jankson jankson = Jankson.builder().build();

	@Override
	public void onInitializeClient(ModContainer mod) {
		config = loadConfig();
	}

	@SuppressWarnings("deprecation")
	public static BedventoryConfig loadConfig() {
		try {
			File file = new File(QuiltLoader.getConfigDir().toString(),"bedventory.json");

			if (!file.exists()) saveConfig(new BedventoryConfig());

			JsonObject json = jankson.load(file);
			config =  jankson.fromJson(json, BedventoryConfig.class);
		} catch (Exception e) {
			logger.error("Error loading config: {}", e.getMessage());
		}
		return config;
	}

	@SuppressWarnings("deprecation")
	public static void saveConfig(BedventoryConfig config) {
		try {
			File file = new File(QuiltLoader.getConfigDir().toString(),"bedventory.json");

			JsonElement json = jankson.toJson(config);
			String result = json.toJson(true, true);
			try (FileOutputStream out = new FileOutputStream(file, false)) {
				out.write(result.getBytes(StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			logger.error("Error saving config: {}", e.getMessage());
		}
	}
}

package de.arbeeco.bedventory.config;

import blue.endless.jankson.Comment;

public class BedventoryConfig {
	@Comment("Should the Inventory be shown while sleeping?")
	public boolean showInventoryInBed = true;
}

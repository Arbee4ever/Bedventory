package de.arbeeco.bedventory.mixin;

import de.arbeeco.bedventory.client.BedventoryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {
	@Shadow
	@Nullable
	public Screen screen;

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
	public void setScreen(Minecraft instance, Screen screen) {
		if (screen == null) {
			instance.setScreen(screen);
			return;
		}
		if (BedventoryClient.config.showInventoryInBed) {
			instance.setScreen(new InventoryScreen(instance.player));
			return;
		}
		instance.setScreen(screen);
	}
}

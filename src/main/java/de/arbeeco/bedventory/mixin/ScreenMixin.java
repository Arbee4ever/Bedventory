package de.arbeeco.bedventory.mixin;

import de.arbeeco.bedventory.client.BedventoryClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin {
	@Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;onClose()V"))
	public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		if (!BedventoryClient.config.showInventoryInBed) return;
		if (Minecraft.getInstance().player == null) return;
		if (!Minecraft.getInstance().player.isSleeping()) return;
		ClientPacketListener clientPacketListener = Minecraft.getInstance().player.connection;
		clientPacketListener.send(new ServerboundPlayerCommandPacket(Minecraft.getInstance().player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
	}
}

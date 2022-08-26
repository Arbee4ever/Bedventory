package de.arbeeco.bedventory.mixin;

import de.arbeeco.bedventory.client.BedventoryClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {
	public InventoryScreenMixin(InventoryMenu abstractContainerMenu, Inventory inventory, Component component) {
		super(abstractContainerMenu, inventory, component);
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;addWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
	public void init(CallbackInfo ci) {
		if(minecraft == null) return;
		if(minecraft.player == null) return;
		if (!minecraft.player.isSleeping()) return;
		if(!BedventoryClient.config.showInventoryInBed) return;
		addRenderableWidget(new Button(this.width / 2 - 100, this.height - 30, 200, 20, Component.translatable("multiplayer.stopSleeping"), (button) -> {
			onClose();
			ClientPacketListener clientPacketListener = minecraft.player.connection;
			clientPacketListener.send(new ServerboundPlayerCommandPacket(minecraft.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
		}));
	}
}

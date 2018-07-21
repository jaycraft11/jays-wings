package com.pau101.wings.client;

import baubles.api.render.IRenderBauble;
import com.pau101.wings.Proxy;
import com.pau101.wings.client.renderer.WingsRenderer;
import com.pau101.wings.server.capability.Flight;
import com.pau101.wings.server.item.WingsItems;
import com.pau101.wings.server.net.serverbound.MessageControlFlying;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class ClientProxy extends Proxy {
	private final WingsRenderer wingsRenderer = new WingsRenderer();

	@Override
	public void init() {
		super.init();
		ItemColors colors = Minecraft.getMinecraft().getItemColors();
		colors.registerItemColorHandler((stack, pass) -> pass == 0 ? 0x9B172D : 0xFFFFFF, WingsItems.BAT_BLOOD);
	}

	@Override
	public void renderWings(ItemStack stack, EntityPlayer player, IRenderBauble.RenderType type, float delta) {
		wingsRenderer.render(stack, player, type, delta);
	}

	@Override
	public void addFlightListeners(EntityPlayer player, Flight flight) {
		if (player.isUser()) {
			Flight.Notifier notifier = Flight.Notifier.of(
				() -> {},
				p -> {},
				() -> network.sendToServer(new MessageControlFlying(flight.isFlying()))
			);
			flight.registerSyncListener(players -> players.notify(notifier));
		}
	}
}
package com.pau101.wings.server;

import com.pau101.wings.WingsMod;
import com.pau101.wings.server.asm.PlayerFlightCheckEvent;
import com.pau101.wings.server.capability.FlightCapability;
import com.pau101.wings.server.item.WingsItems;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = WingsMod.ID)
public final class ServerEventHandler {
	private ServerEventHandler() {}

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteract event) {
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack stack = player.getHeldItem(hand);
		if (event.getTarget() instanceof EntityBat && stack.getItem() == Items.GLASS_BOTTLE) {
			player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			ItemStack destroyed = stack.copy();
			if (!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}
			player.addStat(StatList.getObjectUseStats(Items.GLASS_BOTTLE));
			ItemStack batBlood = new ItemStack(WingsItems.BAT_BLOOD);
			if (stack.isEmpty()) {
				ForgeEventFactory.onPlayerDestroyItem(player, destroyed, hand);
				player.setHeldItem(hand, batBlood);
			} else if (!player.inventory.addItemStackToInventory(batBlood)) {
				player.dropItem(batBlood, false);
			}
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}

	@SubscribeEvent
	public static void onMount(EntityMountEvent event) {
		if (event.isMounting()) {
			FlightCapability.ifPlayer(event.getEntityMounting(), (p, f) -> {
				if (f.isFlying()) {
					event.setCanceled(true);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onFlightCheck(PlayerFlightCheckEvent event) {
		if (FlightCapability.get(event.getEntityPlayer()).isFlying()) {
			event.setResult(Event.Result.ALLOW);
		}
	} 
}

package com.djczq.minecraft.wurst.utils;

import java.util.function.Predicate;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.wurstclient.WurstClient;

public class InventoryUtils {

	protected static final MinecraftClient mc = WurstClient.MC;

	public static int getStartOfPlayerInv() {
		if (mc.player.currentScreenHandler instanceof LecternScreenHandler)
			return 10;
		else if (mc.player.currentScreenHandler instanceof MerchantScreenHandler)
			return 3;
		else if (mc.player.currentScreenHandler instanceof GenericContainerScreenHandler)
			return mc.player.currentScreenHandler.slots.size() - 36;
		else if (mc.player.currentScreenHandler instanceof PlayerScreenHandler)
			return 9;
		return -1;
	}

	public static int getStartOfPlayerHotBar() {
		return getStartOfPlayerInv() + 27;
	}

	public static int getOffhandSlot() {
		return 45;
	}

	public static int findSlot(int start, int end, Predicate<ItemStack> pred) {
		if (start == -1)
			return -1;

		for (int i = start; i < end; i++) {
			if (pred.test(getStack(i))) {
				return i;
			}
		}
		return -1;
	}

	public static int getInventorySize() {
		return mc.player.currentScreenHandler.slots.size();
	}

	public static ItemStack getStack(int i) {
		return mc.player.currentScreenHandler.getSlot(i).getStack();
	}

	public static int countItemInAllSlots(String itemName) {
		int size = getInventorySize();
		int n = 0;
		for (int i = 0; i < size; i++) {
			ItemStack slot = getStack(i);
			if (!slot.isEmpty() && slotContainsItem(slot, itemName)) {
				n += slot.getCount();
			}
		}
		return n;
	}

	public static boolean slotContainsItem(ItemStack stack, String itemName) {
		if (!stack.isEmpty()) {
			return stack.getItem().toString().equals(itemName);
		}
		return false;
	}

	public static int countItemInPlayerInv(String itemName) {

		int start = getStartOfPlayerInv();
		if (start == -1)
			return -1;

		int size = getInventorySize();
		int n = 0;
		for (int i = start; i < size; i++) {
			ItemStack stack = getStack(i);
			if (!stack.isEmpty() && slotContainsItem(stack, itemName)) {
				n += stack.getCount();
			}
		}
		return n;
	}

	public static int countEmptySlot() {
		int start = getStartOfPlayerInv();
		if (start == -1)
			return -1;

		int size = start + 36;
		int n = 0;
		for (int i = start; i < size; i++) {
			ItemStack slot = getStack(i);
			if (slot.isEmpty()) {
				n += 1;
			}
		}
		return n;
	}

	public static void PICKUP_LEFT(int slot) {
		mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP,
				mc.player);
	}

	public static void PICKUP_RIGHT(int slot) {
		mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 1, SlotActionType.PICKUP,
				mc.player);
	}

	public static void MIDDLE(int slot) {
		mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 2, SlotActionType.CLONE,
				mc.player);
	}

	public static void THROW(int slot) {
		mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 1, SlotActionType.THROW,
				mc.player);
	}

	public static void SHIFTCLICK(int slot) {
		mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, 0, SlotActionType.QUICK_MOVE,
				mc.player);
	}

}

package com.djczq.minecraft.wurst.hacks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.djczq.minecraft.wurst.utils.ParameterUtils;
import com.djczq.minecraft.wurst.utils.SellableItem;
import com.djczq.minecraft.wurst.utils.InventoryUtils;
import com.google.gson.JsonObject;

import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.UpdateListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.util.json.JsonException;
import net.wurstclient.util.json.JsonUtils;

@SearchTags({"DjMod", "dj"})
public final class SellOnGuiHack extends Hack implements UpdateListener {
	private final CheckboxSetting debug = new CheckboxSetting("Debug", "Debug mode", false);

	private final SliderSetting speed = new SliderSetting("Speed", "Speed at which the actions are performed.", 1, 0.1,
			10, 0.1, ValueDisplay.DECIMAL);

	private int timer;
	private String cmd;
	private List<SellableItem> sellableItems;
	private SellableItem currentSell;

	private int phase;

	public SellOnGuiHack() {
		super("SellOnGui", "Sell items from inventory using the provided cmd and map of the shop.");
		setCategory(Category.OTHER);
		addSetting(debug);
		addSetting(speed);
	}

	@Override
	public void onEnable() {
		EVENTS.add(UpdateListener.class, this);
		try {
			JsonObject jsonData = JsonUtils
					.parseFileToObject(Path.of(ParameterUtils.CONFIGFOLDER + "/SellableItems.json")).toJsonObject();
			cmd = jsonData.get("command").getAsString();
			sellableItems = ParameterUtils.getSellableItems(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonException e) {
			e.printStackTrace();
		}
		timer = 0;
		currentSell = null;
	}

	@Override
	public void onDisable() {
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public String getRenderName()
	{
		return getName() + " [" + timer + "]";
	}
	
	@Override
	public void onUpdate() {
		// update timer
		timer += 1;

		// check timer
		if (timer < 100 / speed.getValue())
			return;
		timer = 0;
		if (currentSell == null) {
			phase = -1;
			for (SellableItem i : sellableItems) {
				if (InventoryUtils.countItemInPlayerInv(i.getItem()) >= i.getMin()) {
					currentSell = i;
					phase = 0;
					break;
				}
			}

			if (currentSell == null) {
				setEnabled(false);
			}
		} else {
			if (InventoryUtils.countItemInPlayerInv(currentSell.getItem()) >= currentSell.getMin()) {

				if (phase > 0) {
					// currentSell.getClicks().get(phase - 1)
					switch (currentSell.getClicks().get(phase - 1).first) {
					case "middle":
						InventoryUtils.MIDDLE(currentSell.getClicks().get(phase - 1).second);
						break;
					case "right":
						InventoryUtils.PICKUP_RIGHT(currentSell.getClicks().get(phase - 1).second);
						break;
					case "left":
						InventoryUtils.PICKUP_LEFT(currentSell.getClicks().get(phase - 1).second);
						break;
					case "shiftclick":
						InventoryUtils.SHIFTCLICK(currentSell.getClicks().get(phase - 1).second);
						break;
					default:
						break;
					}
					if (phase < currentSell.getClicks().size())
						phase++;
				}

				if (phase == 0) {
					MC.player.sendChatMessage(cmd);
					phase = 1;
				}

			} else {
				MC.player.getInventory().onClose(MC.player);
				phase = -1;
				currentSell = null;
			}
		}
	}

}

package com.djczq.minecraft.wurst.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.icu.impl.Pair;

import net.minecraft.client.MinecraftClient;
import net.wurstclient.WurstClient;

public class ParameterUtils {
	private static final MinecraftClient MC = WurstClient.MC;
	public static final String CONFIGFOLDER = MC.runDirectory.toPath().toString() + "/wurst/djczq/";

	public static String parseOneLine(String file) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(CONFIGFOLDER + file));
			String res = br.readLine();
			br.close();
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static LinkedList<Integer> parseListOfInt(String file) {
		LinkedList<Integer> list = new LinkedList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(CONFIGFOLDER + file));

			String line;
			while ((line = br.readLine()) != null) {
				line.trim();
				list.add(Integer.parseInt(line));

			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void writeListOfInt(String file, LinkedList<Integer> list) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(CONFIGFOLDER + file));
			for (Integer i : list) {
				bw.write(i.toString() + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<SellableItem> getSellableItems(JsonObject jsonObject) {
		List<SellableItem> list = new LinkedList<>();
		JsonArray jsonArray = jsonObject.getAsJsonArray("itemlist");
		for (JsonElement el : jsonArray) {
			JsonObject item = el.getAsJsonObject();
			Integer min = 1;
			if (item.has("min")) {
				min = item.get("min").getAsInt();
			}
			String name = item.get("item").getAsString();
			List<Pair<String, Integer>> pairs = new LinkedList<Pair<String, Integer>>();
			for (JsonElement pairEl : item.get("steps").getAsJsonArray()) {
				JsonObject pairObj = pairEl.getAsJsonObject();
				pairs.add(Pair.of(pairObj.get("cliktype").getAsString(),pairObj.get("position").getAsInt()));
			}
			list.add(new SellableItem(name, min, pairs));
		}
		return list;
	}
}

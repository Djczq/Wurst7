package com.djczq.minecraft.wurst.utils;

import java.util.List;

import com.ibm.icu.impl.Pair;

public class SellableItem {

	String item;
	int min;
	List<Pair<String, Integer>> clicks;
	
	public String getItem() {
		return item;
	}

	public int getMin() {
		return min;
	}

	public List<Pair<String, Integer>> getClicks() {
		return clicks;
		
	}

	public SellableItem(String item, int min, List<Pair<String, Integer>> clicks) {
		super();
		this.item = item;
		this.min = min;
		this.clicks = clicks;
	}

	@Override
	public String toString() {
		String str = "";
		if(clicks != null) {
			for (Pair<String, Integer> i : clicks) {
				str += "=>" + i.toString();
			}
		}

		return "SellableStuff [item=" + item + ", min=" + min + ", path " + str + "]";
	}

}

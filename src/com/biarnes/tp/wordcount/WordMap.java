package com.biarnes.tp.wordcount;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings("serial")
public class WordMap extends ConcurrentHashMap<String, Integer> {
	
	public void addWord(String word) {
		if (this.containsKey(word)) {
			this.replace(word, this.get(word) + 1);
		} else {
			this.put(word, 1);
		}
	}

	public TreeMap<Integer, TreeSet<String>> orderedEntries() {
		Comparator<Integer> comparator = Comparator.reverseOrder();
		TreeMap<Integer, TreeSet<String>> sortedMap = new TreeMap<Integer, TreeSet<String>>(comparator);
		
		for (Map.Entry<String, Integer> entry : this.entrySet()) {
			if (sortedMap.containsKey(entry.getValue())) {
				sortedMap.get(entry.getValue()).add(entry.getKey());
			} else {
				TreeSet<String> set = new TreeSet<String>();
				set.add(entry.getKey());
				sortedMap.put(entry.getValue(), set);
			}
		}
		return sortedMap;	
	}
}

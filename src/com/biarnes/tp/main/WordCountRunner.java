package com.biarnes.tp.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.biarnes.tp.wordcount.WordCounter;
import com.biarnes.tp.wordcount.WordMap;

public class WordCountRunner {
	private static final int _nbCores = 4;
	private WordMap _map;
	private WordCounter[] _counters;
	private BlockingQueue<String> _queue;
	
	public WordCountRunner() {
		_map = new WordMap();
		_counters = new WordCounter[_nbCores];
		_queue = new LinkedBlockingQueue<String>();
	}
	
	public void startCounters() throws IOException, InterruptedException {
		for (int i = 0; i < _nbCores; i++) {
			_counters[i] = new WordCounter(this, _queue, _map);
			_counters[i].start();
		}
	}
	
	public void readFile(String filePath) throws IOException, InterruptedException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		int nbLines = 1;
		try {
		    String line = br.readLine();
		    while (line != null) {
		    	_queue.put(line);		    	
		    	line = br.readLine();
		    	nbLines++;
		    }
		} finally {
			System.out.println("nb lines = " + nbLines);
		    br.close();
		}
		
		_counters[0].notifyFileRed();
	}
	
	public void terminateWordCount() throws InterruptedException {
		for (WordCounter counter : _counters) {
			counter.interrupt();
		}
	}
	
	public void waitForCompletion() throws InterruptedException {
		for (WordCounter counter : _counters) {
			counter.join();
		}
	}
	
	public void printResults() {
		TreeMap<Integer, TreeSet<String>> entries = _map.orderedEntries();
		for (Integer count : entries.keySet()) {
			for (String word : entries.get(count)) {
//				System.out.printf("%s %d%n", word, count);
			}
		}
		
		System.out.println("map size = " + _map.size());
		System.out.println("sorted map size = " + entries.size());
	}
}

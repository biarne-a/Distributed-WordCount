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
	private static final int _nbCores = 8;
	private WordMap _map = new WordMap();
	private WordCounter[] _counters;
	private BlockingQueue<String> _queue;
	
	public WordCountRunner() {
		_map = new WordMap();
		_counters = new WordCounter[_nbCores];
		_queue = new LinkedBlockingQueue<String>();
	}
	
	public void startCounters() throws IOException, InterruptedException {
		for (int i = 0; i < _nbCores; i++) {
			_counters[i] = new WordCounter(_queue, _map);
			_counters[i].start();
		}
	}
	
	public void waitForJobCompletion() throws InterruptedException {
		for (WordCounter counter : _counters) {
			counter.join();
		}
	}
	
	public void readFile(String filePath) throws IOException, InterruptedException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		try {
		    String line = br.readLine();
		    while (line != null) {
		    	_queue.put(line);		    	
		    	line = br.readLine();
		    }
		} finally {
		    br.close();
		}
	}
	
	public void printResults() {
		TreeMap<Integer, TreeSet<String>> entries = _map.orderedEntries();
		System.out.println("map size = " + entries.size());
		for (Integer count : entries.keySet()) {
			for (String word : entries.get(count)) {
				System.out.printf("%s %d%n", word, count);
			}
		}
	}
}

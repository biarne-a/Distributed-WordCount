package com.biarnes.tp.wordcount;

import java.util.concurrent.BlockingQueue;


public class WordCounter extends Thread {
	
	private BlockingQueue<String> _queue;
	private WordMap _wordMap;
	
	public WordCounter(BlockingQueue<String> queue, WordMap wordMap) {
		_queue = queue;
		_wordMap = wordMap;
	}

	public void run() {
		String line;
		try {
			line = _queue.take();
			processLine(line);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while ((line = _queue.poll()) != null) {
			processLine(line);
		}
	}
	
	private void processLine(String line) {
		for (String word : line.split(" ")) {
			if (word.isEmpty()) {
				continue;
			}
			_wordMap.addWord(word);
		}
	}
}

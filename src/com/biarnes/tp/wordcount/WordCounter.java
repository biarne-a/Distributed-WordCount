package com.biarnes.tp.wordcount;

import java.util.concurrent.BlockingQueue;

import com.biarnes.tp.main.WordCountRunner;


public class WordCounter extends Thread {
	private BlockingQueue<String> _queue;
	private WordMap _wordMap;
	private WordCountRunner _runner;
	private volatile boolean _fileFullyRed = false;
	
	public WordCounter(WordCountRunner runner, BlockingQueue<String> queue, WordMap wordMap) {
		_runner = runner;
		_queue = queue;
		_wordMap = wordMap;
	}

	public void run() {
		try {
			while (true) {
				processLine(_queue.take());
				
				if (_fileFullyRed && _queue.isEmpty()) {
					synchronized (this) {
						_runner.terminateWordCount();
					}
					return;
				}
			} 
		} catch (InterruptedException e) {
		}
	}
	
	public void notifyFileRed() {
		_fileFullyRed = true;
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

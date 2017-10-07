package com.biarnes.tp.main;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length != 1) {
			System.err.println("Fatal error: No input file");
		}
		long startTime = System.currentTimeMillis();

		WordCountRunner runner = new WordCountRunner();
		runner.startCounters();
		
		long timeAfterCountersStarted = System.currentTimeMillis();

		runner.readFile(args[0]);

		long timeAfterFileRed = System.currentTimeMillis();

		runner.waitForCompletion();
		
		long timeAfterWordsCount = System.currentTimeMillis();

		runner.printResults();
		long endTime = System.currentTimeMillis();
		
		System.out.printf("Starting counters time: %d milliseconds%n", timeAfterCountersStarted - startTime);
		System.out.printf("Read file time: %d milliseconds%n", timeAfterFileRed - timeAfterCountersStarted);
		System.out.printf("Word count time: %d milliseconds%n", timeAfterWordsCount - timeAfterFileRed);
		System.out.printf("Sort and print time: %d milliseconds%n", endTime - timeAfterWordsCount);
		System.out.printf("Total time: %d milliseconds%n", endTime - startTime);
	}
}

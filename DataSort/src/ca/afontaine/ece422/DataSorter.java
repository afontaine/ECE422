/*
 * Copyright (c) 2014 Andrew Fontaine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.afontaine.ece422;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-10-12
 */
public class DataSorter {

	private static ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
	private static Timer TIMER = new Timer();
	private static String USAGE = "<input file> <output file> <error probability for heap> <error probability for insertion> <time limit>";
	private static Random RANDOM = new Random();

	private double errInsertion;
	private int time;
	private double errHeap;

	public DataSorter(int time, double errHeap, double errInsertion) {
		this.time = time;
		this.errHeap = errHeap;
		this.errInsertion = errInsertion;
	}

	private boolean executeHeap(int[] arr) {
		Future<Integer> heapSort = EXECUTOR.submit(() -> HeapSorter.sort(arr));
		TIMER.schedule(new WatchDog(heapSort), time);
		try {
			int memory = heapSort.get();
			double hazard = RANDOM.nextDouble();
			if(hazard > 0.5 && hazard < 0.5 + errHeap * memory) {
				System.err.println("Heap sort failed due to memory errors.");
				return false;
			}
			return new SortAdjudicator().adjudicate(arr);
		}
		catch(CancellationException | InterruptedException e) {
			System.err.println("Heap Sort was cancelled by Watch Dog.");
			return false;
		}
		catch(ExecutionException e) {
			return false;
		}
	}

	private boolean executeInsert(int[] arr) {
		Future<Integer> insertionSort = EXECUTOR.submit(() -> NativeInsertionSorter.sort(arr));
		TIMER.schedule(new WatchDog(insertionSort), time);

		try {
			int memory = insertionSort.get();
			double hazard = RANDOM.nextDouble();
			if(hazard > 0.5 && hazard < 0.5 + errInsertion * memory) {
				System.err.println("Insertion sort failed due to memory errors.");
				return false;
			}
			return new SortAdjudicator().adjudicate(arr);
		}
		catch(CancellationException | InterruptedException e) {
			System.err.println("Insertion Sort was cancelled by Watch Dog.");
			return false;
		}
		catch(ExecutionException e) {
			e.printStackTrace();
		}

		return false;
	}

	public int[] execute(int[] arr) throws SortFailureException {
		int[] arrHeap = Arrays.copyOf(arr, arr.length);
		int[] arrInsert = Arrays.copyOf(arr, arr.length);
		if(executeHeap(arrHeap)) return arrHeap;
		else if(executeInsert(arrInsert)) return arrInsert;
		throw new SortFailureException("Both sorting algorithms have failed.");
	}


	public static void main(String[] args) {
		if(args.length != 5) {
			System.err.println(USAGE);
			System.exit(1);
		}
		String fileIn = args[0];
		String fileOut = args[1];
		int time = 0;
		double errHeap = 0.0;
		double errInsertion = 0.0;
		String line = "";
		int[] arr = new int[] {};
		try {
			errHeap = Double.parseDouble(args[2]);
		}
		catch(NumberFormatException e) {
			System.err.println("Argument: " + args[2] + " must be a decimal value.");
			System.exit(1);
		}
		try {
			errInsertion = Double.parseDouble(args[3]);
		}
		catch(NumberFormatException e) {
			System.err.println("Argument: " + args[3] + " must be a decimal value.");
			System.exit(1);
		}
		try {
			time = Integer.parseInt(args[4]);
			if(time < 1) {
				System.err.println("Argument: " + args[4] + " must be greater than 0.");
				System.exit(1);
			}
		}
		catch(NumberFormatException e) {
			System.err.println("Argument: " + args[4] + " must be an integer value.");
			System.exit(1);
		}
		try {
			File f = new File(args[0]);
			f.getCanonicalPath();
		}
		catch(IOException e) {
			System.err.println("Argument " + args[0] + " must be a valid filename");
		}
		try {
			line = FileIO.read(fileIn);
		}
		catch(IOException e) {
			System.err.println("Something went wrong reading the file.\n"
					+ "Does it exist?\n"
					+ "Do you have permission to read the file?");
			System.exit(2);
		}
		try {
			arr = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
		}
		catch(NumberFormatException e) {
			System.err.println(args[0] + " is not a file containing comma-separated integer values.");
		}
		try {
			FileIO.write(args[1], new DataSorter(time, errHeap, errInsertion).execute(arr));
		}
		catch(SortFailureException e) {
			System.err.println(e.getMessage());
			System.exit(3);
		}
		catch(IOException e) {
			System.err.println("Cannot write to file. Check your privilege.");
			new File(args[1]).delete();
			System.exit(1);
		}
		System.exit(0);
	}

	private class SortAdjudicator {
		public boolean adjudicate(int[] arr) {
			boolean result = true;
			for(int i = 0; i < arr.length - 1; i++) {
				result &= arr[i] <= arr[i+1];
			}
			return result;
		}

	}

	private class SortFailureException extends RuntimeException {
		public SortFailureException() { super(); }
		public SortFailureException(String message) { super(message); }
		public SortFailureException(String message, Throwable cause) { super(message, cause); }
		public SortFailureException(Throwable cause) { super(cause); }
	}
}

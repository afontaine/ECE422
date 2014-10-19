/*
 * The MIT License (MIT)
 *
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-10-08
 */
public class DataGenerator {

	private static Random RAND = new Random();
	private static int MAX = 500;
	private static String USAGE = "java DataGenerator <file name> <number of data points>";

	public static int[] createValues(int num) {
		List<Integer> numbers = new ArrayList<>();
		for(int i = 0; i < num; i++) {
			numbers.add(RAND.nextInt(MAX));
		}
		return numbers.stream().mapToInt(Integer::intValue).toArray();
	}

	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println(USAGE);
			System.exit(1);
		}
		int num = 0;
		try {
			num = Integer.parseInt(args[1]);
		}
		catch(NumberFormatException e) {
			System.err.println("Argument " + args[1] + " must be an integer.");
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
			FileIO.write(args[0], createValues(num));
		}
		catch(IOException e) {
			System.err.println("Cannot write to file. Check your privilege, cis-het shitlord.");
			System.exit(1);
		}
	}
}

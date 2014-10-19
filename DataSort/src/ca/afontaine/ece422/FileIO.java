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

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Andrew
 * @version 1.0
 * @since 2014-10-12
 */
public class FileIO {

	public static void write(String file, String nums) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(nums);
		writer.close();

	}

	public static void write(String file, int[] arr) throws IOException {
		write(file, Arrays.stream(arr).mapToObj(Integer::toString).collect(Collectors.joining(",")));
	}

	public static String read(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		reader.close();
		if(line.charAt(line.length() - 1) == '\n') {
			throw new IOException("File is in wrong format");
		}
		return line;
	}
}

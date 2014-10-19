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

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-10-09
 */
public class HeapSorter {


	private static int siftDown(int[] heap, int start, int end) {
		int mem = 0;
		int child;
		int swap;
		int temp;
		int root = start;
		mem += 4;

		while(root * 2 + 1 <= end) {
			mem += 2;
			child = root * 2 + 1;
			swap = root;
			mem += 4;
			if(heap[swap] < heap[child]) {
				swap = child;
				mem += 2;
			}
			mem += 2;
			if((child + 1 <= end) && (heap[swap] < heap[child + 1])) {
				swap = child + 1;
				mem += 2;
			}
			mem += 6;

			if(swap != root) {
				temp = heap[swap];
				heap[swap] = heap[root];
				heap[root] = temp;
				root = swap;
				mem += 12;
			}
			else {
				mem += 2;
				return mem;
			}
			mem +=2;
		}
		return mem;
	}

	private static int heapify(int[] heap) {
		int mem = 0;
		for(int start = (heap.length - 2) / 2; start >= 0; start--) {
			mem += 3;
			mem += siftDown(heap, start, heap.length - 1);
		}
		mem += 3;
		return mem;
	}

	public static int sort(int[] heap) {
		int temp;
		int mem = 1;

		mem += heapify(heap);
		for(int end = heap.length - 1; end > 0; ) {
			mem += 1;
			temp = heap[end];
			heap[end] = heap[0];
			heap[0] = temp;
			siftDown(heap, 0, --end);
			mem += 11;
		}
		mem += 3;
		return mem;
	}
}

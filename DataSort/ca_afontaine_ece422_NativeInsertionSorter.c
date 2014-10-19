#include <stdio.h>
#include <jni.h>
#include "include/ca_afontaine_ece422_NativeInsertionSorter.h"

JNIEXPORT jint JNICALL Java_ca_afontaine_ece422_NativeInsertionSorter_sort
	(JNIEnv *env, jclass klass, jintArray arr) {
	int x;
	int j;
	jboolean is_copy;
	jsize len = (*env)->GetArrayLength(env, arr);
	jint *array = (*env)->GetIntArrayElements(env, arr, &is_copy);
	jint mem = 6;
	for(int i = 1; i < len; i++) {
		x = array[i];
		j = i;
		mem += 5;
		while((j > 0) && (array[j - 1] > x)) {
			array[j] = array[--j];
			mem += 8;
		}
		mem += 4;
		array[j] = x;
		mem += 3;
	}
	mem += 2;
	(*env)->ReleaseIntArrayElements(env, arr, array, 0);
	return mem;
}

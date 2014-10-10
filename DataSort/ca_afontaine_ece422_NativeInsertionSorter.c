#include <stdio.h>
#include <jni.h>
#include "include/ca_afontaine_ece422_NativeInsertionSorter.h"

JNIEXPORT void JNICALL Java_ca_afontaine_ece422_NativeInsertionSorter_sort
	(JNIEnv *env, jclass klass, jintArray arr) {
	int x;
	int j;
	jboolean is_copy;
	jsize len = (*env)->GetArrayLength(env, arr);
	jint *array = (*env)->GetIntArrayElements(env, arr, &is_copy);
	for(int i = 1; i < len; i++) {
		x = array[i];
		j = i;
		while((j > 0) && (array[j - 1] > x)) {
			array[j] = array[--j];
		}
		array[j] = x;
	}
	(*env)->ReleaseIntArrayElements(env, arr, array, 0);
}

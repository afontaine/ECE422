#include <jni.h>
#include "include/ca_afontaine_ece422_NativeInsertionSorter.h"

JNIEXPORT void JNICALL Java_ca_afontaine_ece422_NativeInsertionSorter_sort
	(JNIEnv *env, jclass klass, jintArray arr) {
	jboolean is_copy;
	jsize len = (*env)->GetArrayLength(env, arr);
	jint *array = (*env)->GetIntArrayElements(env, arr, &is_copy);
	(*env)->ReleaseIntArrayElements(env, arr, array, 0);
}

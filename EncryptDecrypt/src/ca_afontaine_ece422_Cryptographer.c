#include "include/ca_afontaine_ece422_Cryptographer.h"

void decrypt (long *v, long *k){
/* TEA decryption routine */
unsigned long n=32, sum, y=v[0], z=v[1];
unsigned long delta=0x9e3779b9l;

	sum = delta<<5;
	while (n-- > 0){
		z -= (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
		y -= (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		sum -= delta;
	}
	v[0] = y;
	v[1] = z;
}

void encrypt (long *v, long *k){
/* TEA encryption algorithm */
unsigned long y = v[0], z=v[1], sum = 0;
unsigned long delta = 0x9e3779b9, n=32;

	while (n-- > 0){
		sum += delta;
		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
	}

	v[0] = y;
	v[1] = z;
}


JNIEXPORT void JNICALL Java_ca_afontaine_ece422_Cryptographer_encryptMessage
  (JNIEnv *env, jclass klass, jbyteArray value, jlongArray key) {
	jboolean is_copy_value;
	jboolean is_copy_key;
	jsize len = (*env)->GetArrayLength(env, value);
	jbyte *arr = (*env)->GetByteArrayElements(env, value, &is_copy_value);
	jlong *k = (*env)->GetLongArrayElements(env, key, &is_copy_key);
	long *i = (long *)arr;
	while((jbyte *) i < arr + len) {
		encrypt(i, (long *) k);
		i += 2;
	}
	(*env)->ReleaseByteArrayElements(env, value, arr, is_copy_value);
	(*env)->ReleaseLongArrayElements(env, key, k, is_copy_key);
	return;
}

JNIEXPORT void JNICALL Java_ca_afontaine_ece422_Cryptographer_decryptMessage
  (JNIEnv *env, jclass klass, jbyteArray value, jlongArray key) {
	jboolean is_copy_value;
	jboolean is_copy_key;
	jsize len = (*env)->GetArrayLength(env, value);
	jbyte *arr = (*env)->GetByteArrayElements(env, value, &is_copy_value);
	jlong *k = (*env)->GetLongArrayElements(env, key, &is_copy_key);
	long *i = (long *) arr;
	while((jbyte *) i < arr + len) {
		encrypt(i, (long *)k);
		i += 2;
	}
	(*env)->ReleaseByteArrayElements(env, value, arr, is_copy_value);
	(*env)->ReleaseLongArrayElements(env, key, k, is_copy_key);
	return;
}

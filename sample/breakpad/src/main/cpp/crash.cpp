#include <jni.h>
#include <string>

void crash();

extern "C"
JNIEXPORT void JNICALL
Java_com_cyaan_demo_breakpad_CrashLib_crashDump(JNIEnv *env, jobject thiz) {
    crash();
}

void crash() {
    volatile int *a = (int *) NULL;
    *a = 1;
}


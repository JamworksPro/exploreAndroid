#include <jni.h>
#include <string>
#include <pthread.h>
#include <inttypes.h>

// processing callback to handler class
typedef struct event_context
{
  jclass      classEventHandler;
  jmethodID   methodOnEvent;
}EventContext;
EventContext g_ctxEvent;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
{
  JNIEnv *env;
  memset(&g_ctxEvent, 0, sizeof(g_ctxEvent));

  if(vm->GetEnv((void**) &env, JNI_VERSION_1_6)!=JNI_OK)
  {
    return JNI_ERR; // JNI version not supported.
  }

  //Get a Handle to the Apps EventHandler.java class file
  jobject classEventHandler = env->FindClass("com/jamworkspro/explorejnicallbacks/EventHandler");
  g_ctxEvent.classEventHandler = (jclass)env->NewGlobalRef(classEventHandler);
  g_ctxEvent.methodOnEvent = env->GetStaticMethodID(g_ctxEvent.classEventHandler, "OnEvent", "(I)V");

  return JNI_VERSION_1_6;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_jamworkspro_explorejnicallbacks_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */)
{
  std::string hello="Hello from C++";
  return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_jamworkspro_explorejnicallbacks_MainActivity_JNIGenerateEvent(JNIEnv *env, jobject instance, jint iEventId)
{
  env->CallStaticVoidMethod(g_ctxEvent.classEventHandler, g_ctxEvent.methodOnEvent, iEventId);
}

/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class metaface_nlp_MetaFaceFestival */

#ifndef _Included_metaface_nlp_MetaFaceFestival
#define _Included_metaface_nlp_MetaFaceFestival
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     metaface_nlp_MetaFaceFestival
 * Method:    initialise
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_metaface_nlp_MetaFaceFestival_initialise
  (JNIEnv *, jobject);

/*
 * Class:     metaface_nlp_MetaFaceFestival
 * Method:    openSMLFile
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_metaface_nlp_MetaFaceFestival_openSMLFile
  (JNIEnv *, jobject, jstring);

/*
 * Class:     metaface_nlp_MetaFaceFestival
 * Method:    closeSMLFile
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_metaface_nlp_MetaFaceFestival_closeSMLFile
  (JNIEnv *, jobject);

/*
 * Class:     metaface_nlp_MetaFaceFestival
 * Method:    getNextPhonemeInfo
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_metaface_nlp_MetaFaceFestival_getNextPhonemeInfo
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif

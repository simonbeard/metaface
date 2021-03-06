/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class metaface_dsp_MetaFaceMbrola */

#ifndef _Included_metaface_dsp_MetaFaceMbrola
#define _Included_metaface_dsp_MetaFaceMbrola
#ifdef __cplusplus
extern "C" {
#endif
#undef metaface_dsp_MetaFaceMbrola_MALE
#define metaface_dsp_MetaFaceMbrola_MALE 0L
#undef metaface_dsp_MetaFaceMbrola_FEMALE
#define metaface_dsp_MetaFaceMbrola_FEMALE 1L
/* Inaccessible static: phonemes */
/* Inaccessible static: visemes */
/* Inaccessible static: MAX_PHONEMES */
/*
 * Class:     metaface_dsp_MetaFaceMbrola
 * Method:    synthesise
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_metaface_dsp_MetaFaceMbrola_synthesise
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     metaface_dsp_MetaFaceMbrola
 * Method:    initialise
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;FJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_metaface_dsp_MetaFaceMbrola_initialise
  (JNIEnv *, jobject, jstring, jstring, jstring, jfloat, jlong);

#ifdef __cplusplus
}
#endif
#endif

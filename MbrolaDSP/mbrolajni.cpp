#include <stdio.h>
#include <jni.h>
#include <string.h>
#include "metaface_dsp_MetaFaceMbrola.h"

extern char *initialise (char *,char *,char *,float,long);
extern char *synthesise (char *,char *);
extern char *finalise ();

JNIEXPORT jstring JNICALL Java_metaface_dsp_MetaFaceMbrola_initialise (JNIEnv *env, jobject temp, jstring database, jstring rename, jstring clone, jfloat pitchratio, jlong freq)
{
	const char *database_c;
	const char *rename_c;
	const char *clone_c;

	char *returnval;	

	jboolean iscopy_d;
	jboolean iscopy_r;
	jboolean iscopy_c;	

	database_c = env->GetStringUTFChars (database,&iscopy_d);
	rename_c = env->GetStringUTFChars (rename,&iscopy_r);
	clone_c = env->GetStringUTFChars (clone,&iscopy_c);

	returnval = NULL;

	returnval = initialise ((char *)database_c,(char *)rename_c,(char *)clone_c,(float)pitchratio,(long)freq);
	if (returnval == NULL)
		returnval = " ";

	if (iscopy_d == JNI_TRUE)
		env->ReleaseStringUTFChars (database,database_c);
	if (iscopy_r == JNI_TRUE)
		env->ReleaseStringUTFChars (rename,rename_c);
	if (iscopy_c == JNI_TRUE)
		env->ReleaseStringUTFChars (clone,clone_c);

	return (env->NewStringUTF(returnval));
}

JNIEXPORT jstring JNICALL Java_metaface_dsp_MetaFaceMbrola_synthesise (JNIEnv *env, jobject temp, jstring input, jstring output)
{
	const char *input_c;
	const char *output_c;

	char *returnval;	

	jboolean iscopy_i;
	jboolean iscopy_o;

	input_c = env->GetStringUTFChars (input,&iscopy_i);
	output_c = env->GetStringUTFChars (output,&iscopy_o);

	returnval = NULL;

	returnval = synthesise ((char *)input_c,(char *)output_c);

	if (iscopy_i == JNI_TRUE)
		env->ReleaseStringUTFChars (input,input_c);
	if (iscopy_o == JNI_TRUE)
		env->ReleaseStringUTFChars (output,output_c);

	if (returnval == NULL)
		return (output);
	return (env->NewStringUTF(returnval));
}

JNIEXPORT jstring JNICALL Java_metaface_dsp_MetaFaceMbrola_finalisembrola (JNIEnv *env, jobject temp)
{
	char *returnval;	
	returnval = finalise ();
	returnval = NULL;
	if (returnval == NULL)
		returnval = " ";
	return (env->NewStringUTF(returnval));
}
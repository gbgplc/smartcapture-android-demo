package com.gbg.smartcapture.bigmagic.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Json conversions with Moshi
 */
class JsonConverter {

    companion object {

        fun credentialToJson(credential: SampleAppCredential): String =
            getCredentialAdapter().toJson(credential)

        fun credentialFromJson(json: String): SampleAppCredential? =
            if (json.isNotBlank()) {
                getCredentialAdapter().fromJson(json)
            } else {
                null
            }

        private fun getCredentialAdapter(): JsonAdapter<SampleAppCredential> {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            return moshi.adapter(SampleAppCredential::class.java)
        }

    }

}
package com.gbg.smartcapture.bigmagic.data

data class SampleAppCredential(
    val baseUrl: String = "",
    val username: String = "",
    val password: String = ""
) {
    fun hiddenPassword(): String =
        password.map { '*' }.joinToString("")

    fun toJson(): String =
        JsonConverter.credentialToJson(this)

    override fun equals(other: Any?): Boolean {
        return if (other is SampleAppCredential) {
            other.baseUrl == baseUrl &&
                    other.username == username
        } else {
            false
        }
    }

    companion object {
        fun fromJson(json: String): SampleAppCredential? =
            JsonConverter.credentialFromJson(json)
    }

    override fun hashCode(): Int {
        var result = baseUrl.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }

}
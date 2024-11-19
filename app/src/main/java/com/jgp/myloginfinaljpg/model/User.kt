package com.jgp.myloginfinaljpg.model

data class User (
    val id: String?,
    val userId: String?,
    val displayName: String?,
    val avatarUrl: String?,
    val quote: String?,
    val profession: String?
) {
    fun toMap(): MutableMap<String ,String?>{
        return mutableMapOf(
            "userId" to this.userId,
            "displayName" to this.displayName,
            "profession" to this.profession,
            "quote" to this.quote,
            "avatarUrl" to this.avatarUrl
        )
    }
}
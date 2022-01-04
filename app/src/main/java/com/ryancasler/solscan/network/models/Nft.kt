package com.ryancasler.solscan.network.models;

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.lang.Exception
import java.lang.reflect.Type


data class Nft(
    val lamports: Long,
    val ownerProgram: String,
    val type: String,
    val rentEpoch: Long,
    val account: String,
    val tokenInfo: TokenInfo,
    val metadata: Metadata?
)

data class Metadata(
    val key: Long,
    val updateAuthority: String,
    val mint: String,
    val data: Data,
    val primarySaleHappened: Long,
    val isMutable: Long,
    val type: String
)

data class Data(
    val name: String,
    val symbol: String,
    val image: String,
    val properties: Properties,
    val description: String,
    @SerializedName("seller_fee_basis_points")
    val sellerFeeBasisPoints: Long,
    val attributes: List<Attribute>,
    val collection: Collection,
    val uri: String
)

data class Attribute(
    @SerializedName("trait_type")
    val traitType: String,
    // This is an int or double or string so need a custom serializer. For now will just jam
    // into a string for view only
    val value: String
)

class AttributeDeserializer : JsonDeserializer<Attribute?> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        jsonElement: JsonElement,
        typeOF: Type,
        context: JsonDeserializationContext?
    ): Attribute? {
        if (!jsonElement.isJsonObject) return null

        val jsonObject = jsonElement.asJsonObject
        val traitType = jsonObject.get("trait_type").asString
        val valueElement = jsonObject.get("value")

        try {
            return Attribute(traitType, valueElement.asString)
        } catch (exception: Exception) {
        }

        try {
            return Attribute(traitType, "${valueElement.asInt}")
        } catch (exception: Exception) {
        }

        try {
            return Attribute(traitType, "${valueElement.asDouble}")
        } catch (exception: Exception) {
        }

        return null
    }
}

data class Collection(
    val name: String,
    val family: String
)

data class Properties(
    val files: List<File>,
    val category: String,
    val creators: List<Creator>
)

data class Creator(
    val address: String,
    val share: Long
)

data class File(
    val uri: String,
    val type: String
)

data class TokenInfo(
    val name: String,
    val symbol: String,
    val decimals: Long,
    val tokenAuthority: String,
    val supply: String,
    val type: String
)

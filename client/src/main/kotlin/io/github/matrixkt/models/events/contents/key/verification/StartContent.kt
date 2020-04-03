package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.models.events.contents.Content
import io.github.matrixkt.utils.JsonPolymorphicSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(StartContent.Serializer::class)
abstract class StartContent : Content() {
    /**
     * The device ID which is initiating the process.
     */
    @SerialName("from_device")
    abstract val fromDevice: String

    /**
     * An opaque identifier for the verification process.
     * Must be unique with respect to the devices involved.
     * Must be the same as the transaction_id given in the `m.key.verification.request` if this process is originating from a request.
     */
    @SerialName("transaction_id")
    abstract val transactionId: String

    // /**
    //  * The verification method to use.
    //  */
    // abstract val method: String

    /**
     * Optional method to use to verify the other user's key with.
     * Applicable when the [method] chosen only verifies one user's key.
     * This field will never be present if the [method] verifies keys both ways.
     */
    @SerialName("next_method")
    open val nextMethod: String? get() = null

    @SerialName("m.sas.v1")
    @Serializable
    data class SasV1(
        @SerialName("from_device")
        override val fromDevice: String,

        @SerialName("transaction_id")
        override val transactionId: String,

        /**
         * The key agreement protocols the sending device understands. Must include at least curve25519.
         */
        @SerialName("key_agreement_protocols")
        val keyAgreementProtocols: List<String>,

        /**
         * The hash methods the sending device understands. Must include at least sha256.
         */
        val hashes: List<String>,

        /**
         * The message authentication codes that the sending device understands. Must include at least hkdf-hmac-sha256.
         */
        @SerialName("message_authentication_codes")
        val messageAuthenticationCodes: List<String>,

        /**
         * The SAS methods the sending device (and the sending device's user) understands.
         * Must include at least decimal. Optionally can include emoji. One of: ["decimal", "emoji"]
         */
        @SerialName("short_authentication_string")
        val shortAuthenticationString: List<String>
    ) : StartContent()

    object Serializer : KSerializer<StartContent> by JsonPolymorphicSerializer(
        StartContent::class, "method")
}
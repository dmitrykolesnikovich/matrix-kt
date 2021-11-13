package io.github.matrixkt.api

import io.github.matrixkt.models.UserIdentifier
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Authenticates the user, and issues an access token they can
 * use to authorize themself in subsequent requests.
 *
 * If the client does not supply a ``device_id``, the server must
 * auto-generate one.
 *
 * The returned access token must be associated with the ``device_id``
 * supplied by the client or generated by the server. The server may
 * invalidate any access token previously associated with that device. See
 * `Relationship between access tokens and devices`_.
 */
public class Login(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc<RpcMethod.Post, Login.Url, Login.Body, Login.Response> {
    @Resource("/_matrix/client/r0/login")
    @Serializable
    public class Url

    @Serializable
    public abstract class Body {
        // /**
        //  * The login type being used.
        //  * One of: ["m.login.password", "m.login.token"]
        //  */
        // val type: String

        /**
         * Identification information for the user.
         */
        public abstract val identifier: UserIdentifier?

        /**
         * ID of the client device. If this does not correspond to a
         * known client device, a new device will be created. The server
         * will auto-generate a device_id if this is not specified.
         */
        @SerialName("device_id")
        public abstract val deviceId: String?

        /**
         * A display name to assign to the newly-created device.
         * Ignored if [deviceId] corresponds to a known device.
         */
        @SerialName("initial_device_display_name")
        public abstract val initialDeviceDisplayName: String?

        @SerialName("m.login.password")
        @Serializable
        public data class Password(
            /**
             * Identification information for the user.
             */
            override val identifier: UserIdentifier? = null,

            /**
             * The user's password.
             */
            val password: String,

            @SerialName("device_id")
            override val deviceId: String? = null,

            /**
             * A display name to assign to the newly-created device.
             * Ignored if [deviceId] corresponds to a known device.
             */
            @SerialName("initial_device_display_name")
            override val initialDeviceDisplayName: String? = null
        ) : Body()

        @SerialName("m.login.token")
        @Serializable
        public data class Token(
            /**
             * Identification information for the user.
             */
            override val identifier: UserIdentifier? = null,

            /**
             * Part of [Token-based](https://matrix.org/docs/spec/client_server/r0.5.0#token-based) login.
             */
            val token: String,

            @SerialName("device_id")
            override val deviceId: String? = null,

            /**
             * A display name to assign to the newly-created device.
             * Ignored if [deviceId] corresponds to a known device.
             */
            @SerialName("initial_device_display_name")
            override val initialDeviceDisplayName: String? = null
        ) : Body()
    }

    @Serializable
    public class Response(
        /**
         * An access token for the account.
         * This access token can then be used to authorize other requests.
         */
        @SerialName("access_token")
        public val accessToken: String? = null,
        /**
         * ID of the logged-in device. Will be the same as the
         * corresponding parameter in the request, if one was specified.
         */
        @SerialName("device_id")
        public val deviceId: String? = null,
        /**
         * The server_name of the homeserver on which the account has
         * been registered.
         *
         * **Deprecated**. Clients should extract the server_name from
         * ``user_id`` (by splitting at the first colon) if they require
         * it. Note also that ``homeserver`` is not spelt this way.
         */
        @Deprecated("Clients should extract the server_name from ``user_id``")
        @SerialName("home_server")
        public val homeServer: String? = null,
        /**
         * The fully-qualified Matrix ID that has been registered.
         */
        @SerialName("user_id")
        public val userId: String? = null,
        /**
         * Optional client configuration provided by the server. If present,
         * clients SHOULD use the provided object to reconfigure themselves,
         * optionally validating the URLs within. This object takes the same
         * form as the one returned from .well-known autodiscovery.
         */
        @SerialName("well_known")
        public val wellKnown: Map<String, JsonObject>? = null
    )
}

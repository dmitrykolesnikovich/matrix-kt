package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This API endpoint uses the `User-Interactive Authentication API`_, except in
 * the cases where a guest account is being registered.
 *
 * Register for an account on this homeserver.
 *
 * There are two kinds of user account:
 *
 * - `user` accounts. These accounts may use the full API described in this specification.
 *
 * - `guest` accounts. These accounts may have limited permissions and may not be supported by all
 * servers.
 *
 * If registration is successful, this endpoint will issue an access token
 * the client can use to authorize itself in subsequent requests.
 *
 * If the client does not supply a ``device_id``, the server must
 * auto-generate one.
 *
 * The server SHOULD register an account with a User ID based on the
 * ``username`` provided, if any. Note that the grammar of Matrix User ID
 * localparts is restricted, so the server MUST either map the provided
 * ``username`` onto a ``user_id`` in a logical manner, or reject
 * ``username``\s which do not comply to the grammar, with
 * ``M_INVALID_USERNAME``.
 *
 * Matrix clients MUST NOT assume that localpart of the registered
 * ``user_id`` matches the provided ``username``.
 *
 * The returned access token must be associated with the ``device_id``
 * supplied by the client or generated by the server. The server may
 * invalidate any access token previously associated with that device. See
 * `Relationship between access tokens and devices`_.
 *
 * When registering a guest account, all parameters in the request body
 * with the exception of ``initial_device_display_name`` MUST BE ignored
 * by the server. The server MUST pick a ``device_id`` for the account
 * regardless of input.
 *
 * Any user ID returned by this API must conform to the grammar given in the
 * `Matrix specification <../appendices.html#user-identifiers>`_.
 */
public class Register(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc<RpcMethod.Post, Register.Url, Register.Body, Register.Response> {
    @Resource("_matrix/client/r0/register")
    @Serializable
    public class Url(
        /**
         * The kind of account to register. Defaults to ``user``.
         */
        public val kind: Kind? = null
    )

    @Serializable
    public enum class Kind {
        @SerialName("guest")
        GUEST,
        @SerialName("user")
        USER;
    }

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the
         * user-interactive authentication API. Note that this
         * information is *not* used to define how the registered user
         * should be authenticated, but is instead used to
         * authenticate the ``register`` call itself.
         */
        public val auth: AuthenticationData? = null,
        /**
         * ID of the client device. If this does not correspond to a
         * known client device, a new device will be created. The server
         * will auto-generate a device_id if this is not specified.
         */
        @SerialName("device_id")
        public val deviceId: String? = null,
        /**
         * If true, an ``access_token`` and ``device_id`` should not be
         * returned from this call, therefore preventing an automatic
         * login. Defaults to false.
         */
        @SerialName("inhibit_login")
        public val inhibitLogin: Boolean? = null,
        /**
         * A display name to assign to the newly-created device. Ignored
         * if ``device_id`` corresponds to a known device.
         */
        @SerialName("initial_device_display_name")
        public val initialDeviceDisplayName: String? = null,
        /**
         * The desired password for the account.
         */
        public val password: String? = null,
        /**
         * The basis for the localpart of the desired Matrix ID. If omitted,
         * the homeserver MUST generate a Matrix ID local part.
         */
        public val username: String? = null
    )

    @Serializable
    public class Response(
        /**
         * An access token for the account.
         * This access token can then be used to authorize other requests.
         * Required if the ``inhibit_login`` option is false.
         */
        @SerialName("access_token")
        public val accessToken: String? = null,
        /**
         * ID of the registered device. Will be the same as the
         * corresponding parameter in the request, if one was specified.
         * Required if the ``inhibit_login`` option is false.
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
         * The fully-qualified Matrix user ID (MXID) that has been registered.
         *
         * Any user ID returned by this API must conform to the grammar given in the
         * `Matrix specification <../appendices.html#user-identifiers>`_.
         */
        @SerialName("user_id")
        public val userId: String
    )
}

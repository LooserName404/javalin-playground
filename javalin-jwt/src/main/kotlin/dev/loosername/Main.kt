@file:Suppress("UNCHECKED_CAST")

package dev.loosername

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.javalin.Javalin
import io.javalin.core.security.Role
import io.javalin.core.security.Role.*
import io.javalin.http.Context
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.FromJsonMapper
import io.javalin.plugin.json.JavalinJson
import io.javalin.plugin.json.ToJsonMapper
import javalinjwt.JWTAccessManager
import javalinjwt.JWTGenerator
import javalinjwt.JWTProvider
import javalinjwt.JavalinJWT
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


@Serializable
data class Message(val msg: String = "")

@Serializable
data class User(val name: String = "", val level: String = "")

@Serializable
class SerializableJWTResponse(val jwt: String = "")

@ExperimentalSerializationApi
fun main() {
    val app = Javalin.create{
        it.accessManager(accessManager)
    }.start()

    JavalinJson.fromJsonMapper = object : FromJsonMapper {
        override fun <T> map(json: String, targetClass: Class<T>): T {
            val deserializer = serializer(targetClass) as KSerializer<T>
            return Json.decodeFromString(deserializer, json)
        }
    }

    JavalinJson.toJsonMapper = object : ToJsonMapper {
        override fun map(obj: Any): String {
            val serializer = serializer(obj.javaClass)
            return Json.encodeToString(serializer, obj)
        }
    }

    app.before(decodeHandler)

    app.routes {
        get("/generate", ::generateHandler)
        get("/validate", ::validateHandler)
    }
}

fun generateHandler(ctx: Context) {
    val user = User("Thyago", "Dev")
    val token = provider.generateToken(user)
    ctx.json(SerializableJWTResponse(token))
}

fun validateHandler(ctx: Context) {
    val decodedJWT = JavalinJWT.getDecodedFromContext(ctx)
    ctx.json(Message("Hi ${decodedJWT.getClaim("name").asString()}"))
}

val algorithm = Algorithm.HMAC256("very_secret")
val generator = JWTGenerator<User> { user, alg ->
    val token = JWT.create()
        .withClaim("name", user.name)
        .withClaim("level", user.level)
    return@JWTGenerator token.sign(alg)
}
val verifier = JWT.require(algorithm).build()
val provider = JWTProvider(algorithm, generator, verifier)
val decodeHandler = JavalinJWT.createHeaderDecodeHandler(provider)
val rolesMapping = HashMap<String, Role>()

enum class Roles : Role {
    ANYONE,
    USER,
    ADMIN
}

val accessManager = JWTAccessManager("level", rolesMapping, Roles.ANYONE)

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.*

class TokenHandler {
    private val algorithm = Algorithm.HMAC256("ah8hJH6Gs7aS729")
    private val issuer = "auth0"
    private val validityInMs = 36_000_00 * 10 // 10 hours
    private val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .acceptExpiresAt(5)
        .build()

    fun makeToken(user : User) : String {
        return JWT.create()
               .withIssuer(issuer)
               .withClaim("id", user.id)
               .withExpiresAt(getExpiration())
               .sign(algorithm)
    }

    fun verifyToken(token : String) : Boolean {
        try {
            verifier.verify(token)
        }
        catch (e: JWTVerificationException) {
            return false
        }
        return true
    }

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}
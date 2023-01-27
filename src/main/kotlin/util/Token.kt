package util

import com.auth0.jwt.JWT
import exception.TenantIdNotValidException
import java.util.*

//3. For extraction of tenantId from JWT token we need some util methods

//Decoding claim (jwt field) from token
fun getClaimFromToken(token: String, claim: String): String? {
    return JWT.decode(token.removePrefix("Bearer ")).getClaim(claim).asString()
}

//tenantIds are usually in UUID from, BUT sometimes dashes are removed from UUID string and we need to add them and validate UUID if it is valid
fun getDashedTenantId(token: String): UUID {
    val nonDashedTenantIdString = getClaimFromToken(token, "ten")

    if (!isValidUUID(nonDashedTenantIdString)) {
        throw TenantIdNotValidException("Given Authorization Token tenant is not valid.")
    }

    val dashedTenantIdString = StringBuilder(nonDashedTenantIdString)
        .insert(8, "-")
        .insert(13, "-")
        .insert(18, "-")
        .insert(23, "-")
        .toString()

    return UUID.fromString(dashedTenantIdString)
}

fun isValidUUID(id: String?): Boolean {
    //TODO regex does not work correctly need to fix
    return id != null// && id.matches(Regex.fromLiteral("(\\p{XDigit}{8})-?(\\p{XDigit}{4})-?(\\p{XDigit}{4})-?(\\p{XDigit}{4})-?(\\p{XDigit}{12})"))
}

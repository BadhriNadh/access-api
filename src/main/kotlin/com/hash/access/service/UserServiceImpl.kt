package com.hash.access.service

import com.hash.access.model.User
import com.hash.access.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
@Transactional
class UserServiceImpl (
    @Autowired private val userRepository: UserRepository,
    @Autowired private val jwtEncoder: JwtEncoder,
    @Autowired private val passwordEncoder: PasswordEncoder
) : UserService  {


    override fun saveUser(user: User?): User? {
        val userEncode: User = User(null, user!!.profileName, user.userName, passwordEncoder.encode(user.password))
        return userRepository.save(userEncode)
    }

    override fun generateToken(authentication: Authentication?): String? {
        val now = Instant.now()
        val claims = JwtClaimsSet.builder()
            .issuer("data-wiz")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication!!.name)
            .build()
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    override fun getUserId(userName: String?): Int {
        return userRepository.findByUserName(userName)?.get()!!.userId!!
    }
}

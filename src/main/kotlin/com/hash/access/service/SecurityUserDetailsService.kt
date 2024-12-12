package com.hash.access.service

import com.hash.access.config.SecurityUser
import com.hash.access.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class SecurityUserDetailsService (
    @Autowired private val userRepository: UserRepository
): UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userName: String): UserDetails {
        val user: SecurityUser = userRepository.findByUserName(userName)?.map { it?.let { it1 -> SecurityUser(it1) } }!!
            .orElseThrow { UsernameNotFoundException("User not present") }
        return user
    }
}
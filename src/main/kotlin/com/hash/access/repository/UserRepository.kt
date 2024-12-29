package com.hash.access.repository

import com.hash.access.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Int> {
    fun findByUserName(userName: String): Optional<User>
}
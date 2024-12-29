package com.hash.access.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    var userId: Int? = null,

    var userName: String,

    var password: String,

    var profileName: String,

    var role: String? = null
)

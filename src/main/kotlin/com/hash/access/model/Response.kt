package com.hash.access.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response(
    var data: Any? = null,
    var status: Int,
    var message: String,
    var totalPages: Int? = null
)

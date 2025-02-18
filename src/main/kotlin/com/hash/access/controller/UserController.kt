package com.hash.access.controller

import com.hash.access.model.Response
import com.hash.access.model.ResponseData
import com.hash.access.model.User
import com.hash.access.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/user"], method = [RequestMethod.GET, RequestMethod.POST])
class UserController (@Autowired private val userService: UserService) {

    @PostMapping("/register")
    fun saveUser(@RequestBody user: User): ResponseEntity<Response> {

        val response: Response = if (userService.saveUser(user)) {
            Response(null, HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name)
        }else{
            Response(null, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name)
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body<Response>(response)
    }

    @GetMapping("/login")
    fun token(authentication: Authentication): ResponseEntity<Response> {
        val user: User = userService.getUserId(authentication.name)
        val data: ResponseData = ResponseData(userService.generateToken(authentication), user.userId!!, user.profileName  )
        val response: Response =
            Response(data, HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body<Response>(response)
    }

    @Deprecated("No longer required")
    @GetMapping("/me/{userName}")
    fun me(@PathVariable("userName") userName: String): ResponseEntity<Response> {
        val response: Response = Response(userService.getUserId(userName), HttpStatus.OK.value(), HttpStatus.OK.name)
        return ResponseEntity.status(HttpStatus.OK).body<Response>(response)
    }
}

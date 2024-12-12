package com.hash.access.controller

import com.hash.access.model.Response
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

    //    @Autowired
    //    var userService: UserService? = null

    //For Testing only
    @GetMapping("/test")
    fun test(): String {
        return "Never give up!!"
    }

    @PostMapping("/save")
    fun saveUser(@RequestBody user: User?): ResponseEntity<Response> {
        val response: Response =
            Response(userService.saveUser(user)?.userId, HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body<Response>(response)
    }

    @GetMapping("/login")
    fun token(authentication: Authentication?): ResponseEntity<Response> {
        val response: Response =
            Response(userService.generateToken(authentication), HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body<Response>(response)
    }

    @GetMapping("/me/{userName}")
    fun me(@PathVariable("userName") userName: String?): ResponseEntity<Response> {
        val response: Response = Response(userService.getUserId(userName), HttpStatus.OK.value(), HttpStatus.OK.name)
        return ResponseEntity.status(HttpStatus.OK).body<Response>(response)
    }
}

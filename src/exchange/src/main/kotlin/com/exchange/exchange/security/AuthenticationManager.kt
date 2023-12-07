package com.exchange.exchange.security

import com.exchange.exchange.domain.user.UserEntity
import com.exchange.exchange.exception.UnauthorizedException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * exchange-all
 *
 * @author uuhnaut69
 *
 */
@Component
class AuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val userEntity = authentication.principal as UserEntity

        if (userEntity.isEnabled.not()
            || userEntity.isAccountNonLocked.not()
            || userEntity.isAccountNonExpired.not()
            || userEntity.isCredentialsNonExpired.not()
        ) {
            return Mono.error { UnauthorizedException() }
        }

        return Mono.just(authentication)
    }
}
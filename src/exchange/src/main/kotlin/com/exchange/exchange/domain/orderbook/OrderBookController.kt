package com.exchange.exchange.domain.orderbook

import com.exchange.exchange.core.Response
import com.exchange.exchange.domain.user.UserEntity
import com.exchange.exchange.security.CurrentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author uuhnaut69
 *
 */
@Tag(name = "Order Book", description = "Order Book API")
@RestController
@RequestMapping("/api/v1/order-book")
class OrderBookController(
    private val orderBookService: OrderBookService
) {

    @Operation(
        summary = "Create ask limit order",
        description = "Create ask limit order"
    )
    @PostMapping("/create-ask-limit-order")
    suspend fun createAskLimitOrder(
        @CurrentUser currentUser: UserEntity,
        @RequestBody @Valid createAskLimitOrderRequest: AskLimitOrderRequest,
    ): Response<AskLimitOrderResponse> {
        return this.orderBookService.createAskLimitOrder(currentUser, createAskLimitOrderRequest)
    }

    @Operation(
        summary = "Create bid limit order",
        description = "Create bid limit order"
    )
    @PostMapping("/create-bid-limit-order")
    suspend fun createBidLimitOrder(
        @CurrentUser currentUser: UserEntity,
        @RequestBody @Valid createBidLimitOrderRequest: BidLimitOrderRequest,
    ): Response<BidLimitOrderResponse> {
        return this.orderBookService.createBidLimitOrder(currentUser, createBidLimitOrderRequest)
    }

    @Operation(
        summary = "Cancel bid limit order",
        description = "Cancel bid limit order"
    )
    @PostMapping("/cancel-ask-limit-order")
    suspend fun cancelAskLimitOrder(
        @CurrentUser currentUser: UserEntity,
        @RequestBody @Valid cancelAskLimitOrderRequest: CancelAskLimitOrderRequest,
    ): Response<AskLimitOrderResponse> {
        return this.orderBookService.cancelAskLimitOrder(currentUser, cancelAskLimitOrderRequest)
    }

    @Operation(
        summary = "Cancel bid limit order",
        description = "Cancel bid limit order"
    )
    @PostMapping("/cancel-bid-limit-order")
    suspend fun cancelBidLimitOrder(
        @CurrentUser currentUser: UserEntity,
        @RequestBody @Valid cancelBidLimitOrderRequest: CancelBidLimitOrderRequest,
    ): Response<BidLimitOrderResponse> {
        return this.orderBookService.cancelBidLimitOrder(currentUser, cancelBidLimitOrderRequest)
    }
}

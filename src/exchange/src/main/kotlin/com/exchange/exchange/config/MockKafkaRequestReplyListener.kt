package com.exchange.exchange.config

import com.exchange.exchange.core.CloudEventUtils
import com.exchange.exchange.core.ExternalEvent
import com.exchange.exchange.domain.balance.*
import io.cloudevents.CloudEvent
import io.cloudevents.core.builder.CloudEventBuilder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.Message
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*


/**
 * This class is used to mock and reply to the request from other services
 *
 * @author uuhnaut69
 *
 */
@Component
@Profile("standalone")
class MockKafkaRequestReplyListener {
    private val consumerSource = "order-book-service"

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MockKafkaRequestReplyListener::class.java)
    }

    @KafkaListener(id = "standalone-group", topics = ["\${kafka.order-book.request-topic}"])
    @SendTo("\${kafka.order-book.reply-topic}")
    fun fakeReply(command: CloudEvent): Message<CloudEvent> {
        val responseEventBuilder = CloudEventBuilder.v1()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create(consumerSource))

        when (command.type) {
            BalanceCommandType.CREATE_BALANCE.type -> {
                val createCommand = CloudEventUtils.cloudEventToObject(command, CreateBalanceCommand::class.java)
                responseEventBuilder.withType(BalanceEventType.CREATE_BALANCE_SUCCESS.type)
                        .withData(
                                CloudEventUtils.serializeData(
                                        ExternalEvent(
                                                createCommand!!,
                                                BalanceCreated(UUID.randomUUID().toString())
                                        )
                                )
                        )
            }

            BalanceCommandType.DEPOSIT_BALANCE.type -> {
                val depositCommand = CloudEventUtils.cloudEventToObject(command, DepositCommand::class.java)

                responseEventBuilder.withType(BalanceEventType.DEPOSIT_BALANCE_SUCCESS.type)
                        .withData(
                                CloudEventUtils.serializeData(
                                        ExternalEvent(
                                                depositCommand!!,
                                                BalanceDeposited(depositCommand.accountId)
                                        )
                                )
                        )
            }

            BalanceCommandType.WITHDRAW_BALANCE.type -> {
                val withdrawCommand = CloudEventUtils.cloudEventToObject(command, WithdrawCommand::class.java)

                responseEventBuilder.withType(BalanceEventType.WITHDRAW_BALANCE_SUCCESS.type)
                        .withData(
                                CloudEventUtils.serializeData(
                                        ExternalEvent(
                                                withdrawCommand!!,
                                                BalanceWithdrawn(withdrawCommand.accountId)
                                        )
                                )
                        )
            }
            // -- Add more cases here --
        }

        val responseEvent = responseEventBuilder.build()
        LOGGER.info("Fake reply event: $responseEvent")
        return MessageBuilder.withPayload(responseEvent).build()
    }
}

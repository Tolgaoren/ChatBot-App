package com.toren.geminiapp.domain.model

import com.toren.geminiapp.MessageRole

data class Message(
    val role: MessageRole,
    val message: String
)

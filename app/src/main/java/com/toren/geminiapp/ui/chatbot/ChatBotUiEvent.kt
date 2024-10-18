package com.toren.geminiapp.ui.chatbot

sealed class ChatBotUiEvent {
    data class TextFieldChanged(val value: String) : ChatBotUiEvent()
    object SendMessage : ChatBotUiEvent()
}
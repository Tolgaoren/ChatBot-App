package com.toren.geminiapp.ui.chatbot

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.Part
import com.google.ai.client.generativeai.type.TextPart
import com.toren.geminiapp.BuildConfig
import com.toren.geminiapp.MessageRole
import com.toren.geminiapp.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel
@Inject constructor() : ViewModel() {

    private val _textFieldState = mutableStateOf("")
    val textFieldState: State<String> = _textFieldState

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    fun onEvent(event: ChatBotUiEvent) {
        when (event) {
            is ChatBotUiEvent.TextFieldChanged -> {
                _textFieldState.value = event.value
            }

            ChatBotUiEvent.SendMessage -> {
                if (_textFieldState.value.isEmpty()) return
                _messages.add(Message(MessageRole.USER, _textFieldState.value))
                _messages.add(Message(MessageRole.MODEL, "Typing..."))
                sendMessage(_textFieldState.value)
            }
        }
    }


    private fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                _textFieldState.value = ""
                val chat = generativeModel.startChat(
                    history = messages.map {
                        Content(
                            role = it.role.toString(),
                            parts = listOf(TextPart(it.message))
                        )
                    },
                )
                val response = chat.sendMessage(message)
                _messages.removeLast()
                _messages.add(Message(MessageRole.MODEL, response.text.toString()))

            } catch (e: Exception) {
                _messages.removeLast()
                _messages.add(Message(MessageRole.MODEL, "Error: ${e.message}"))
            }
        }
    }

}
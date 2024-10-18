package com.toren.geminiapp.ui.chatbot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.toren.geminiapp.MessageRole
import com.toren.geminiapp.domain.model.Message
import com.toren.geminiapp.ui.theme.blue300
import com.toren.geminiapp.ui.theme.red300

@Composable
fun ChatBotScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatBotViewModel = hiltViewModel(),
) {

    val message by viewModel.textFieldState
    val messages = viewModel.messages

    Column {

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            reverseLayout = true
        ) {
            items(messages.size) {
                MessageItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    message = messages[it]
                )
            }
        }

        MessageInput(
            message = message,
            label = "Enter your message",
            onValueChange = {
                viewModel.onEvent(ChatBotUiEvent.TextFieldChanged(it))
            },
            onSendMessage = {
                viewModel.onEvent(ChatBotUiEvent.SendMessage)
            }
        )
    }
}

@Composable
fun MessageItem(
    modifier: Modifier = Modifier,
    message: Message,
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
        if (message.role == MessageRole.USER) {
            Arrangement.End
        } else {
            Arrangement.Start
        },
    ) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .widthIn(min = 0.dp, max = 300.dp),
            shape = RoundedCornerShape(10.dp),
            colors = if (message.role == MessageRole.USER) {
                CardDefaults.cardColors(red300)
            } else {
                CardDefaults.cardColors(blue300)
            }
        ) {
            Text(
                text = message.message,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}

@Composable
fun MessageInput(
    message: String,
    label: String,
    onValueChange: (String) -> Unit,
    onSendMessage: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 5.dp,
                bottom = 15.dp,
                start = 5.dp,
                end = 5.dp
            )
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label
                )
            },
            textStyle = TextStyle(color = Color.DarkGray),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(5.dp)
        )
        IconButton(
            onClick = onSendMessage,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(5.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send"
            )
        }
    }
}

@Preview
@Composable
private fun ChatBotScreenPreview() {
    ChatBotScreen()
}
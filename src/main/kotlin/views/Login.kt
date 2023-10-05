package views

import DatabaseConnection
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation

class Login(private val onLoggedIn: () -> Unit = {}) {
    @Composable
    fun Render() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val (username, setUsername) = remember { mutableStateOf("") }
            val (password, setPassword) = remember { mutableStateOf("") }
            val (hostUrl, setHostUrl) = remember { mutableStateOf("jdbc:mariadb://localhost:3306") }

            OutlinedTextField(
                modifier = Modifier.moveFocusOnTab(),
                value = username,
                onValueChange = { setUsername(it) },
                label = { Text("Username") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.moveFocusOnTab(),
                value = password,
                onValueChange = { setPassword(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.moveFocusOnTab(),
                value = hostUrl,
                onValueChange = { setHostUrl(it) },
                label = { Text("Host Url") },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                try {
                    DatabaseConnection.connect(username, password, hostUrl)
                    onLoggedIn()
                } catch (e: Exception) {
                    println("Error during connection: ${e.message}")
                }
            }) {
                Text("Login",
                    modifier = Modifier.moveFocusOnTab(),
                )
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    fun Modifier.moveFocusOnTab() = composed {
        val focusManager = LocalFocusManager.current
        onPreviewKeyEvent {
            if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
                focusManager.moveFocus(
                    if (it.isShiftPressed) FocusDirection.Previous else FocusDirection.Next
                )
                true
            } else {
                false
            }
        }
    }
}
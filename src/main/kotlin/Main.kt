import views.Login
import views.QueryInput
import views.DatabaseChoice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

enum class Screen {
    LOGIN,
    DATABASE_CHOICE,
    QUERY_INPUT
}

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose Desktop App",
        state = rememberWindowState(
            position = WindowPosition(Alignment.Center), size = DpSize(1280.dp, 768.dp)
        )
    ) {
        AppNavigator()
    }
}

@Composable
fun AppNavigator() {
    val currentScreen = remember { mutableStateOf(Screen.LOGIN) }
    when (currentScreen.value) {
        Screen.LOGIN -> Login {
            currentScreen.value = Screen.DATABASE_CHOICE
        }.Render()

        Screen.DATABASE_CHOICE -> DatabaseChoice { _ ->
            currentScreen.value = Screen.QUERY_INPUT
        }.Render()

        Screen.QUERY_INPUT -> QueryInput().Render()
    }
}

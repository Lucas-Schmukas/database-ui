package views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import DatabaseConnection
import androidx.compose.foundation.border
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.jooq.Record
import org.jooq.Result

class DatabaseChoice(private val onDatabaseChosen: (String) -> Unit) {

    private val databases: Result<Record>? = DatabaseConnection.databases()

    @Composable
    fun Render() {
        var expanded by remember { mutableStateOf(false) }
        var selectedDatabase by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)) {
                TextButton(modifier = Modifier.border(1.dp, Color.Gray), onClick = { expanded = true }) {
                    Text(selectedDatabase ?: "Please choose a database")
                }
                DropdownMenu(
                    modifier = Modifier.border(1.dp, Color.Gray),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    databases?.forEach { dbName ->
                        val name = dbName.getValue("Database", String::class.java) // Verschieben Sie dies hierher
                        DropdownMenuItem(onClick = {
                            selectedDatabase = name
                            expanded = false
                        }) {
                            Text(name)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                selectedDatabase?.let {
                    DatabaseConnection.choose(it) // Ändern Sie 'name' zu 'it'
                    onDatabaseChosen(it)
                }
            }) {
                Text("Accept")
            }
        }
    }
}

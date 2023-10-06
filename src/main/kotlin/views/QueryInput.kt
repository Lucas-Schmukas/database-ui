package views

import DatabaseConnection
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jooq.Record
import org.jooq.Result

class QueryInput {
    @Composable
    fun Render() {
        val fontSize = remember { mutableStateOf(16.sp) }
        Box() {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
            ) {
                Button(
                    modifier = Modifier.padding(end = 6.dp),
                    onClick = {
                        fontSize.value *= 1.5f
                    }) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Enlarge Font Size")
                    Text("Aa", fontSize = 20.sp)
                }
                Button(
                    modifier = Modifier.padding(end = 6.dp),
                    onClick = {
                        fontSize.value *= 0.67f
                    }) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Shrink Font Size")
                    Text("Aa", fontSize = 20.sp)
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val (query, setQuery) = remember { mutableStateOf("") }
            var result by remember { mutableStateOf<Result<Record>?>(null) }
            TextField(
                value = query,
                onValueChange = { setQuery(it) },
                label = { Text("Enter SQL Query", fontSize = fontSize.value) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                result = DatabaseConnection.execute(query)
            }) {
                Text("Execute Query", fontSize = fontSize.value)
            }

            Spacer(modifier = Modifier.height(16.dp))
            result?.let {
                Box(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)) {
                    LazyColumn {
                        item {
                            Row(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)) {
                                it[0].fields().forEach { field ->
                                    Text(
                                        text = field.name,
                                        style = TextStyle(
                                            fontSize = fontSize.value,
                                            fontFamily = FontFamily.Default),
                                        modifier = Modifier.weight(1f).border(1.dp, Color.Gray),
                                    )
                                }
                            }
                        }

                        items(it.size) { rowIndex ->
                            val row = it[rowIndex]
                            Row(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)) {
                                for (field in row.fields()) {
                                    Text(
                                        text = field.getValue(row).toString(),
                                        modifier = Modifier.weight(1f).border(1.dp, Color.Gray),
                                        fontSize = fontSize.value
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

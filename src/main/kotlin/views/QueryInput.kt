package views

import DatabaseConnection
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jooq.Record
import org.jooq.Result

class QueryInput {
    @Composable
    fun Render() {
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
                label = { Text("Enter SQL Query") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                result = DatabaseConnection.execute(query)
            }) {
                Text("Execute Query")
            }

            Spacer(modifier = Modifier.height(16.dp))
            result?.let {
                Box(modifier = Modifier.fillMaxSize().border(1.dp, Color.Gray)) {
                    LazyColumn {
                        item {
                            Row(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)) {
                                it[0].fields().forEach { field ->
                                    Text(
                                        text = field.name,
                                        modifier = Modifier.weight(1f).border(1.dp, Color.Gray)
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
                                        modifier = Modifier.weight(1f).border(1.dp, Color.Gray)
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

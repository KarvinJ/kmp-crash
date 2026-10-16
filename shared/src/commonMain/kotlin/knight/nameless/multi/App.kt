package knight.nameless.multi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import knight.nameless.multi.entities.Person
import kotlin.collections.plus

@Composable
@Preview
fun App() {

    MaterialTheme {

        var showCreateDialog by remember { mutableStateOf(false) }

        var persons by remember {

            mutableStateOf(listOf<Person>())
        }

        var personToEdit by remember {
            mutableStateOf<Person?>(null)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Registration Form",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth() // Why fillMaxWidth()? It gives the Text the full
                // available width, so TextAlign.Center can center the text within it.
            )

            Button(
                onClick = {
                    showCreateDialog = true
                    personToEdit = null
                },

                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add user")
            }

            if (showCreateDialog) {

                PersonDialog(
                    onAdd = { person ->
                        persons = persons + person
                    },
                    onDismiss = {
                        showCreateDialog = false
                    },
                    onEdit = { person ->

                        showCreateDialog = false
//     This code updates a person in your persons list by matching their id.
//     It's useful for implementing the Edit operation in your CRUD app.
                        persons = persons.map {

                            if (it.id == person.id) {
                                person
                            } else {
                                it
                            }
                        }
                    },
                    personToEdit = personToEdit
                )
            }

            PersonList(
                persons = persons,
//            De esta forma enviamos funciones por parametro
                onDelete = { personToRemove ->
                    persons = persons.filter { it != personToRemove }
                },
                onEdit = { person ->

                    showCreateDialog = true
                    personToEdit = person
                }
            )
        }

//        ShowPlatform(showContent)
    }
}

@Composable
fun PersonList(
    persons: List<Person>,
    onEdit: (Person) -> Unit,
    onDelete: (Person) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(persons) { person ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${person.name} ${person.lastName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = person.phoneNumber,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { onEdit(person) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit person",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = { onDelete(person) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete person",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
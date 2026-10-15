package knight.nameless.multi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import knight.nameless.multi.entities.Person
import kotlin.collections.plus
import kotlin.random.Random
import kotlin.uuid.Uuid.Companion.random

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
fun PersonDialog(
//    When I need to modify values of a variable that is in the parent component I need to handle
//    this as a callback. Cuz all the variables send to the child component are immutable.
//    And the only way to mut is with a callBack function
    onAdd: (Person) -> Unit,
    onDismiss: () -> Unit,
    onEdit: (Person) -> Unit,
    personToEdit: Person? = null
) {

    //    Equivalentes a estados en react esto es lo que utilzaremose n kotlin
    var name by remember {
        mutableStateOf(personToEdit?.name ?: "")
    }
    var lastName by remember {
        mutableStateOf(personToEdit?.lastName ?: "")
    }
    var phoneNumber by remember {
        mutableStateOf(personToEdit?.phoneNumber ?: "")
    }

    var showErrors by remember { mutableStateOf(false) }

    //            AlertDialog is the ideal component to add extra data in a list
    AlertDialog(
//
//                This is the equivalent to onClose() in react, called when the user tries to
//                dismiss the Dialog by clicking outside or pressing the back button.
//                This is not called when the dismiss button is clicked.
        onDismissRequest = {
            onDismiss()
            showErrors = false
        },

        title = {
            Text("Add person")
        },

//                text which presents the details regarding the dialog's purpose.
//                Here will put our design dialog code.
        text = {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true, // We need to add this value in true so if I hit tab
                    // then the text input will jump to the next field.
                    isError = name.isBlank() && showErrors,
                    supportingText = {
                        if (name.isBlank() && showErrors) {
                            Text("Name is required")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = lastName.isBlank() && showErrors,
                    supportingText = {
                        if (lastName.isBlank() && showErrors) {
                            Text("Last name is required")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        phoneNumber =
                            Formatters.buildFormattedPhoneNumber(digits)
                    },
                    label = { Text("Phone number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = phoneNumber.isBlank() && showErrors,
                    supportingText = {
                        if (phoneNumber.isBlank() && showErrors) {
                            Text("Phone number is required")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },

//                The confirmButton comes define in the AlertDialog.
        confirmButton = {

            Button(
                onClick = {
                    if (
                        name.isBlank() ||
                        lastName.isBlank() ||
                        phoneNumber.isBlank()
                    ) {
                        showErrors = true
                        return@Button
                    }

                    if (personToEdit == null) {

                        val personToCreate = Person(
                            id = Random.nextInt(1, Int.MAX_VALUE),
                            name = name.trim(),
                            lastName = lastName.trim(),
                            phoneNumber = phoneNumber
                        )

                        onAdd(personToCreate)
                    }
                    else {
                        val personToUpdate = Person(
                            id = personToEdit.id,
                            name = name.trim(),
                            lastName = lastName.trim(),
                            phoneNumber = phoneNumber
                        )

                        onEdit(personToUpdate)
                    }

                    name = ""
                    lastName = ""
                    phoneNumber = ""
                    showErrors = false
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },

//                button which is meant to dismiss the dialog. Normally this will be use for the cancel button
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    showErrors = false
                }
            ) {
                Text("Cancel")
            }
        }
    )
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

@Composable
private fun ShowPlatform(showContent: Boolean) {

    AnimatedVisibility(showContent) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Hello ${Greeting().greet()}")
        }
    }
}
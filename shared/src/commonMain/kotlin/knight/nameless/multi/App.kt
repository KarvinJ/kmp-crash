package knight.nameless.multi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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

@Composable
@Preview
fun App() {
    MaterialTheme {

        PersonForm()
    }
}

@Composable
fun PersonForm() {

//    Equivalentes a estados en react esto es lo que utilzaremose n kotlin
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    var persons by remember {

        mutableStateOf(listOf<Person>())
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
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add user")
        }

        if (showCreateDialog) {

//            AlertDialog is the ideal component to add extra data in a list
            AlertDialog(
//
//                This is the equivalent to onClose() in react, called when the user tries to
//                dismiss the Dialog by clicking outside or pressing the back button.
//                This is not called when the dismiss button is clicked.
                onDismissRequest = {
                    showCreateDialog = false
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
                            singleLine = true,
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

                            persons = persons + Person(
                                name = name.trim(),
                                lastName = lastName.trim(),
                                phoneNumber = phoneNumber
                            )

                            name = ""
                            lastName = ""
                            phoneNumber = ""
                            showErrors = false
                            showCreateDialog = false
                        }
                    ) {
                        Text("Save")
                    }
                },

//                button which is meant to dismiss the dialog. Normally this will be use for the cancel button
                dismissButton = {
                    TextButton(
                        onClick = {
                            showCreateDialog = false
                            showErrors = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

//        ShowPlatform(showContent)

        PersonList(
            persons = persons,
//            De esta forma enviamos funciones por parametro
            onDelete = { personToRemove ->
                persons = persons.filter { it != personToRemove }
            }
        )
    }
}

@Composable
fun PersonList(
    persons: List<Person>,
//    asi recibimos funciones por parametro
    onDelete: (Person) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(persons) { person ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${person.name} ${person.lastName}, ${person.phoneNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Button(
                        onClick = { onDelete(person) },
                    ) {
                        Text("Delete")
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
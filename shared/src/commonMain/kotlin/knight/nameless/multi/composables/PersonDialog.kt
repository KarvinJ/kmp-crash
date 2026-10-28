package knight.nameless.multi.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import knight.nameless.multi.data.entities.Person
import kotlin.random.Random

//I can simply move this composable function to other files for reuse purposes.
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
                        phoneNumber = buildFormattedPhoneNumber(digits)
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

fun buildFormattedPhoneNumber(phoneNumber: String): String {

    val digitsOnly = phoneNumber.replace("\\D".toRegex(), "")
    val formattedNumber = StringBuilder()

    for (i in digitsOnly.indices) {

        when (i) {
            0 -> formattedNumber.append("(")
            3 -> formattedNumber.append(") ")
            6 -> formattedNumber.append("-")
        }

        formattedNumber.append(digitsOnly[i])

        if (formattedNumber.length >= 14)
            break
    }

    return formattedNumber.toString()
}
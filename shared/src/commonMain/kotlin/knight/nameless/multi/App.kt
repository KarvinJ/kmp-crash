package knight.nameless.multi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    var showContent by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    var persons by remember {

        mutableStateOf(listOf<Person>())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Registration Form",
            style = MaterialTheme.typography.headlineSmall,
        )

        Button(
            onClick = {
                showContent = !showContent
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add user")
        }

        AnimatedVisibility(showContent) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(36.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),

                    isError = name.isBlank() && showErrors,
                    supportingText = {
                        if (name.isBlank() && showErrors) {
                            Text("Name is required")
                        }
                    }
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    isError = lastName.isBlank() && showErrors,
                    supportingText = {
                        if (lastName.isBlank() && showErrors) {
                            Text("Last name is required")
                        }
                    }
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->

                        val digit = input.filter { it.isDigit() }
                        phoneNumber = Formatters.buildFormattedPhoneNumber(digit)
                    },
                    label = { Text("Phone number") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next

                    ),
                    isError = phoneNumber.isBlank() && showErrors,
                    supportingText = {
                        if (phoneNumber.isBlank() && showErrors) {
                            Text("Phone number is required")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(

                    onClick = {

                        if (name.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {

                            showErrors = true

                            return@Button
                        }

                        showErrors = false

                        persons += Person(
                            name = name,
                            lastName = lastName,
                            phoneNumber = phoneNumber
                        )

                        name = ""
                        lastName = ""
                        phoneNumber = ""

                        showContent = !showContent
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }

        }

//        ShowPlatform(showContent)

        PersonList(persons = persons)
    }
}

@Composable
fun PersonList(
    persons: List<Person>,
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
                    .padding(16.dp)
            ) {
                Text(
                    text = "${person.name} ${person.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = person.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
package knight.nameless.multi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import knight.nameless.multi.entities.Person
import knight.nameless.multi.entities.PersonDao

@Composable
@Preview
fun App(/*personDao: PersonDao*/) {
    MaterialTheme {

        PersonForm(/*personDao*/)
    }
}

@Composable
fun PersonForm(/*personDao: PersonDao*/) {

//    Equivalentes a estados en react esto es lo que utilzaremose n kotlin
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showContent by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//
//        val persons = listOf(
//            Person(
//                name = "John",
//                lastName = "Doe",
//                phoneNumber = "1234567890"
//            ),
//
//            Person(
//                name = "John1",
//                lastName = "Doe1",
//                phoneNumber = "1234567890"
//            ),
//
//        )
//
//        persons.forEach { person ->
//            personDao.upsert(person)
//        }
//
//        personDao.getAll().collect { persons ->
//            println("Persons: $persons")
//        }
//    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { input ->

                val digit = input.filter { it.isDigit() }
                phoneNumber = Formatters.buildFormattedPhoneNumber(digit)
            },
            label = { Text("Phone number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                showContent = !showContent
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        AnimatedVisibility(showContent) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Hello ${Greeting().greet()}")
                Text("Name: $name, Last name: $lastName, Phone number: $phoneNumber")
            }
        }
    }
}
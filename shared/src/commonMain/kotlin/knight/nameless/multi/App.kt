package knight.nameless.multi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import knight.nameless.multi.data.DbFactory
import knight.nameless.multi.data.PersonRepository
import knight.nameless.multi.ui.composables.NetworkCall
import knight.nameless.multi.ui.composables.PersonDialog
import knight.nameless.multi.ui.composables.PersonList
import knight.nameless.multi.data.entities.Person
import knight.nameless.multi.data.toEntity
import knight.nameless.multi.networking.UserClient
import knight.nameless.multi.ui.PersonViewModel
import knight.nameless.multi.ui.composables.PersonListRoom
import knight.nameless.multi.ui.personViewModelFactory
import kotlin.collections.plus

//We will work with the dbFactory as null to allow not sending the dbFactory for the web mains for
// now. Cuz I definitely need this dbFactory to not be null
@Composable
fun App(client: UserClient, dbFactory: DbFactory? = null) {

    val appDatabase = remember { dbFactory?.createDatabase() }

    val repository = PersonRepository(appDatabase?.personDao())

    val personViewModel: PersonViewModel = viewModel(
        factory = personViewModelFactory(repository)
    )

    val personsDb = personViewModel.persons?.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showPersonDialog by remember { mutableStateOf(false) }

    var persons by remember {

        mutableStateOf(listOf<Person>())
    }

    var personToEdit by remember {
        mutableStateOf<Person?>(null)
    }

    var selectedScreen by remember { mutableIntStateOf(0) }

//    The MaterialTheme and scaffold They have different responsibilities:
//- MaterialTheme defines the visual system: colors, typography, and shapes.
//- Scaffold provides the structure for a screen: top app bar, content, floating action button,
// bottom bar, and snack bar

    MaterialTheme {

//        Scaffold give us a standard structure on how to design the ui of our app,
//        And also give us several components to add in our app. like floating buttons, top app
        //        bar and bottom app bar.
        Scaffold(

            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {

                        if (selectedScreen == 0) {

                            Text("Network Test")
                        } else {

                            Text("Registration Form")
                        }
                    }
                )
            },

            floatingActionButton = {

                if (selectedScreen == 1 || selectedScreen == 2) {

                    FloatingActionButton(
                        onClick = {
                            personToEdit = null
                            showCreateDialog = true
                            showPersonDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add person"
                        )
                    }
                }
            },

//            adding the bottom bar
            bottomBar = {

//                we normally add the navigation bar here in the bottom bar.
                NavigationBar {

                    NavigationBarItem(
                        selected = selectedScreen == 0,
                        onClick = { selectedScreen = 0 },
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Register") }
                    )

                    NavigationBarItem(
                        selected = selectedScreen == 1,
                        onClick = {
                            selectedScreen = 1
                            showCreateDialog = false
                        },
                        icon = { Icon(Icons.Default.People, null) },
                        label = { Text("People") }
                    )

                    NavigationBarItem(
                        selected = selectedScreen == 2,
                        onClick = {
                            selectedScreen = 2
                            showPersonDialog = false
                        },
                        icon = { Icon(Icons.Default.Accessibility, null) },
                        label = { Text("People") }
                    )
                }
            }

        ) { innerPadding ->

            Box(Modifier.padding(innerPadding)) {

                when (selectedScreen) {
                    0 -> NetworkCall(client)
                    1 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            if (showCreateDialog) {

                                PersonDialog(
                                    personToEdit = personToEdit,
                                    onDismiss = {
                                        showCreateDialog = false
                                    },
                                    onAdd = { person ->
                                        persons = persons + person
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
                    }

                    2 -> {

//                            Code for handling the crud with the room database
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            if (showPersonDialog) {

                                PersonDialog(

                                    onAdd = { person ->
                                        personViewModel.upsertPerson(person.toPersonModel())
                                        showPersonDialog = false
                                    },
                                    onDismiss = {
                                        showPersonDialog = false
                                    },
                                    onEdit = { person ->

                                        personViewModel.upsertPerson(person.toPersonModel())
                                        showPersonDialog = false
                                    },
                                    personToEdit = personToEdit
                                )
                            }

                            PersonListRoom(
                                persons = personsDb?.value ?: emptyList(),
                                onDelete = { personToRemove ->

                                    personViewModel.deletePerson(personToRemove)
                                },
                                onEdit = { person ->

                                    showPersonDialog = true
                                    personToEdit = person.toEntity()
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}
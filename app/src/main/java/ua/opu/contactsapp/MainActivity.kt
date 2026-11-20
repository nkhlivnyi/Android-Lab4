package ua.opu.contactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.opu.contactsapp.data.local.DatabaseProvider
import ua.opu.contactsapp.data.repository.ContactRepository
import ua.opu.contactsapp.navigation.AppNavigation
import ua.opu.contactsapp.ui.theme.ContactsAppTheme
import ua.opu.contactsapp.ui.viewmodel.ContactViewModel
import ua.opu.contactsapp.ui.viewmodel.ContactViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ContactsAppTheme {
                ContactsApp()
            }
        }
    }
}

@Composable
fun ContactsApp() {
    val context = LocalContext.current
    val database = DatabaseProvider.getDatabase(context)
    val repository = ContactRepository(database.contactDao())
    val viewModel: ContactViewModel = viewModel(factory = ContactViewModelFactory(repository))

    AppNavigation(viewModel = viewModel)
}

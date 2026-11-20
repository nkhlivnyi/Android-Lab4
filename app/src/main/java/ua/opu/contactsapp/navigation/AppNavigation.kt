package ua.opu.contactsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.opu.contactsapp.ui.screens.AddContactScreen
import ua.opu.contactsapp.ui.screens.ContactsListScreen
import ua.opu.contactsapp.ui.screens.DetailsContactScreen
import ua.opu.contactsapp.ui.viewmodel.ContactDetailsUiState
import ua.opu.contactsapp.ui.viewmodel.ContactViewModel
import ua.opu.contactsapp.ui.viewmodel.ContactsListUiState

object AppRoutes {
    const val CONTACT_ID = "contactId"
    const val CONTACTS_LIST = "contacts"
    const val CONTACT_ADD = "add"
    const val CONTACT_EDIT = "edit/{$CONTACT_ID}"
    const val CONTACT_DETAILS = "details/{$CONTACT_ID}"

    fun contactEdit(contactId: Int): String = "edit/$contactId"

    fun contactDetails(contactId: Int): String = "details/$contactId"
}

@Composable
fun AppNavigation(
    viewModel: ContactViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.CONTACTS_LIST
    ) {
        composable(route = AppRoutes.CONTACTS_LIST) {
            val contacts by viewModel.contacts.collectAsStateWithLifecycle()

            ContactsListScreen(
                uiState = ContactsListUiState(contacts = contacts),
                onAddClick = {
                    viewModel.prepareNewContact()
                    navController.navigate(AppRoutes.CONTACT_ADD)
                },
                onContactClick = { contactId ->
                    navController.navigate(AppRoutes.contactDetails(contactId))
                }
            )
        }

        composable(route = AppRoutes.CONTACT_ADD) {
            val uiState by viewModel.addEditContactUiState.collectAsStateWithLifecycle()

            AddContactScreen(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onPhoneChange = viewModel::updatePhone,
                onEmailChange = viewModel::updateEmail,
                onSaveClick = {
                    viewModel.saveContact()
                    navController.popBackStack()
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.CONTACT_EDIT,
            arguments = listOf(navArgument(AppRoutes.CONTACT_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt(AppRoutes.CONTACT_ID)

            LaunchedEffect(contactId) {
                if (contactId != null) {
                    viewModel.prepareEditContact(contactId)
                }
            }

            val uiState by viewModel.addEditContactUiState.collectAsStateWithLifecycle()

            AddContactScreen(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onPhoneChange = viewModel::updatePhone,
                onEmailChange = viewModel::updateEmail,
                onSaveClick = {
                    viewModel.saveContact()
                    navController.popBackStack()
                },
                onCancelClick = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.CONTACT_DETAILS,
            arguments = listOf(navArgument(AppRoutes.CONTACT_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt(AppRoutes.CONTACT_ID) ?: 0
            val contactFlow = remember(contactId) { viewModel.getContactById(contactId) }
            val contact by contactFlow.collectAsStateWithLifecycle(initialValue = null)

            DetailsContactScreen(
                uiState = ContactDetailsUiState(contact = contact),
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(AppRoutes.contactEdit(id))
                },
                onDeleteClick = {
                    contact?.let { viewModel.deleteContact(it) }
                    navController.popBackStack()
                }
            )
        }
    }
}

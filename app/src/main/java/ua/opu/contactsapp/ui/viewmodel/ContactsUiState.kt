package ua.opu.contactsapp.ui.viewmodel

import ua.opu.contactsapp.domain.model.Contact

data class ContactsListUiState(
    val contacts: List<Contact> = emptyList()
)

data class AddEditContactUiState(
    val contactId: Int? = null,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val isEditMode: Boolean = false
)

data class ContactDetailsUiState(
    val contact: Contact? = null
)

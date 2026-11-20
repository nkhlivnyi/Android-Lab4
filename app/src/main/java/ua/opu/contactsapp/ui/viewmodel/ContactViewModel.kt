package ua.opu.contactsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.opu.contactsapp.data.repository.ContactRepository
import ua.opu.contactsapp.domain.model.Contact

class ContactViewModel(
    private val repository: ContactRepository
) : ViewModel() {

    val contacts: StateFlow<List<Contact>> = repository.contacts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _addEditContactUiState = MutableStateFlow(AddEditContactUiState())
    val addEditContactUiState: StateFlow<AddEditContactUiState> = _addEditContactUiState.asStateFlow()

    fun getContactById(id: Int): Flow<Contact?> = repository.getContactById(id)

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            repository.addContact(contact)
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.updateContact(contact)
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun prepareNewContact() {
        _addEditContactUiState.value = AddEditContactUiState()
    }

    fun prepareEditContact(contactId: Int) {
        viewModelScope.launch {
            val contact = repository.getContactById(contactId).first() ?: return@launch
            _addEditContactUiState.value = AddEditContactUiState(
                contactId = contact.id,
                name = contact.name,
                phone = contact.phone,
                email = contact.email,
                isEditMode = true
            )
        }
    }

    fun updateName(name: String) {
        _addEditContactUiState.update { it.copy(name = name) }
    }

    fun updatePhone(phone: String) {
        _addEditContactUiState.update { it.copy(phone = phone) }
    }

    fun updateEmail(email: String) {
        _addEditContactUiState.update { it.copy(email = email) }
    }

    fun saveContact() {
        val state = _addEditContactUiState.value
        if (state.name.isBlank() || state.phone.isBlank()) return

        val contact = Contact(
            id = state.contactId ?: 0,
            name = state.name.trim(),
            phone = state.phone.trim(),
            email = state.email.trim()
        )

        if (state.isEditMode) {
            updateContact(contact)
        } else {
            addContact(contact)
        }
    }
}

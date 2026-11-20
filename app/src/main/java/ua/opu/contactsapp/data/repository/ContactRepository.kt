package ua.opu.contactsapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.opu.contactsapp.data.local.ContactDao
import ua.opu.contactsapp.data.mapper.toContact
import ua.opu.contactsapp.data.mapper.toEntity
import ua.opu.contactsapp.domain.model.Contact

class ContactRepository(
    private val contactDao: ContactDao
) {

    val contacts: Flow<List<Contact>> = contactDao.getAllContacts().map { entities ->
        entities.map { it.toContact() }
    }

    fun getContactById(id: Int): Flow<Contact?> {
        return contactDao.getContactById(id).map { it?.toContact() }
    }

    suspend fun addContact(contact: Contact) {
        contactDao.insertContact(contact.toEntity())
    }

    suspend fun updateContact(contact: Contact) {
        contactDao.updateContact(contact.toEntity())
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact.toEntity())
    }
}

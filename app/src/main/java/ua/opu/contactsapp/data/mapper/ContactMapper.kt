package ua.opu.contactsapp.data.mapper

import ua.opu.contactsapp.data.local.ContactEntity
import ua.opu.contactsapp.domain.model.Contact

fun ContactEntity.toContact(): Contact {
    return Contact(
        id = id,
        name = name,
        phone = phone,
        email = email
    )
}

fun Contact.toEntity(): ContactEntity {
    return ContactEntity(
        id = id,
        name = name,
        phone = phone,
        email = email
    )
}

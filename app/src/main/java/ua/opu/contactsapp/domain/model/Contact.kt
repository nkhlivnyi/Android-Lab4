package ua.opu.contactsapp.domain.model

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String
)

fun Contact.initials(): String {
    return name.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
}

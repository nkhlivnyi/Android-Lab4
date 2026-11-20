package ua.opu.contactsapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.opu.contactsapp.domain.model.Contact
import ua.opu.contactsapp.domain.model.initials
import ua.opu.contactsapp.ui.theme.CardBorder
import ua.opu.contactsapp.ui.theme.SecondaryText
import ua.opu.contactsapp.ui.viewmodel.ContactDetailsUiState

@Composable
fun DetailsContactScreen(
    uiState: ContactDetailsUiState,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Scaffold(
        topBar = { ContactsTopBar(title = "Деталі контакту", onBackClick = onBackClick) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        val contact = uiState.contact
        if (contact == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Контакт не знайдено", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                FormButton(text = "Назад", onClick = onBackClick)
            }
        } else {
            DetailsContent(
                contact = contact,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun DetailsContent(
    contact: Contact,
    modifier: Modifier,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InitialsAvatar(initials = contact.initials(), size = 88.dp, fontSize = 34.sp)
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        InfoField(label = "Телефон", value = contact.phone)
        InfoField(label = "Email", value = contact.email)

        FormButton(text = "Редагувати", onClick = { onEditClick(contact.id) })
        FormButton(text = "Видалити", onClick = { onDeleteClick(contact.id) })
        FormButton(text = "Назад", onClick = onBackClick)
    }
}

@Composable
private fun InfoField(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

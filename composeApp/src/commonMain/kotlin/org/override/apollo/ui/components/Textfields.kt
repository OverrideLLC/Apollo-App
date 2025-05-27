package org.override.apollo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    emailText: String,
    textState: (String) -> Unit
) {
    OutlinedTextField(
        value = emailText,
        onValueChange = { textState(it) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(text = "Correo electrónico") },
        keyboardOptions = KeyboardOptions(keyboardType = Email),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        )

    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    passwordText: String,
    textState: (String) -> Unit
) {
    OutlinedTextField(
        value = passwordText,
        onValueChange = { textState(it) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        )

    )
}
package com.example.enishopcompose.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.enishopcompose.utils.DateConverter
import java.util.Date

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun FormRowSurface(
    modifier: Modifier = Modifier,
    formRow: @Composable () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier.padding(4.dp)
    ) {
        formRow()
    }

}


@Composable
fun FormTextRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    FormRowSurface {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = label, Modifier.padding(top = 4.dp, bottom = 4.dp), fontSize = 24.sp
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = keyboardOptions
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateRow(
    label: String,
    value: Date,
    modifier: Modifier = Modifier,
    onValueChange: (Date) -> Unit
) {
    var isDatePickerVisible by rememberSaveable {
        mutableStateOf(false)
    }
    //permet de récupérer la date sélectionnée
    val state = rememberDatePickerState(
        //je commence avec un calendrier
        initialDisplayMode = DisplayMode.Picker,
        //la date du jour est sélectionné
        initialSelectedDateMillis = DateConverter.convertDateToMillis(value)
    )

    FormRowSurface {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = label, Modifier.padding(top = 4.dp, bottom = 4.dp), fontSize = 24.sp
            )
            TextField(
                value = DateConverter.convertDateToSimpleString(value),
                onValueChange = { },
                //pour rendre l'élément clickable
                //je dois désactiver son foncitonnement par défaut
                enabled = false,
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    //est clickable si enabled est à false
                    .clickable {
                        isDatePickerVisible = true
                    }
            )
        }
    }

    if (isDatePickerVisible) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(
                rememberScrollState()
            ),
            onDismissRequest = { isDatePickerVisible = false },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onValueChange(
                                DateConverter.convertMillisToDate(
                                    state.selectedDateMillis ?: Date().time
                                )
                            )
                            isDatePickerVisible = false
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            }
        ) {
            DatePicker(
                state = state,
                title = {
                    Text(text = "")
                },
                headline = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Choisir une date", modifier = Modifier.padding(4.dp))
                    }
                },
                showModeToggle = false
            )
        }
    }
}

@Composable
fun TitleApp(modifier: Modifier = Modifier) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shop",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ENI-SHOP",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 40.sp
            )
        }
    }
}


@Preview
@Composable
fun FormTextRowPreview() {
    LoadingScreen()
}
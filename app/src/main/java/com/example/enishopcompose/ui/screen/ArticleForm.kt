package com.example.enishopcompose.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.enishopcompose.ui.common.FormDateRow
import com.example.enishopcompose.ui.common.FormTextRow
import com.example.enishopcompose.vm.ArticleViewModel


@Composable
fun ArticleFormScreen(
    modifier: Modifier = Modifier,
    articleViewModel: ArticleViewModel = viewModel(
        factory = ArticleViewModel.Factory
    ),
    onClickOnSave: () -> Unit
) {
    val contextForToast = LocalContext.current.applicationContext

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        ArticleForm(articleViewModel = articleViewModel)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                articleViewModel.insertArticle()
                //contact api pour ajouter un nouvel article
                Toast.makeText(
                    contextForToast,
                    "Sauvegarde éffectuée",
                    Toast.LENGTH_LONG
                ).show()
                onClickOnSave()
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Enregistrer")
        }
    }
}

@Composable
fun ArticleForm(
    modifier: Modifier = Modifier,
    articleViewModel: ArticleViewModel
) {
    Column {
        FormTextRow(
            label = "Titre",
            value = articleViewModel.name,
            onValueChange = {
                articleViewModel.name = it
            }
        )
        FormTextRow(
            label = "Description",
            value = articleViewModel.description,
            onValueChange = {
                articleViewModel.description = it
            }
        )
        FormTextRow(
            label = "Prix",
            value = articleViewModel.price.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                articleViewModel.price = it.toFloat()
            }
        )
        FormDateRow(
            label = "Sortie initiale",
            value = articleViewModel.date,
            onValueChange = { articleViewModel.date = it }
        )

    }
}

@Preview
@Composable
fun ArticleFormPreview() {
    ArticleFormScreen() {}
}
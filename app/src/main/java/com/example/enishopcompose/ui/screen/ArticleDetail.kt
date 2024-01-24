package com.example.enishopcompose.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.ui.common.LoadingScreen
import com.example.enishopcompose.utils.DateConverter
import com.example.enishopcompose.vm.ArticleViewModel

@Composable
fun ArticleDetailScreen(
    modifier: Modifier = Modifier,
    articleId: Long,
    articleViewModel: ArticleViewModel = viewModel(
        factory = ArticleViewModel.Factory
    )
) {
    LaunchedEffect(Unit) {
        articleViewModel.getArticleById(articleId)
        articleViewModel.getArticleFavById(articleId)
    }

    val article by articleViewModel.article
    val isLoading by articleViewModel.isLoading
    var checkedFav by articleViewModel.checkedFav

    if (!isLoading) {
        LoadingScreen()
    } else {
        Column(
            modifier = modifier.verticalScroll(
                rememberScrollState()
            )
        ) {
            ArticleDetail(
                article = article,
                checkedFav = checkedFav,
                onCheckedChange = {
                    checkedFav = it
                    if (checkedFav) {
                        articleViewModel.insertArticleFav()
                    } else {
                        articleViewModel.deleteArticleFav()
                    }
                }
            )
        }
    }
}

@Composable
fun ArticleDetail(
    article: Article,
    checkedFav: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = article.name,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.fr/search?q=eni+shop+${article.name}")
                    ).also { context.startActivity(it) }
                },
            textAlign = TextAlign.Justify,
            lineHeight = 1.em
        )
        AsyncImage(
            model = article.urlImage, contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = article.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Prix : ${String.format("%.2f", article.price)} €")
            Text(text = "Date de sortie : ${DateConverter.convertDateToSimpleString(article.date)}")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(checked = checkedFav, onCheckedChange = onCheckedChange)
            Text("Favoris ?")
        }

    }

}

@Composable
@Preview
fun ArticleDetailPreview() {
    ArticleDetail(
        article = Article(
            urlImage = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
            name = "Valise de fou",
            description = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut la",
            price = 123.38f
        ),
        checkedFav = false
    ) {

    }
}
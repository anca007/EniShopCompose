package com.example.enishopcompose.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.ui.common.FormRowSurface
import com.example.enishopcompose.ui.common.LoadingScreen
import com.example.enishopcompose.vm.ArticleViewModel

private const val TAG = "ArticleList"

@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    articleViewModel: ArticleViewModel = viewModel(
        factory = ArticleViewModel.Factory
    ),
    isArticlesFav: Boolean = false,
    onClickOnAddArticle: () -> Unit,
    onClickOnArticleItem: (Long) -> Unit
) {

    //LaunchedEffect ne lance que la première fois à moins de lui donner un élément à suivre
    //C'est key1 ici, quand key1 est modifié, LaunchedEffect est appelé
    LaunchedEffect(key1 = isArticlesFav, Unit) {
        //gestion de l'affichage de la liste qui provient de l'api ou de la BDD
        if (isArticlesFav) {
            articleViewModel.getAllArticlesFav()
        } else {
            articleViewModel.getAllArticles()
        }
    }

    val articleList by articleViewModel.articles
    val isLoading by articleViewModel.isLoading

    if (!isLoading) {
        LoadingScreen()
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ArticleList(
                articleList = articleList,
                onClickOnArticleItem = onClickOnArticleItem
            )
            FloatingActionButton(
                onClick = onClickOnAddArticle,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Image(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add article",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}


@Composable
fun ArticleList(
    articleList: List<Article>,
    onClickOnArticleItem: (Long) -> Unit
) {
    //val list = List(30) { i -> Article(initialName = "Article $i") }
    LazyColumn() {
        items(articleList) {
            ArticleItem(article = it, onClickOnArticleItem = onClickOnArticleItem)
        }
    }
}

@Composable
fun ArticleItem(article: Article = Article(), onClickOnArticleItem: (Long) -> Unit) {
    FormRowSurface(modifier = Modifier.clickable {
        onClickOnArticleItem(article.id)
    })
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlImage,
                contentDescription = article.urlImage,
                modifier = Modifier
                    .size(80.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                    .padding(8.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = article.name,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    //maxLines = 2,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

}

@Composable
@Preview
fun ArticleListPreview() {
//    ArticleItem(
////        Article(
////            urlImage = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
////            initialName = "Valise de fou"
////        )
////    )

//    ArticleListScreen(){
//
//    }
}
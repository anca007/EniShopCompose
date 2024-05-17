package com.example.enishopcompose.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.enishopcompose.bo.Article
import com.example.enishopcompose.ui.common.AddArticleFAB
import com.example.enishopcompose.ui.common.FormRowSurface
import com.example.enishopcompose.ui.common.LoadingScreen
import com.example.enishopcompose.ui.common.TopBar
import com.example.enishopcompose.vm.ArticleListViewModel

private const val TAG = "ArticleList"

@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    articleListViewModel: ArticleListViewModel = viewModel(
        factory = ArticleListViewModel.Factory
    ),
    isArticlesFav: Boolean = false,
    backgroundColor: Color,
    onClickOnArticleItem: (Long) -> Unit
) {

    //LaunchedEffect ne lance que la première fois à moins de lui donner un élément à suivre
    //C'est key1 ici, quand key1 est modifié, LaunchedEffect est appelé
//    LaunchedEffect(key1 = isArticlesFav, Unit) {
//        //gestion de l'affichage de la liste qui provient de l'api ou de la BDD
//        if (isArticlesFav) {
//            //articleViewModel.getAllArticlesFav()
//        } else {
//            articleListViewModel.initArticleList()
//        }
//    }

    val articles by articleListViewModel.articles.collectAsState()
    val isLoading by articleListViewModel.isLoading.collectAsState()
    val categories by articleListViewModel.categories.collectAsState()
    var selectedCategory by remember {
        mutableStateOf("")
    }

    val filteredArticles = if (selectedCategory != "") {
        articles.filter {
            it.category == selectedCategory
        }
    } else {
        articles
    }

    Scaffold(
        topBar = { TopBar(navController = navController) },
        floatingActionButton = { AddArticleFAB(navController = navController) },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = backgroundColor
    ) {

        if (!isLoading) {
            LoadingScreen()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column() {

                    CategoryFilterChip(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        articleListViewModel = articleListViewModel,
                        onCategoryClick = {
                            selectedCategory = it
                        }
                    )
                    ArticleList(
                        articleList = filteredArticles,
                        onClickOnArticleItem = onClickOnArticleItem
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterChip(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String,
    articleListViewModel: ArticleListViewModel,
    onCategoryClick: (String) -> Unit
) {
    LazyRow {
        items(categories) { category ->
            FilterChip(
                modifier = modifier.padding(4.dp),
                selected = category == selectedCategory,
                onClick = {
                    // articleListViewModel.setCategory(category)
                    if (category == selectedCategory) {
                        onCategoryClick("")
                    } else {
                        onCategoryClick(category)
                    }
                },
                label = {
                    Text(text = category.replaceFirstChar { it.uppercase() })
                },
                leadingIcon = if (category == selectedCategory) {
                    {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done icon"
                        )
                    }
                } else {
                    null
                }
            )
        }
    }

}


@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    articleList: List<Article>,
    onClickOnArticleItem: (Long) -> Unit
) {
    //val list = List(30) { i -> Article(initialName = "Article $i") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    //softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${article.price} €"
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
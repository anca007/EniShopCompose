package com.example.enishopcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.enishopcompose.repository.ArticleRepository
import com.example.enishopcompose.service.DataStoreManager
import com.example.enishopcompose.service.dataStore
import com.example.enishopcompose.ui.screen.ArticleDetailScreen
import com.example.enishopcompose.ui.screen.ArticleFormScreen
import com.example.enishopcompose.ui.screen.ArticleListScreen
import com.example.enishopcompose.ui.theme.EniShopComposeTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EniShopApp()
        }
    }
}

@Composable
fun EniShopApp(modifier: Modifier = Modifier) {

    EniShopComposeTheme() {

        val navController = rememberNavController()
        EniShopNavHost(navController = navController)
    }
}


@Composable
fun EniShopNavigationBar(navController: NavHostController) {

    var homeSelected by remember {
        mutableStateOf(true)
    }
    var favSelected by remember {
        mutableStateOf(false)
    }

    NavigationBar(modifier = Modifier.heightIn(max = 40.dp)) {
        NavigationBarItem(
            selected = homeSelected,
            onClick = {
                homeSelected = true
                favSelected = false
                navController.navigate(EniShopHome.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.findStartDestination().id)
                }
            },
            icon = {
                Icon(imageVector = EniShopHome.icon, contentDescription = null)
            }
        )
        NavigationBarItem(
            selected = favSelected,
            onClick = {
                favSelected = true
                homeSelected = false
                navController.navigate("${EniShopHome.route}?${EniShopHome.homeArg}=$favSelected") {
                    launchSingleTop = true
                    popUpTo(navController.graph.findStartDestination().id)
                }
            },
            icon = {
                Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
            }
        )
    }
}

@Composable
fun EniShopNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    var backgroundColor by remember { mutableStateOf(Color.White) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        DataStoreManager.getBackgroundColor(context).collect {
            backgroundColor = Color(it)
        }
    }


    NavHost(
        navController = navController,
        startDestination = EniShopHome.routeWithArgs,
        modifier = modifier
    ) {
        this.composable(
            route = EniShopHome.routeWithArgs,
            arguments = EniShopHome.arguments
        ) {
            //récupération du paramètre facultatif de la page d'accueil pour accéder aux favoris
            val fav = it.arguments?.getBoolean(EniShopHome.homeArg) ?: false

            ArticleListScreen(
                navController = navController,
                onClickOnArticleItem = {
                    navController.navigate("${EniShopDetail.route}/$it")
                },
                isArticlesFav = fav,
                backgroundColor = backgroundColor
            )
        }
        this.composable(EniShopAdd.route) {
            ArticleFormScreen(
                onClickOnSave = {
                    navController.navigate(EniShopHome.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                },
                navController = navController,
                backgroundColor = backgroundColor
            )
        }
        this.composable(
            route = EniShopDetail.routeWithArgs,
            arguments = EniShopDetail.arguments
        ) {
            val articleId = it.arguments?.getInt(EniShopDetail.articleDetailArg) ?: 0
            ArticleDetailScreen(articleId = articleId.toLong(), navController = navController,  backgroundColor = backgroundColor)
        }
    }
}

@Preview
@Composable
fun AppBarPreview() {
    EniShopApp()
}



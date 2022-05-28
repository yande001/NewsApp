package com.example.darren.newsapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.darren.newsapp.MockData
import com.example.darren.newsapp.MockData.getTimeAgo
import com.example.darren.newsapp.NewsData
import com.example.darren.newsapp.R
import com.example.darren.newsapp.models.TopNewsArticle
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun DetailScreen(navController: NavController, article: TopNewsArticle, scrollState: ScrollState){
    Scaffold(topBar = {
        DetailTopBarApp(onBackPressed = {navController.popBackStack()})
    })

    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = "Detail Screen", fontWeight = FontWeight.SemiBold)
            CoilImage(
                imageModel = article.urlToImage,
                contentScale = ContentScale.Crop,
                error = ImageBitmap.imageResource(id = R.drawable.breaking_news),
                placeHolder = ImageBitmap.imageResource(id = R.drawable.breaking_news)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoWithIcon(icon = Icons.Default.Edit, info = article.author?:"Not Available")
                InfoWithIcon(icon = Icons.Default.DateRange,
                    info = MockData.stringToDate(article.publishedAt!!).getTimeAgo())
            }
            Text(text = article.title?:"Not Available", fontWeight = FontWeight.Bold)
            Text(text = article.description?:"Not Available", modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun DetailTopBarApp(onBackPressed: () -> Unit){
    TopAppBar(
        title = { Text(text ="Detail Screen")},
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            }
        }

    )
        

}



@Composable
fun InfoWithIcon(icon: ImageVector, info: String){
    Row() {
        Icon(imageVector = icon,
            contentDescription = "",
            modifier = Modifier.padding(end = 8.dp),
            colorResource(id = R.color.purple_500)
            )
        Text(text = info)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview(){
    DetailScreen(navController = rememberNavController(),
        TopNewsArticle(
            author = "Namita Singh",
            title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
            description = "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
            publishedAt = "2021-11-07T04:42:40Z"
        ),
        rememberScrollState(),
        )
}
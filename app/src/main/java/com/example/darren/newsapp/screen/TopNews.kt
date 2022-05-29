package com.example.darren.newsapp.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.darren.newsapp.MainViewModel
import com.example.darren.newsapp.MockData
import com.example.darren.newsapp.MockData.getTimeAgo
import com.example.darren.newsapp.NewsData
import com.example.darren.newsapp.R
import com.example.darren.newsapp.component.ErrorUI
import com.example.darren.newsapp.component.LoadingUI
import com.example.darren.newsapp.component.SearchBar
import com.example.darren.newsapp.models.TopNewsArticle
import com.example.darren.newsapp.network.NewsManager
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun TopNews(navController: NavController,
            articles: List<TopNewsArticle>,
            query: MutableState<String>,
            viewModel: MainViewModel,
            isError: MutableState<Boolean>,
            isLoading: MutableState<Boolean>
            ){
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        SearchBar(query = query, viewModel = viewModel)
        val searchedText = query.value
        val resultList = mutableListOf<TopNewsArticle>()
        if (searchedText != ""){
            resultList.addAll(viewModel.getArticleByQuery.collectAsState().value.articles?: articles)
            Log.e("TEST","1")
        } else{
            resultList.addAll(articles)
            Log.e("TEST","2, ${resultList.size}")
        }

        when{
            isLoading.value ->{
                LoadingUI()
            }
            isError.value ->{
                ErrorUI()
            } else ->{
                LazyColumn{
                    items(resultList.size){
                            index ->
                        TopNewsItems(
                            article = resultList[index],
                            onNewsClick = {
                                navController.navigate("Detail/$index")
                            }
                        )
                    }
                }
            }
        }


    }
}

@Composable
fun TopNewsItems(article: TopNewsArticle, onNewsClick:() -> Unit ={} ){
    Box(modifier = Modifier
        .height(200.dp)
        .padding(8.dp)
        .clickable {
            onNewsClick()
        }
    ) {
        CoilImage(
            imageModel = article.urlToImage,
            contentScale = ContentScale.Crop,
            error = ImageBitmap.imageResource(id = R.drawable.breaking_news),
            placeHolder = ImageBitmap.imageResource(id = R.drawable.breaking_news)

        )
        Column(modifier = Modifier
            .wrapContentHeight()
            .padding(top = 16.dp, start = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            article.publishedAt?.let {
                Text(
                    text = MockData.stringToDate(it).getTimeAgo(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(80.dp))

            article.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }


        }
    }

}




@Preview
@Composable
fun TopNewsPreview(){
    TopNewsItems(
        TopNewsArticle(
        author = "Raja Razek, CNN",
        title = "'Tiger King' Joe Exotic says he has been diagnosed with aggressive form of prostate cancer - CNN",
        description = "Joseph Maldonado, known as Joe Exotic on the 2020 Netflix docuseries \\\"Tiger King: Murder, Mayhem and Madness,\\\" has been diagnosed with an aggressive form of prostate cancer, according to a letter written by Maldonado.",
        publishedAt = "2021-11-04T05:35:21Z"
    )
    )
}
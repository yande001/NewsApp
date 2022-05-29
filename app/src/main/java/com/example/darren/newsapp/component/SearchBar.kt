package com.example.darren.newsapp.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.darren.newsapp.MainViewModel
import com.example.darren.newsapp.network.Api
import com.example.darren.newsapp.network.NewsManager

@Composable
fun SearchBar(query: MutableState<String>, viewModel: MainViewModel){
    val localFocusManager = LocalFocusManager.current
    Card(elevation = 6.dp,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.primary
    ) {
        TextField(value = query.value,
            onValueChange = {
            query.value = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Search", color = Color.White)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "", tint = Color.White)
            },
            trailingIcon = {
                if(query.value != ""){
                    IconButton(onClick = {query.value = ""}) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "", tint = Color.White)
                    }
                }
            },
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if(query.value != ""){
                        viewModel.getArticlesByQuery(query.value)
                    }
                    localFocusManager.clearFocus()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
        )

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun SearchBarPreview(){
    SearchBar(query = mutableStateOf(""), viewModel())
}
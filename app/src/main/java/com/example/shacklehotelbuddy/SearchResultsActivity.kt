package com.example.shacklehotelbuddy

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shacklehotelbuddy.ui.theme.ShackleHotelBuddyTheme

class SearchResultsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShackleHotelBuddyTheme {
                SearchResultsMainScreen()
            }
        }
    }


    data class SearchResult(
        val hotelName: String,
        val hotelCity: String,
        val hotelPrice: String,
        val hotelRating: String,
        val hotelImage: String
    )
}

@Composable
fun SearchResultsMainScreen() {
    Column {
        Header()
        SearchResults(
            listOf(
                SearchResultsActivity.SearchResult(
                    "Hotel Name",
                    "Hotel City",
                    "Hotel Price",
                    "Hotel Rating",
                    "Hotel Image"
                )
            )
        )
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = {
            }
        ) {
            Image(painter = painterResource(R.drawable.arrow_back), contentDescription = "")
        }
        Text(
            text = "Search Results",
            color = Color.Black,
            modifier = Modifier
                .padding(end = 16.dp)
                .background(colorResource(id = R.color.teal))
        )
    }
}

@Composable
fun SearchResults(searchResults: List<SearchResultsActivity.SearchResult>) {
    LazyColumn() {
        items(searchResults.size) { index ->
            SearchResult(searchResults[index])
        }
    }
}


@Composable
private fun SearchResult(searchResult: SearchResultsActivity.SearchResult) {
    Column {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "",
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .height(200.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = searchResult.hotelName)
            Text(text = searchResult.hotelRating)
        }
        Text(
            text = searchResult.hotelCity, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Text(
            text = searchResult.hotelPrice, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    ShackleHotelBuddyTheme {
        SearchResultsMainScreen()
    }
}
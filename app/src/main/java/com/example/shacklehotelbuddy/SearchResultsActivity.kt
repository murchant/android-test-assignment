package com.example.shacklehotelbuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shacklehotelbuddy.data.Respository
import com.example.shacklehotelbuddy.ui.theme.ShackleHotelBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsActivity : ComponentActivity() {

    private val viewModel: SearchResultsViewModel by viewModels()
    private val checkInDate: String by lazy {
        intent.getStringExtra(CHECK_IN_DATE) ?: ""
    }
    private val checkOutDate: String by lazy {
        intent.getStringExtra(CHECK_OUT_DATE) ?: ""
    }
    private val adults: Int by lazy {
        intent.getIntExtra(ADULTS, 0)
    }
    private val children: Int by lazy {
        intent.getIntExtra(CHILDREN, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.bind(checkInDate, checkOutDate, adults, children)
        setContent {
            ShackleHotelBuddyTheme {
                val searchResults = viewModel.viewSate.observeAsState()
                when (searchResults.value) {
                    is SearchResultsViewModel.ViewState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            IndeterminateCircularIndicator()
                        }
                    }
                    is SearchResultsViewModel.ViewState.Empty -> {
                        Header(::navigateBack)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                color = ShackleHotelBuddyTheme.colors.grayText,
                                fontWeight = FontWeight.Medium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontSize = TextUnit(22F, TextUnitType.Sp),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 16.dp),
                                text = "No results found, try again please!"
                            )
                        }
                    }
                    is SearchResultsViewModel.ViewState.Success -> {
                        SearchResultsMainScreen(
                            (searchResults.value as SearchResultsViewModel.ViewState.Success).searchResults,
                            ::navigateBack
                        )
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                        ) {
                            Header(::navigateBack)
                            Text(text = "Error")
                        }
                    }
                }
            }
        }
    }

    private fun navigateBack() {
        finish()
    }

    companion object {
        private const val TAG = "SearchResultsActivity"
        const val CHECK_IN_DATE = "checkInDate"
        const val CHECK_OUT_DATE = "checkOutDate"
        const val ADULTS = "adults"
        const val CHILDREN = "children"

        fun newIntent(
            context: Context,
            checkInDate: String,
            checkOutDate: String,
            adults: Int,
            children: Int
        ): Intent {
            val intent = Intent(context, SearchResultsActivity::class.java)
            intent.putExtra(CHECK_IN_DATE, checkInDate)
            intent.putExtra(CHECK_OUT_DATE, checkOutDate)
            intent.putExtra(ADULTS, adults)
            intent.putExtra(CHILDREN, children)
            return intent
        }
    }
}

@Composable
fun SearchResultsMainScreen(
    hotelSearchResults: List<Respository.SearchResultWithDetails?>,
    backButtonEvent: () -> Unit
) {
    Column {
        Header(backButtonEvent)
        if (hotelSearchResults.isEmpty()) {
            IndeterminateCircularIndicator()
        }
        LazyColumn {
            items(hotelSearchResults.size) { index ->
                SearchResult(hotelSearchResults[index])
            }
        }
    }
}


@Composable
private fun Header(backButtonEvent: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .padding(end = 16.dp),
            onClick = {
                backButtonEvent()
            }
        ) {
            Image(painter = painterResource(R.drawable.arrow_back), contentDescription = "")
        }
        Text(
            text = "Search Results",
            color = Color.Black,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = TextUnit(20F, TextUnitType.Sp),
            modifier = Modifier
                .padding(end = 16.dp)
        )
    }
}

@Composable
private fun SearchResult(searchResult: Respository.SearchResultWithDetails?) {
    searchResult?.let {
        Column {
            Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)) {
                AsyncImage(
                    model = searchResult.hotelImage,
                    contentDescription = "",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxSize()
                        .height(200.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = searchResult.hotelName,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                StarAndRating(searchResult)
            }
            Text(
                text = searchResult.hotelCity, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = ShackleHotelBuddyTheme.colors.grayText
            )
            Text(
                text = "${searchResult.price} night", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StarAndRating(searchResult: Respository.SearchResultWithDetails) {
    Row {
        Icon(
            painterResource(id = R.drawable.star),
            contentDescription = ""
        )
        Text(text = searchResult.hotelRating)
    }
}

@Composable
fun IndeterminateCircularIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .padding(bottom = 32.dp),
            color = ShackleHotelBuddyTheme.colors.teal,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Text(
            color = ShackleHotelBuddyTheme.colors.grayText,
            text = "Please wait while we retrieve your results.."
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    ShackleHotelBuddyTheme {
//        SearchResultsMainScreen()
    }
}
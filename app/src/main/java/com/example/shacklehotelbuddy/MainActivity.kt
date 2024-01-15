package com.example.shacklehotelbuddy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.shacklehotelbuddy.ui.theme.ShackleHotelBuddyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShackleHotelBuddyTheme {
                MainScreen()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.background),
                    contentScale = ContentScale.FillWidth
                ),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = "Select guests, date and time",
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    fontSize = TextUnit(38F, TextUnitType.Sp),
                    color = Color.White
                )
                Box(modifier = Modifier.padding(16.dp)) {
                    Column {
                        SearchParameters()
                    }
                }
                Text(
                    text = "Recent searches",
                    modifier = Modifier.padding(
                        top = 20.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    ),
                    fontSize = TextUnit(18F, TextUnitType.Sp),
                    color = Color.White
                )
                RecentSearches(
                    states = listOf(
                        RecentSearchState(
                            imageRes = R.drawable.manage_history,
                            title = "Recent Searches",
                            value = SearchParameterState(
                                imageRes = R.drawable.event_available,
                                checkInDate = "DD/MM/YYYY",
                                checkOutDate = "DD/MM/YYYY",
                                adults = 1,
                                children = 0,
                                value = "1 Room, 1 Adult"
                            )
                        )
                    )
                )

                Button(
                    onClick = {
                        navigateToSearchResults()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ShackleHotelBuddyTheme.colors.teal,
                        contentColor = ShackleHotelBuddyTheme.colors.white
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Search")
                }
            }
        }
    }

    fun navigateToSearchResults() {
        val currentState = viewModel.searchParameters.value
        viewModel.saveSearchParameters(currentState)
        startActivity(
            SearchResultsActivity.newIntent(
                this,
                currentState?.checkInDate ?: "",
                currentState?.checkOutDate ?: "",
                currentState?.adult ?: 0,
                currentState?.children ?: 0
            )
        )
    }

    @Composable
    fun RecentSearch(state: RecentSearchState) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.manage_history),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 8.dp)
            )
            Text(
                text = state.title,
                style = ShackleHotelBuddyTheme.typography.bodyMedium,
                color = ShackleHotelBuddyTheme.colors.grayText,
                textAlign = TextAlign.Start,
                modifier = Modifier
            )
        }
    }

    @Composable
    fun RecentSearches(states: List<RecentSearchState>) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                .background(ShackleHotelBuddyTheme.colors.white)
        ) {
            states.forEach {
                item {
                    RecentSearch(
                        state = RecentSearchState(
                            imageRes = R.drawable.manage_history,
                            title = it.title,
                            value = it.value
                        )
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun SearchParameters() {
        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = ShackleHotelBuddyTheme.colors.white)
        ) {
            item {
                SearchDateParameter(
                    state = RowState(
                        imageRes = R.drawable.event_available,
                        title = "Check-in-date"
                    ),
                    action = ::updateCheckInDate
                )
                SearchDateParameter(
                    state = RowState(
                        imageRes = R.drawable.event_available,
                        title = "Check-out-date"
                    ),
                    action = ::updateCheckoutDate
                )
                SearchTextParameter(
                    state = RowState(
                        imageRes = R.drawable.event_available,
                        title = "Adult"
                    ),
                    action = ::updateAdultValue
                )
                SearchTextParameter(
                    state = RowState(
                        imageRes = R.drawable.event_available,
                        title = "Children"
                    ),
                    action = ::updateChildrenValue
                )
            }
        }
    }

    private fun updateAdultValue(value: Int) {
        viewModel.updateAdultValue(value)
    }

    private fun updateChildrenValue(value: Int) {
        viewModel.updateChildrenValue(value)
    }

    private fun updateCheckoutDate(date: String) {
        viewModel.updateCheckInDate(date)
    }

    private fun updateCheckInDate(date: String) {
        viewModel.updateCheckOutDate(date)
    }

    data class SearchParameterState(
        val imageRes: Int,
        val checkInDate: String,
        val checkOutDate: String,
        val adults: Int,
        val children: Int,
        val value: String
    )

    data class RowState(
        val imageRes: Int,
        val title: String,
    )

    data class RecentSearchState(
        val imageRes: Int,
        val title: String,
        val value: SearchParameterState
    )

    @Composable
    private fun SearchTextParameter(state: RowState, action: (value: Int) -> Unit = {}) {
        var value by remember { mutableStateOf("") }
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(id = state.imageRes),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = state.title,
                        style = ShackleHotelBuddyTheme.typography.bodyMedium,
                        color = ShackleHotelBuddyTheme.colors.grayText,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .weight(1f)
            ) {
                TextField(
                    value = value,
                    modifier = Modifier
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        value = it
                        action(it.toInt())
                    },
                    textStyle = ShackleHotelBuddyTheme.typography.bodyMedium,
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun SearchDateParameter(state: RowState, action: (date: String) -> Unit = {}) {
        var value by remember { mutableStateOf("") }
        var isDatePickerVisible by remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(id = state.imageRes),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = state.title,
                        style = ShackleHotelBuddyTheme.typography.bodyMedium,
                        color = ShackleHotelBuddyTheme.colors.grayText,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                    .background(ShackleHotelBuddyTheme.colors.white)
                    .clickable {
                        isDatePickerVisible = !isDatePickerVisible
                    }
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = value,
                    style = ShackleHotelBuddyTheme.typography.bodyMedium,
                    color = ShackleHotelBuddyTheme.colors.grayText,
                    textAlign = TextAlign.Start
                )
            }
            if (isDatePickerVisible) {
                SimpleDatePickerInDatePickerDialog(
                    openDialog = isDatePickerVisible,
                    onDismiss = { isDatePickerVisible = false },
                    onConfirm = { date ->
                        value = Instant.ofEpochMilli(date).toString()
                        isDatePickerVisible = false
                        action(value)
                    }
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun SimpleDatePickerInDatePickerDialog(
        openDialog: Boolean,
        onDismiss: () -> Unit,
        onConfirm: (date: Long) -> Unit
    ) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        DatePickerDialog(
            shape = RoundedCornerShape(6.dp),
            onDismissRequest = onDismiss,
            confirmButton = {
                // Seems broken at the moment with DateRangePicker
                // Works fine with DatePicker
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let(onConfirm)
                }) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Dismiss")
                }
            },
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ShackleHotelBuddyTheme {
            MainScreen()
        }
    }
}
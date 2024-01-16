package com.example.shacklehotelbuddy

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.shacklehotelbuddy.data.SearchParameter
import com.example.shacklehotelbuddy.ui.theme.ShackleHotelBuddyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.bind()
        setContent {
            ShackleHotelBuddyTheme {
                val resents = viewModel.recentSearches.observeAsState()
                MainScreen(
                    resents.value ?: emptyList(),
                    navigateToSearchResults = ::navigateToSearchResults,
                    formatDate = ::formatDate,
                    updateAdultValue = ::updateAdultValue,
                    updateChildrenValue = ::updateChildrenValue,
                    updateCheckInDate = ::updateCheckInDate,
                    updateCheckOutDate = ::updateCheckOutDate,
                    formatToDisplayValue = ::formatToDisplayValue
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.bind()
    }

    private fun formatToDisplayValue(value: String): String {
        return if (value != "") {
            viewModel.formatDate(value)
        } else {
            ""
        }
    }

    private fun formatDate(value: String): String {
        return viewModel.formatDate(value)
    }

    private fun updateAdultValue(value: Int) {
        viewModel.updateAdultValue(value)
    }

    private fun updateChildrenValue(value: Int) {
        viewModel.updateChildrenValue(value)
    }

    private fun updateCheckOutDate(date: String) {
        viewModel.updateCheckInDate(date)
    }

    private fun updateCheckInDate(date: String) {
        viewModel.updateCheckOutDate(date)
    }


    private fun navigateToSearchResults() {
        val currentState = viewModel.searchParameters.value
        currentState?.let {
            if (it.adult != 0 && it.children != 0 && it.checkInDate != "" && it.checkOutDate != "") {
                viewModel.saveSearchParameters(currentState)
                startActivity(
                    SearchResultsActivity.newIntent(
                        this,
                        it.checkInDate,
                        it.checkOutDate,
                        it.adult,
                        it.children
                    )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    recents: List<SearchParameter>,
    navigateToSearchResults: () -> Unit,
    formatDate: (value: String) -> String,
    updateAdultValue: (value: Int) -> Unit,
    updateChildrenValue: (value: Int) -> Unit,
    updateCheckInDate: (value: String) -> Unit,
    updateCheckOutDate: (value: String) -> Unit,
    formatToDisplayValue: (value: String) -> String
) {
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
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                fontSize = TextUnit(40F, TextUnitType.Sp),
                color = Color.White
            )
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    SearchParameters(
                        updateAdultValue = updateAdultValue,
                        updateChildrenValue = updateChildrenValue,
                        updateCheckInDate = updateCheckInDate,
                        updateCheckOutDate = updateCheckOutDate,
                        formatToDisplayValue = formatToDisplayValue
                    )
                }
            }
            if (recents.isNotEmpty()) {
                Text(
                    text = "Recent searches",
                    modifier = Modifier.padding(
                        top = 20.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 0.dp
                    ),
                    fontSize = TextUnit(22F, TextUnitType.Sp),
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                )
                val recentAsState = recents.map {
                    RecentSearchState(
                        imageRes = R.drawable.manage_history,
                        title = "${formatDate(it.checkInDate)} - ${formatDate(it.checkOutDate)}  ${it.adult} Adults, ${it.children} Children",
                        value = it
                    )
                }
                RecentSearches(
                    states = recentAsState
                )
            }
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
                    .height(60.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Search",
                    fontSize = TextUnit(18F, TextUnitType.Sp),
                )
            }
        }
    }
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
            fontSize = TextUnit(13F, TextUnitType.Sp),
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
private fun SearchParameters(
    updateCheckInDate: (value: String) -> Unit,
    updateCheckOutDate: (value: String) -> Unit,
    updateAdultValue: (value: Int) -> Unit,
    updateChildrenValue: (value: Int) -> Unit,
    formatToDisplayValue: (value: String) -> String
) {
    LazyColumn(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = ShackleHotelBuddyTheme.colors.white)
    ) {
        item {
            SearchDateParameter(
                state = RowState(
                    imageRes = R.drawable.event_upcoming,
                    title = "Check-in-date"
                ),
                action = updateCheckInDate,
                formatToDisplayValue = formatToDisplayValue
            )
            SearchDateParameter(
                state = RowState(
                    imageRes = R.drawable.event_available,
                    title = "Check-out-date"
                ),
                action = updateCheckOutDate,
                formatToDisplayValue = formatToDisplayValue
            )
            SearchTextParameter(
                state = RowState(
                    imageRes = R.drawable.person,
                    title = "Adult"
                ),
                action = updateAdultValue
            )
            SearchTextParameter(
                state = RowState(
                    imageRes = R.drawable.supervisor_account,
                    title = "Children"
                ),
                action = updateChildrenValue
            )
        }
    }
}

@Composable
private fun SearchTextParameter(state: RowState, action: (value: Int) -> Unit = {}) {
    var value by remember { mutableStateOf("") }
    Row(
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TitleCell(
            state = state, modifier = Modifier
                .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                .background(ShackleHotelBuddyTheme.colors.white)
                .height(50.dp)
                .weight(1F)
                .padding(16.dp)
        )
        Box(
            modifier = Modifier
                .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                .height(50.dp)
                .background(ShackleHotelBuddyTheme.colors.white)
                .weight(1f)
        ) {
            TextField(
                value = value,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                    if (it != "") {
                        action(it.toInt())
                    }
                },
                textStyle = ShackleHotelBuddyTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun TitleCell(state: RowState, modifier: Modifier) {
    Box(
        modifier = modifier
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun SearchDateParameter(
    state: RowState,
    action: (date: String) -> Unit = {},
    formatToDisplayValue: (value: String) -> String
) {
    var value by remember { mutableStateOf("") }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        TitleCell(
            state = state, modifier = Modifier
                .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                .background(ShackleHotelBuddyTheme.colors.white)
                .height(50.dp)
                .weight(1F)
                .padding(16.dp)
        )
        Box(
            modifier = Modifier
                .border(width = 0.5.dp, color = ShackleHotelBuddyTheme.colors.grayBorder)
                .background(ShackleHotelBuddyTheme.colors.white)
                .height(50.dp)
                .clickable {
                    isDatePickerVisible = !isDatePickerVisible
                }
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = formatToDisplayValue(value),
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
//            MainScreen(recents.value ?: emptyList())
    }
}
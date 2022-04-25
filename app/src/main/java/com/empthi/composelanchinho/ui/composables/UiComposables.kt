@file:OptIn(
    ExperimentalUnitApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)

package com.empthi.composelanchinho.ui.composables

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.empthi.composelanchinho.R
import com.empthi.composelanchinho.domain.entities.FoodUI
import com.empthi.composelanchinho.domain.entities.Order

@Composable
fun FoodCardsGrid(list: List<FoodUI>, onItemSelected: (item: FoodUI) -> Unit) {
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(list) { food ->
            FoodCard(name = food.name, imageUrl = food.uri, onItemSelected = {
                onItemSelected(food)
            })
        }
    }
}

@Composable
fun OrdersTracker(
    modifier: Modifier = Modifier,
    clientOrders: List<Order>,
    showOrders: Boolean = false,
    onShowOrderClick: () -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            modifier = modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color.Gray)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Column {
                IconButton(onClick = {
                    onShowOrderClick()
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.my_orders),
                            textAlign = TextAlign.Center,
                        )
                        val icon =
                            if (showOrders) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp
                        val desc =
                            if (showOrders) stringResource(R.string.collapse_menu) else stringResource(
                                R.string.expand_menu
                            )
                        Icon(icon, desc)
                    }
                }
                if (showOrders) {
                    OrdersList(orders = clientOrders)
                }
            }
        }
    }
}

@Composable
fun OrdersList(orders: List<Order>, modifier: Modifier = Modifier) {
    LazyRow {
        items(orders) { order ->
            Surface(
                modifier = modifier
                    .wrapContentSize()
                    .sizeIn(maxWidth = 180.dp)
            ) {
                Column {
                    FoodCard(
                        name = order.food.name,
                        imageUrl = order.food.uri,
                        onItemSelected = { })
                    LinearProgressIndicator(
                        modifier = modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.CenterHorizontally),
                        progress = order.waitingTime
                    )

                }
            }
        }
    }
}

@Composable
fun FoodCard(
    name: String,
    imageUrl: String,
    onItemSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 8.dp,
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp),
        onClick = onItemSelected
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = name,
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = name,
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(start = 8.dp),
                fontSize = TextUnit(value = 18f, TextUnitType.Sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}


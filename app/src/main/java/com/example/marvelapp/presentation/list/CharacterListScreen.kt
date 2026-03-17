
package com.example.marvelapp.presentation.list

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.marvelapp.presentation.viewmodel.CharacterListViewModel
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(onClick: (Int) -> Unit) {
    val vm: CharacterListViewModel = viewModel()
    val characters by vm.characters.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "MARVEL",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            letterSpacing = (-1).sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            items(characters) { character ->
                CharacterItem(character = character, onClick = { onClick(character.id) })
            }
            
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        MarvelLoadingAnimation()
                    }
                }
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (index != null && index >= characters.size - 1) {
                    vm.loadMore()
                }
            }
    }
}

@Composable
fun MarvelLoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = Modifier.size(40.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2
        val strokeWidth = 4.dp.toPx()
        val numLines = 12
        val angleStep = 360f / numLines

        for (i in 0 until numLines) {
            val angle = i * angleStep + rotation
            val angleRad = Math.toRadians(angle.toDouble())
            
            val start = Offset(
                x = center.x + (radius * 0.5f * cos(angleRad)).toFloat(),
                y = center.y + (radius * 0.5f * sin(angleRad)).toFloat()
            )
            val end = Offset(
                x = center.x + (radius * cos(angleRad)).toFloat(),
                y = center.y + (radius * sin(angleRad)).toFloat()
            )

            val alpha = (i.toFloat() / numLines)
            drawOutlineLine(start, end, Color.Gray.copy(alpha = alpha), strokeWidth)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawOutlineLine(
    start: Offset,
    end: Offset,
    color: Color,
    strokeWidth: Float
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

@Composable
fun CharacterItem(character: com.example.marvelapp.domain.model.Character, onClick: () -> Unit) {
    val painter = rememberAsyncImagePainter(model = character.imageUrl)
    val painterState = painter.state

    if (painterState !is AsyncImagePainter.State.Error) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onClick() }
        ) {
            androidx.compose.foundation.Image(
                painter = painter,
                contentDescription = character.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            val labelShape = GenericShape { size, _ ->
                val slant = 40f
                moveTo(slant, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width - slant, size.height)
                lineTo(0f, size.height)
                close()
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 24.dp),
                shape = labelShape,
                color = Color.White
            ) {
                Text(
                    text = character.name.uppercase(),
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

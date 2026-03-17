
package com.example.marvelapp.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.marvelapp.domain.model.Character
import com.example.marvelapp.domain.model.ResourceInfo
import com.example.marvelapp.presentation.viewmodel.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(characterId: Int, onBack: () -> Unit) {
    val vm: CharacterDetailViewModel = viewModel()
    val character by vm.character.collectAsState()
    var selectedResource by remember { mutableStateOf<ResourceInfo?>(null) }

    LaunchedEffect(characterId) {
        vm.getCharacter(characterId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { paddingValues ->
        character?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Image
                Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                    AsyncImage(
                        model = data.imageUrl,
                        contentDescription = data.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    DetailSectionTitle("NAME")
                    Text(
                        text = data.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (data.description.isNotEmpty()) {
                        DetailSectionTitle("DESCRIPTION")
                        Text(
                            text = data.description,
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    HorizontalResourceSection("COMICS", data.comics) { selectedResource = it }
                    HorizontalResourceSection("SERIES", data.series) { selectedResource = it }
                    HorizontalResourceSection("STORIES", data.stories) { selectedResource = it }
                    HorizontalResourceSection("EVENTS", data.events) { selectedResource = it }

                    if (data.urls.isNotEmpty()) {
                        DetailSectionTitle("RELATED LINKS")
                        data.urls.forEach { urlInfo ->
                            RelatedLinkItem(urlInfo.type)
                        }
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Red)
        }
    }

    selectedResource?.let { resource ->
        ResourceDetailDialog(resource = resource, onDismiss = { selectedResource = null })
    }
}

@Composable
fun ResourceDetailDialog(resource: ResourceInfo, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable { onDismiss() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.Center)
                    .background(Color.DarkGray, RoundedCornerShape(8.dp))
                    .clickable(enabled = false) {} // Prevent dismiss when clicking the content
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = resource.imageUrl,
                        contentDescription = resource.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp),
                        contentScale = ContentScale.FillBounds
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
                
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = resource.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DetailSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.Red,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun HorizontalResourceSection(title: String, items: List<ResourceInfo>, onItemClick: (ResourceInfo) -> Unit) {
    if (items.isEmpty()) return

    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        DetailSectionTitle(title)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(items) { item ->
                ResourceItemView(item, onItemClick)
            }
        }
    }
}

@Composable
fun ResourceItemView(item: ResourceInfo, onClick: (ResourceInfo) -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick(item) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            modifier = Modifier.height(180.dp).fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = item.name,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RelatedLinkItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle link */ }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.replaceFirstChar { it.uppercase() },
            color = Color.White,
            fontSize = 18.sp
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White
        )
    }
}

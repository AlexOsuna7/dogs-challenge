package com.example.dogschallenge.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.dogschallenge.domain.model.Dog
import com.example.dogschallenge.presentation.theme.DarkGrey
import com.example.dogschallenge.presentation.theme.LightGrey
import com.example.dogschallenge.presentation.theme.MediumGrey
import com.example.dogschallenge.presentation.theme.ShimmerBase
import com.example.dogschallenge.presentation.theme.ShimmerHighlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(
    viewModel: DogListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val loadedItems by viewModel.loadedItems.collectAsState()


    Spacer(modifier = Modifier.height(16.dp))

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dogs We Love", color = DarkGrey, fontSize = 26.sp)},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightGrey
                )
            )
        },
        containerColor = LightGrey
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val currentState = state) {
                is DogListState.Loading -> {
                    LoadingState()
                }
                is DogListState.Success -> {
                    if (currentState.dogs.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No dogs found.", color = MediumGrey)
                        }
                    } else {
                        DogList(
                            dogs = currentState.dogs,
                            loadedItems = loadedItems,
                            onItemImageLoaded = viewModel::onItemImageLoaded
                        )
                    }
                }
                is DogListState.Error -> {
                    ErrorState(
                        message = currentState.message,
                        onRetry = viewModel::onRetry
                    )
                }
            }
        }
    }
}

@Composable
fun DogList(
    dogs: List<Dog>,
    loadedItems: Set<String>,
    onItemImageLoaded: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        itemsIndexed(dogs, key = { index, dog -> dog.dogName + index }) { _, dog ->
            val isLoaded = loadedItems.contains(dog.dogName + dog.age)
            DogCard(
                dog = dog,
                isLoaded = isLoaded,
                onItemImageLoaded = { onItemImageLoaded(dog.dogName + dog.age) }
            )
        }
    }
}

@Composable
fun DogCard(
    dog: Dog,
    isLoaded: Boolean,
    onItemImageLoaded: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (isLoaded) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    val painter = rememberAsyncImagePainter(
        model = dog.imageUrl,
        onState = { state ->
            if (state is AsyncImagePainter.State.Success && !isLoaded) {
                onItemImageLoaded()
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        if (!isLoaded) {
            ShimmerItem(brush = shimmerBrush())
        }
        // ConstraintLayout is used here to create the overlapping effect
        // between the image and the text card.
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
        ) {
            val (image, textCard) = createRefs()

            Card(
                modifier = Modifier.constrainAs(textCard) {
                    start.linkTo(parent.start, margin = 40.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(155.dp)
                },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 110.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                ) {
                    var descriptionLineCount by remember { mutableStateOf(0) }
                    Text(text = dog.dogName, color = DarkGrey, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = dog.description,
                        color = MediumGrey,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Justify,
                        onTextLayout = { textLayoutResult ->
                            descriptionLineCount = textLayoutResult.lineCount
                        }
                    )
                    val spacerHeight = if (descriptionLineCount > 2) 6.dp else 22.dp
                    Spacer(modifier = Modifier.height(spacerHeight))
                    Text(
                        text = "Almost ${dog.age} years",
                        color = DarkGrey,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Image(
                painter = painter,
                contentDescription = dog.dogName,
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
                    .width(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .zIndex(1f),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun LoadingState() {
    val brush = shimmerBrush()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        userScrollEnabled = false
    ) {
        items(5) {
            ShimmerItem(brush = brush)
        }
    }
}

@Composable
fun ShimmerItem(brush: Brush) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        val (image, textCard) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(textCard) {
                    start.linkTo(parent.start, margin = 40.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(155.dp)
                }
                .background(brush, shape = RoundedCornerShape(12.dp))
        )
        Box(
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .width(130.dp)
                .background(brush, shape = RoundedCornerShape(16.dp))
        )
    }
}

/**
 * Creates a Brush for the shimmer effect animation.
 * @return A Brush that can be used as a background.
 */
@Composable
fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        ShimmerBase,
        ShimmerHighlight,
        ShimmerBase
    )
    val transition = rememberInfiniteTransition(label = "shimmer-transition")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer-animation"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation.value - 500f, 0f),
        end = Offset(translateAnimation.value, 0f)
    )
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.SentimentVeryDissatisfied,
                contentDescription = "Error Icon",
                modifier = Modifier.size(64.dp),
                tint = MediumGrey
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Ups! Something went wrong",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = DarkGrey
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = MediumGrey,
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
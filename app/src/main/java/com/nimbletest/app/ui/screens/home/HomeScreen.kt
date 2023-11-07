package com.nimbletest.app.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.nimbletest.app.ui.screens.home.HomeScreenViewModel.HomeScreenData
import com.nimbletest.app.ui.screens.home.HomeScreenViewModel.HomeScreenEvent
import com.nimbletest.app.ui.screens.home.HomeScreenViewModel.HomeScreenState
import com.nimbletest.app.R
import com.nimbletest.app.data.database.entities.SurveyEntity
import com.nimbletest.app.ui.components.BottomLoaderShimmer
import com.nimbletest.app.ui.components.CarouselDots
import com.nimbletest.app.ui.components.ScreenWithMessage
import com.nimbletest.app.ui.components.TopLoaderShimmer
import com.nimbletest.app.ui.theme.LocalNimbleColors
import com.nimbletest.app.util.toNamedDateFormat
import com.nimbletest.app.util.toTimeAgo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeScreenData: HomeScreenData,
    homeScreenState: HomeScreenState,
    surveys: List<SurveyEntity> = emptyList(),
    onEvent: (HomeScreenEvent) -> Unit = {},
    onSurveyButtonPressed: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        surveys.size
    }
    //
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = homeScreenState == HomeScreenState.Loading,
        onRefresh = {
            onEvent(HomeScreenEvent.OnRefreshSurveys)
            scope.launch {
                pagerState.animateScrollToPage(-1) // for some reason, the pager state is not resetting to 0
            }
        }
    )

    Box(Modifier.pullRefresh(pullRefreshState)) {
        Box {
            when (homeScreenState) {
                HomeScreenState.Loading -> {
                    // loading screen
                    CarouselLoadingScreen()
                }

                is HomeScreenState.Error -> {
                    // error screen
                    ScreenWithMessage(
                        onRetry = { onEvent(HomeScreenEvent.OnRefreshSurveys) },
                    )
                }

                HomeScreenState.Empty -> {
                    // empty screen
                    ScreenWithMessage(
                        errorMessage = stringResource(id = R.string.generic_empty_message),
                        onRetry = { onEvent(HomeScreenEvent.OnRefreshSurveys) },
                    )
                }

                HomeScreenState.Success -> {
                    ModalNavigationDrawer(
                        gesturesEnabled = true,
                        drawerState = drawerState,
                        drawerContent = {
                            ProfileDrawerPage(
                                userName = homeScreenData.userName,
                                avatarUrl = homeScreenData.avatarUrl,
                                onLogout = onLogout
                            )
                        }
                    ) {
                        Box {
                            HorizontalPager(
                                state = pagerState
                            ) { page ->
                                val survey = surveys.getOrNull(page)
                                CarouselCard(
                                    name = survey?.title.orEmpty(),
                                    description = survey?.description.orEmpty(),
                                    imageUrl = survey?.coverImageUrl.orEmpty(),
                                    date = survey?.createdAt.orEmpty(),
                                    avatarUrl = homeScreenData.avatarUrl,
                                    goToDetails = onSurveyButtonPressed,
                                    openProfile = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                )
                            }
                            CarouselDots(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(bottom = 156.dp, start = 16.dp),
                                dotsCount = surveys.size,
                                currentPage = pagerState.currentPage
                            )
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding(),
            refreshing = homeScreenState == HomeScreenState.Loading,
            state = pullRefreshState
        )
    }
}

@Composable
fun ProfileDrawerPage(
    userName: String = "",
    avatarUrl: String = "",
    onLogout: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.7f)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userName.ifEmpty { "UserName" },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUrl)
                            .crossfade(true)
                            .error(R.drawable.ic_launcher_foreground)
                            .build(),
                        contentDescription = null
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onLogout)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Logout",
                        color = LocalNimbleColors.current.nimbleLightGrey,
                    )
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = "version 1.0.0.0",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = LocalNimbleColors.current.nimbleLightGrey,
                    textAlign = TextAlign.Start
                ),
            )
        }
    }
}

@Composable
fun CarouselLoadingScreen() {
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Black,
                            Color.Black
                        )
                    ),
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopLoaderShimmer()
            BottomLoaderShimmer()
        }
    }
}

@Composable
fun CarouselCard(
    name: String,
    description: String,
    date: String,
    imageUrl: String,
    avatarUrl: String = "",
    openProfile: () -> Unit = {},
    goToDetails: () -> Unit = {}
) {
    Box {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radiusX = 8.dp,
                    radiusY = 8.dp,
                    edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(4.dp))
                ), // perform some blur effect to appease poor quality images
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.nimble_survey_bg_opacity)
                .error(R.drawable.nimble_survey_bg_opacity)
                .build(),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.secondary)
                )
            },
            contentDescription = null,
            alignment = Alignment.CenterStart,
            contentScale = ContentScale.FillBounds,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            LocalNimbleColors.current.nimbleDarkerGrey,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.secondary
                        )
                    ),
                    alpha = 0.7f
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = date.toNamedDateFormat(), //"EEEE, MMMM dd"
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    )
                    Text(
                        text = date.toTimeAgo(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
                IconButton(onClick = openProfile) {
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(avatarUrl)
                            .crossfade(true)
                            .error(R.drawable.ic_notification) // the best for now
                            .build(),
                        contentDescription = null
                    )
                }
            }
            // bottom content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(.8f),
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    FloatingActionButton(
                        onClick = goToDetails,
                        backgroundColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
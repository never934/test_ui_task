package com.example.testui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.testui.Option
import com.example.testui.R
import com.example.testui.ui.theme.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun OptionsWidget(
    modifier: Modifier = Modifier,
    size: Dp,
    options: List<Option>
) {

    var isExpandedMain by remember { mutableStateOf(false) }
    var isExpandedBluetooth by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isExpandedBluetooth && isExpandedMain,
        transitionSpec = {
            scaleIn(initialScale = 0.9f) + fadeIn() with
            scaleOut(targetScale = 0.9f) + fadeOut()
        }
    ) { targetBluetoothExpanded ->
        if(targetBluetoothExpanded) {
            BluetoothControlWidget(
                modifier = modifier,
                size = size,
                onClickListener = { isExpandedBluetooth = false },
                contentPadding = size/4
            )
        } else {
            AnimatedContent(
                targetState = isExpandedMain,
                transitionSpec = {
                    expandIn(expandFrom = TopStart) + fadeIn() with
                    shrinkOut(shrinkTowards = TopStart) + fadeOut()
                }
            ) { targetMainExpanded ->
                if(targetMainExpanded) {
                    OptionsExpandedWidget(
                        modifier = modifier,
                        size = size,
                        options = options,
                        onClickListener = { isExpandedMain = false},
                        onBluetoothClicked = { isExpandedBluetooth = true }
                    )

                } else {
                    OptionsNotExpandedWidget(
                        modifier = modifier,
                        initSize = size,
                        options = options,
                        onClickListener = { isExpandedMain = true }
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun OptionsExpandedWidget(
    modifier: Modifier = Modifier,
    size: Dp,
    options: List<Option>,
    onClickListener: () -> Unit,
    onBluetoothClicked: () -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier
            .background(color = Black85, shape = RoundedCornerShape(size / 5))
            .width(size * 2.5f)
            .height(size * 3.5f)
            .clickable(
                onClick = onClickListener,
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple()
            ),
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(size/5)
    ) {
        items(options) {
            OptionItemExpanded(
                modifier = Modifier
                    .clickable {
                        if (it.title == "Bluetooth")
                            onBluetoothClicked.invoke()
                        else
                            onClickListener.invoke()
                    },
                iconId = it.iconId,
                title = it.title,
                size = size / 2.5f,
                contentPadding = size / 4
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun OptionsNotExpandedWidget(
    modifier: Modifier = Modifier,
    initSize: Dp,
    options: List<Option>,
    onClickListener: () -> Unit
) {

    val previewItems =
        if(options.size >= 4) options.subList(0, 4) else options.subList(0, options.size)

    /** btn scale animation **/

    var toState by remember { mutableStateOf(ComponentState.Released) }
    val transition: Transition<ComponentState> = updateTransition(targetState = toState, label = "")
    val scale: Float by transition.animateFloat(
        transitionSpec = { spring() }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 0.90f else 1f
    }
    val size = initSize * scale

    Box(
        modifier = modifier
            .width(size)
            .height(size),
        contentAlignment = Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .background(color = Black85, shape = RoundedCornerShape(size / 10))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            toState = ComponentState.Pressed
                            if (tryAwaitRelease()) onClickListener.invoke()
                            toState = ComponentState.Released
                        }
                    )
                },
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(size / 20)
        ) {
            items(previewItems) {
                OptionItemNotExpanded(iconId = it.iconId, size / 2.5f, contentPadding = size / 40)
            }
        }
    }
}

@Composable
private fun BluetoothControlWidget(
    modifier: Modifier = Modifier,
    size: Dp,
    onClickListener: () -> Unit,
    contentPadding: Dp
) {
    Column(
        modifier = modifier
            .background(color = Black85, shape = RoundedCornerShape(size / 5))
            .width(size * 2.5f)
            .height(size * 3.5f)
            .clickable(
                onClick = onClickListener,
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple()
            )
    ) {
        Image(
            modifier = Modifier
                .align(CenterHorizontally)
                .size(size / 2)
                .padding(top = contentPadding / 3),
            painter = painterResource(id = R.drawable.ic_baseline_bluetooth_24),
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = contentPadding / 4)
                .align(CenterHorizontally),
            text = "Bluetooth",
            fontSize = (size.value/5).sp,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OptionItemNotExpanded(iconId: Int, size: Dp, contentPadding: Dp) {
    Box(modifier = Modifier
        .padding(contentPadding)
        .size(size)
        .clip(CircleShape)
        .background(color = White)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(color = Black90)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(size / 2),
                painter = painterResource(id = iconId),
                contentDescription = null,
            )
        }
    }

}

@Composable
private fun OptionItemExpanded(
    modifier: Modifier = Modifier,
    iconId: Int,
    title: String,
    size: Dp,
    contentPadding: Dp
) {
    Column(modifier.padding(contentPadding)) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color = White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(color = Black90)
            ) {
                Image(
                    modifier = Modifier
                        .align(Center)
                        .size(size / 2),
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(top = contentPadding / 4)
                .align(CenterHorizontally),
            text = title,
            fontSize = (size.value/5).sp,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}
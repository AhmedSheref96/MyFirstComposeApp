package com.el3asas.myfirstcomposeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.el3asas.myfirstcomposeapp.ui.theme.MyFirstComposeAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultPreview()
        }
    }
}

@Composable
fun HeaderSendButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    onClickListener: () -> Unit
) {
    Box(modifier.padding(16.dp)) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .rotate(180f)
                .clickable(
                    onClick = { onClickListener() }
                ))
    }
}

@Composable
fun NameInput(modifier: Modifier,initial:String){
    var value by remember {
        mutableStateOf(initial)
    }
    Box(
        Modifier.layoutId("nameInput")
            .fillMaxWidth()
            .padding(10.dp)) {
        TextField(value = value, modifier = modifier, onValueChange = {
            value=it
        })
    }
}

@Composable
fun BottomView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Green)
            .padding(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append("This is ")
                withStyle(SpanStyle(color = Color.Blue, fontSize = 18.sp)) {
                    append("BottomView")
                }
            },
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ItemsRecyclerView(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(count=1000) {
            Text(
                text = "text $it",
                textAlign = TextAlign.Start,
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(){
    MyFirstComposeAppTheme {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            val scope = rememberCoroutineScope()
            val constraints = ConstraintSet {
                val headerSendButton = createRefFor("headerBtn")
                val bottomView = createRefFor("bottomView")
                val recyclerView = createRefFor("recyclerView")
                val nameInputView = createRefFor("nameInput")
                constrain(headerSendButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }

                constrain(bottomView) {
                    bottom.linkTo(parent.bottom)
                }

                constrain(nameInputView) {
                    bottom.linkTo(bottomView.bottom)
                }

                constrain(recyclerView) {
                    top.linkTo(headerSendButton.bottom)
                    bottom.linkTo(nameInputView.top)
                    height= Dimension.fillToConstraints
                }
            }

            ConstraintLayout(constraints, modifier = Modifier.fillMaxSize()) {
                val painter = painterResource(id = R.drawable.ic_baseline_send_24)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Cyan)
                        .layoutId("headerBtn")
                ) {
                    HeaderSendButton(painter = painter) {
                        Log.d("****************", "onCreate: HeaderSendButton clicked")
                        scope.launch {
                            scaffoldState.snackbarHostState.apply {
                                showSnackbar(
                                    message = "HeaderSendButton clicked",
                                    actionLabel = "send again",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                    Text(
                        text = "Add Your Templates To Send..",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.align(CenterVertically)
                    )
                }

                ItemsRecyclerView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("recyclerView")
                )

                NameInput(Modifier.fillMaxWidth(),"initial value")
                BottomView(Modifier.layoutId("bottomView"))
            }
        }
    }
}
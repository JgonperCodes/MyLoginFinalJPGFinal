package com.jgp.myloginfinaljpg.screens.splash
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jgp.myloginfinaljpg.R
import com.jgp.myloginfinaljpg.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = { OvershootInterpolator(4f).getInterpolation(it) }
            )
        )

        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000
            )
        )

        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = { it }
            )
        )

        delay(2000)

        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(Screens.LoginScreen.name) {
                popUpTo(Screens.SplashScreen.name) { inclusive = true }
            }
        } else {
            navController.navigate(Screens.HomeScreen.name) {
                popUpTo(Screens.SplashScreen.name) { inclusive = true }
            }
        }
    }

    val tumblrBlue = Color(0xFF3d536d)


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
    ) {

        Surface(
            modifier = Modifier
                .size(300.dp, 150.dp)
                .scale(scale.value)
                .alpha(alpha.value),
            shape = MaterialTheme.shapes.medium,
            color = tumblrBlue,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Login por Judit G.P",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))


        Surface(
            modifier = Modifier
                .size(100.dp)
                .rotate(rotation.value),
            shape = CircleShape,
            color = Color(0xFF3d536d),
            border = BorderStroke(width = 2.dp, color = Color.Black),
        ) {
            Image(
                painter = painterResource(id = R.drawable.tumblrlogo),
                contentDescription = "Tumblr Logo",
                modifier = Modifier.size(250.dp)
            )
        }
    }
}

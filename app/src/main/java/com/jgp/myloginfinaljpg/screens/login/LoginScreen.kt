package com.jgp.myloginfinaljpg.screens.login

import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jgp.myloginfinaljpg.R
import com.jgp.myloginfinaljpg.navigation.Screens

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    val token = "102189252440-i2b5v33u25544iiispcsq8mn9nm93fm1.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult() //
    ) {
        val task =
            GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                navController.navigate(Screens.HomeScreen.name)
            }
        } catch (ex: Exception) {
            Log.d("My Login", "GoogleSignIn falló")
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFF3d536d)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.tumblrlogo),
                contentDescription = "Logo Tumblr",
                modifier = Modifier.size(290.dp).padding(top = 5.dp)
            )

            if (showLoginForm.value) {
                Text(text = "Inicia sesion", color = Color.White, fontSize = 20.sp)
                UserForm(isCreateAccount = false) { email, password ->
                    Log.d("My Login", "Logueando con $email y $password")
                    viewModel.signInWithEmailAndPassword(
                        email,
                        password
                    ) {
                        navController.navigate(Screens.HomeScreen.name)
                    }

                }
            } else {
                Text(text = "Crear una cuenta", color = Color.White, fontSize =  20.sp)
                UserForm(isCreateAccount = true) { email, password ->
                    Log.d("My Login", "Creando cuenta con $email y $password")
                    viewModel.createUserWithEmailAndPassword(
                        email,
                        password
                    ) {
                        navController.navigate(Screens.HomeScreen.name)
                    }

                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = if (showLoginForm.value) "¿No tienes cuenta?"
                else "¿Ya tienes cuenta?"
                val text2 = if (showLoginForm.value) "Registrate"
                else "Inicia sesión"
                Text(text = text1, color = Color.White, fontSize = 15.sp)
                Text(
                    text = text2,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {

                        val opciones = GoogleSignInOptions
                            .Builder(
                                GoogleSignInOptions.DEFAULT_SIGN_IN
                            )
                            .requestIdToken(token)
                            .requestEmail()
                            .build()
                        val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
                        launcher.launch(googleSingInCliente.signInIntent)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Login con GOOGLE",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                )

                Text(
                    text = "Login con Google",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun UserForm(isCreateAccount: Boolean, onDone: (String, String) -> Unit) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val validInput = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        EmailInput(emailState = email)
        PasswordInput(passwordState = password, passwordVisible = passwordVisible)
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Log in",
            inputValido = validInput
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp,
                start = 30.dp, end = 30.dp),
        shape = RoundedCornerShape(8.dp),
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color(0xFF6791b9),
            containerColor = Color(0xFF6791b9),
            disabledContentColor =  Color(0xFF6791b9),
            disabledContainerColor = Color(0xFF6791b9)
        )
    ) {
        Text(text = textId,  color = Color.White, modifier = Modifier.padding(8.dp), fontSize = 19.sp, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valuestate = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}


@Composable
fun InputField(
    valuestate: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true,
) {
    TextField(
        value = valuestate.value,
        onValueChange = { valuestate.value = it },
        label = { Text(text = labelId, color = Color.Gray) },
        singleLine = isSingleLine,
        modifier = Modifier
            .fillMaxWidth().padding(start = 30.dp, end = 30.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(Color.White)
    )
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String = "Password",
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    TextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId, color = Color.Gray) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth().padding(start = 30.dp, end = 30.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            } else null
        },
       colors = TextFieldDefaults.colors(Color.White)
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility

    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}

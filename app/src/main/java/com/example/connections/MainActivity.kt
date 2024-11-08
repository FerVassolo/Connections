package com.example.connections

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.connections.home.HomeViewModel
import com.example.connections.navigation.BottomBar
import com.example.connections.navigation.NavHostComposable
import com.example.connections.navigation.TopBar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Inicializa el BiometricPrompt y configura el mensaje
            biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // La autenticación fue exitosa
                    navigateToHome()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Maneja los errores específicos y muestra un mensaje
                    when (errorCode) {
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> showToast("Hardware biométrico no disponible")
                        BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> showToast("No se pudo procesar la huella")
                        BiometricPrompt.ERROR_TIMEOUT -> showToast("Tiempo de espera agotado")
                        BiometricPrompt.ERROR_LOCKOUT -> showToast("Demasiados intentos, intenta más tarde")
                        BiometricPrompt.ERROR_CANCELED -> showToast("Autenticación cancelada")
                        BiometricPrompt.ERROR_USER_CANCELED -> showToast("Has cancelado la autenticación")
                        else -> showToast("Error desconocido: $errString")
                    }
                    Log.e("BiometricAuth", "Error code: $errorCode, message: $errString")
                }

                override fun onAuthenticationFailed() {
                    // La autenticación falló, muestra un mensaje al usuario
                    showToast("Autenticación fallida, intenta nuevamente")
                }
            })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación Biométrica")
                .setSubtitle("Inicia sesión usando tu huella o credenciales")
                .setNegativeButtonText("Cancelar")
                .build()

            setContent {
                AuthenticationScreen(onRetryAuthentication = {
                    biometricPrompt.authenticate(promptInfo)
                })
            }

            // Lanza la autenticación biométrica al abrir la actividad
            biometricPrompt.authenticate(promptInfo)

        } catch (e: Exception) {
            Log.e("BiometricAuth", "Error al inicializar la autenticación biométrica", e)
            showToast("No se pudo inicializar la autenticación biométrica")
        }
    }

    @Composable
    fun AuthenticationScreen(onRetryAuthentication: () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Por favor, autentícate para continuar")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onRetryAuthentication) {
                    Text("Reintentar autenticación")
                }
            }
        }
    }

    private fun navigateToHome() {
        setContent {
            val viewModel = hiltViewModel<HomeViewModel>()
            val userName by viewModel.userName.collectAsState()
            val navController = rememberNavController()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    topBar = {
                        TopBar(
                            userName = userName,
                            navController = navController
                        )
                    },
                    bottomBar = {
                        BottomBar(
                            onNavigate = {
                                navController.navigate(it)
                            }
                        )
                    },
                ) { innerPadding ->
                    NavHostComposable(innerPadding, navController)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

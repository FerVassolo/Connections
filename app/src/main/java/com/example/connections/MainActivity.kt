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
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.connections.home.HomeViewModel
import com.example.connections.navigation.BottomBar
import com.example.connections.navigation.NavHostComposable
import com.example.connections.navigation.TopBar
import com.example.connections.ui.theme.ConnectionsTheme // Importa el tema
import com.example.connections.ui.theme.authScreenSpacer
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
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> showToast(getString(R.string.unavailable_bio))
                        BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> showToast(getString(R.string.unable_to_process))
                        BiometricPrompt.ERROR_TIMEOUT -> showToast(getString(R.string.timeout))
                        BiometricPrompt.ERROR_LOCKOUT -> showToast(getString(R.string.lockout))
                        BiometricPrompt.ERROR_CANCELED -> showToast(getString(R.string.auth_cancelled))
                        BiometricPrompt.ERROR_USER_CANCELED -> showToast(getString(R.string.user_cancelled))
                        else -> showToast(getString(R.string.unknown_error) + errString)
                    }
                }

                override fun onAuthenticationFailed() {
                    // La autenticación falló, muestra un mensaje al usuario
                    showToast(getString(R.string.failed_toast))
                }
            })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.bio_auth))
                .setSubtitle(getString(R.string.use_bio))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()

            setContent {
                ConnectionsTheme { // Aplica el tema aquí
                    AuthenticationScreen(onRetryAuthentication = {
                        biometricPrompt.authenticate(promptInfo)
                    })
                }
            }

            // Lanza la autenticación biométrica al abrir la actividad
            biometricPrompt.authenticate(promptInfo)

        } catch (e: Exception) {
            showToast(getString(R.string.bio_catch))
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
                Text(text = stringResource(R.string.please_auth))

                Spacer(modifier = Modifier.height(authScreenSpacer))

                Button(onClick = onRetryAuthentication) {
                    Text(stringResource(R.string.retry_auth))
                }
            }
        }
    }

    private fun navigateToHome() {
        setContent {
            ConnectionsTheme { // Aplica el tema aquí también
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
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

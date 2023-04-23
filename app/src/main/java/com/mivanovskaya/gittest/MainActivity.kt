package com.mivanovskaya.gittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.mivanovskaya.gittest.data.KeyValueStorage.Companion.authToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        if (authToken != null)
            navController.navigate(R.id.action_authFragment_to_repositoriesListFragment)
    }
}
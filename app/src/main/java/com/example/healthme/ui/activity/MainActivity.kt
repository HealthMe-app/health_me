package com.example.healthme.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.healthme.R
import com.example.healthme.databinding.ActivityMainBinding
import com.example.healthme.repository.Repository
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav: BottomNavigationView = binding.btmNav
        bottomNav.itemIconTintList = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNav, navHostFragment.navController)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]


        // registration is working completely fine

//        viewModel.register(
//            "lol1@email.ru",
//            "Danil",
//            false,
//            "2005-01-15",
//            "1111"
//        )
//        viewModel.myResponseUserInfo.observe(this, Observer { response ->
//            if (response.isSuccessful) {
//                Log.d("Response", response.body().toString())
//                Log.d("Response", response.code().toString())
//                Log.d("Response", response.message())
//            } else {
//                val errorText = response.errorBody()?.string()?.substringAfter("[\"")?.dropLast(3)
//                Log.e("Error Response", errorText.toString())
//            }
//        })

        // authorization is working completely fine

//        viewModel.login(
//            "lol1@email.ru",
//            "1111"
//        )
//        viewModel.myResponseUserInfo.observe(this, Observer { response ->
//            if (response.isSuccessful) {
//                Log.d("Response", response.body()?.token.toString())
//                Log.d("Response", response.code().toString())
//                Log.d("Response", response.message())
//            } else {
//                val errorText = response.errorBody()?.string()?.substringAfter("[\"")?.dropLast(3)
//                Log.e("Error Response", errorText.toString())
//            }
//        })


//        viewModel.getUser("Token 213f93e3463d8394ad16f2cea4ded481b3f44059a65a9ae3530539ca344539d4")
//        viewModel.myResponse.observe(this, Observer { response ->
//            if (response.isSuccessful)
//                Log.d("Response", response.body()?.first_name.toString())
//            else {
//                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
//                Log.e("Error Response", errorText.toString())
//            }
//        })

//        viewModel.logout("Token 213f93e3463d8394ad16f2cea4ded481b3f44059a65a9ae3530539ca344539d4")
//        viewModel.myResponseString.observe(this, Observer { response ->
//            if (response.isSuccessful)
//                Log.d("Response", response.toString())
//            else {
//                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
//                Log.e("Error Response", errorText.toString())
//            }
//        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
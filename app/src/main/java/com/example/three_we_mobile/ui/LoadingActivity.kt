package com.example.three_we_mobile.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.three_we_mobile.MainActivity
import com.example.three_we_mobile.databinding.ActivityLoadingBinding
import com.example.three_we_mobile.utils.ConnectionLiveData
import com.example.three_we_mobile.utils.showSnackbar
import com.example.three_we_mobile.viewmodel.LoadingViewModel
import com.google.android.material.snackbar.Snackbar
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {

    private val loadingViewModel by viewModels<LoadingViewModel>()

    private lateinit var _binding: ActivityLoadingBinding
    private val binding get() =  _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCenter.configure(application, "2530ec0b52500981b30199ff45a4a5e9d018f97d")

        if (AppCenter.isConfigured()) {
            AppCenter.start(Analytics::class.java)
            AppCenter.start(Crashes::class.java)
        }

        loadingViewModel.connectionLiveData = ConnectionLiveData(this)

        lifecycleScope.launchWhenStarted {
            delay(2000)
            loadingViewModel.appData.collect {
                val result = it ?: return@collect

                if (result.data?.isActive == true){
                    startActivity(Intent(this@LoadingActivity, MainActivity::class.java).apply {
                        finish()
                    })
                }else{
                    Toast.makeText(this@LoadingActivity, result.data.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            loadingViewModel.events.collect { event ->
                when(event){
                    is LoadingViewModel.Event.ShowErrorMessage ->
                        showSnackbar(
                            event.error.localizedMessage,
                            Snackbar.LENGTH_LONG,
                            binding.rootLayout
                        )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadingViewModel.onStart()
    }
}
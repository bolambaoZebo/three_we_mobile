package com.example.three_we_mobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.three_we_mobile.R
import com.example.three_we_mobile.adapter.SliderImageAdapter
import com.example.three_we_mobile.databinding.ActivityTempBinding
import com.example.three_we_mobile.model.SliderImage

class TempActivity : AppCompatActivity() {

    private lateinit var sliderAdapter: SliderImageAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var viewPager2: ViewPager2

    private lateinit var _binding: ActivityTempBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTempBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    /**SLIDER METHODS**/
    private fun setSliderAdapter() {
        var adsImages = ArrayList<SliderImage>()
        adsImages.add(SliderImage(""))
        adsImages.add(SliderImage(""))

        sliderAdapter = SliderImageAdapter(
            this,
            adsImages,
            onItemClick = {
                lifecycleScope.launchWhenCreated {
//                    if(isConnected){
//                        MainPageViewModel.ads.observe(this@MainPage) { ads ->
//                            if(ads.isActive == true){
//                                goToLink(this@MainPage, URL_3WE_BANNER)
//                            }else{
//                                this@MainPage.toast(URL_FB_HORSE)
//                            }
//                        }
//                    }
                }
            })

        sliderHandler = Handler(Looper.getMainLooper())

        viewPager2 = binding.viewPager2
        viewPager2.adapter = sliderAdapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipChildren = false
        viewPager2.clipToPadding = false

        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)
    }

    private val sliderRunnable = Runnable {
        kotlin.run {
            viewPager2.currentItem = if (viewPager2.size > viewPager2.currentItem) viewPager2.currentItem + 1 else 0
        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            sliderHandler.removeCallbacks(sliderRunnable)
            sliderHandler.postDelayed(sliderRunnable, 4000)
        }
    }

}
package com.example.three_we_mobile

import android.app.ActionBar
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.three_we_mobile.databinding.ActivityMainBinding
import com.example.three_we_mobile.listener.MainActivityListener
import com.example.three_we_mobile.ui.ebooks.Ebooks
import com.example.three_we_mobile.ui.esports.Esports
import com.example.three_we_mobile.ui.horse.Horse
import com.example.three_we_mobile.ui.movies.Movies
import com.example.three_we_mobile.ui.page.Page
import com.example.three_we_mobile.ui.results.Results
import com.example.three_we_mobile.ui.show.Show
import com.example.three_we_mobile.ui.sports.Sports
import com.example.three_we_mobile.utils.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), MainActivityListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var moviesFragment: Movies
    private lateinit var showFragment: Show
    private lateinit var horseFramgent: Horse
    private lateinit var pageFragment: Page
    private lateinit var esportsFragment: Esports
    private lateinit var resultFragment: Results
    private lateinit var ebookFragment: Ebooks
    private lateinit var sportsFragment: Sports

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private lateinit var navController: NavController

    private val fragments: Array<Fragment>
        get() = arrayOf(
            moviesFragment,
            showFragment,
            horseFramgent,
            pageFragment,
            esportsFragment,
            resultFragment,
            ebookFragment,
            sportsFragment
        )

    private var selectedIndex = 0
    private val selectedFragment get() = fragments[selectedIndex]

    private fun selectFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()


        title = when (selectedFragment) {
            is Movies -> getString(R.string.movies)
            is Show -> getString(R.string.show)
            is Horse -> getString(R.string.horse)
            is Page -> getString(R.string.page)
            is Esports -> getString(R.string.esports)
            is Results -> getString(R.string.resutls)
            is Ebooks -> getString(R.string.ebooks)
            is Sports -> getString(R.string.sports)
            else -> ""
        }

        toolbar.title = title

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarLayout = findViewById(R.id.main_app_bar_layout)
        toolbar = binding.appBarMain.toolbar

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_movies,
                R.id.nav_sports,
                R.id.nav_show,
                R.id.nav_horse,
                R.id.nav_page,
                R.id.nav_esports,
                R.id.nav_results,
                R.id.nav_esports
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menu ->
            drawerLayout.close()
            val fragment = when (menu.itemId){
                R.id.nav_movies -> moviesFragment
                R.id.nav_sports -> sportsFragment
                R.id.nav_show -> showFragment
                R.id.nav_horse -> horseFramgent
                R.id.nav_page -> pageFragment
                R.id.nav_esports -> esportsFragment
                R.id.nav_results -> resultFragment
                R.id.nav_book -> ebookFragment
                else -> throw IllegalArgumentException("Unexpected itemId")
            }

            if(selectedFragment.tag == fragment.tag){
//                if( fragment is OnBottomNavigationFragmentReselectedListener){
//                    fragment.onBottomNavigationFragmentReselected()
//                }
            }else {
                selectFragment(fragment)
            }
            return@setNavigationItemSelectedListener true
        }


        setupFragment(savedInstanceState)
    }

//    interface OnBottomNavigationFragmentReselectedListener {
//        fun onBottomNavigationFragmentReselected()
//    }

    private fun setupFragment(savedInstanceState: Bundle?){

        if (savedInstanceState == null) {
            moviesFragment = Movies.newInstance()
            sportsFragment = Sports.newInstance()
            showFragment = Show.newInstance()
            horseFramgent = Horse.newInstance()
            pageFragment = Page.newInstance()
            esportsFragment = Esports.newInstance()
            resultFragment = Results.newInstance()
            ebookFragment = Ebooks.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment_content_main, moviesFragment, TAG_MOVIE_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, sportsFragment, TAG_SPORTS_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, showFragment, TAG_SHOW_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, horseFramgent, TAG_HORSE_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, pageFragment, TAG_PAGE_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, esportsFragment, TAG_ESPORTS_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, resultFragment, TAG_RESULTS_FRAGMENT)
                .add(R.id.nav_host_fragment_content_main, ebookFragment, TAG_EBOOK_FRAGMENT)
                .commit()

            moviesFragment.mainActivityListener = this
            sportsFragment.mainActivityListener = this
            showFragment.mainActivityListener = this
            horseFramgent.mainActivityListener = this
            pageFragment.mainActivityListener = this
            esportsFragment.mainActivityListener = this
            resultFragment.mainActivityListener = this
            ebookFragment.mainActivityListener = this

        } else {
            moviesFragment =
                supportFragmentManager.findFragmentByTag(TAG_MOVIE_FRAGMENT) as Movies
            sportsFragment =
                supportFragmentManager.findFragmentByTag(TAG_SPORTS_FRAGMENT) as Sports
            showFragment =
                supportFragmentManager.findFragmentByTag(TAG_SHOW_FRAGMENT) as Show
            horseFramgent =
                supportFragmentManager.findFragmentByTag(TAG_HORSE_FRAGMENT) as Horse
            pageFragment =
                supportFragmentManager.findFragmentByTag(TAG_PAGE_FRAGMENT) as Page
            esportsFragment =
                supportFragmentManager.findFragmentByTag(TAG_ESPORTS_FRAGMENT) as Esports
            resultFragment =
                supportFragmentManager.findFragmentByTag(TAG_RESULTS_FRAGMENT) as Results
            ebookFragment =
                supportFragmentManager.findFragmentByTag(TAG_EBOOK_FRAGMENT) as Ebooks

            selectedIndex = savedInstanceState.getInt(KEY_SELECTED_INDEX, 0)
        }

        selectFragment(selectedFragment)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(baseContext, "Landscape Mode", Toast.LENGTH_SHORT).show()
            with(window) {
                setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                )
            }
            hideSystemUI()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(baseContext, "Portrait Mode", Toast.LENGTH_SHORT).show()
            var win:Window = this.window
            win.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            showSystemUI()
        }
    }

    override fun onFullScreen(full: Boolean) {
        appBarLayout.setExpanded(full, true)
        if (!full){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }else{
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            setStatusBarLightTextNewApi(window,false)
        }
    }

//    private fun setStatusBarLightTextOldApi(window: Window, isLight: Boolean) {
//        val decorView = window.decorView
//        decorView.systemUiVisibility =
//            if (isLight) {
//                decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//            } else {
//                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//    }

    private fun setStatusBarLightTextNewApi(window: Window, isLightText: Boolean) {
        ViewCompat.getWindowInsetsController(window.decorView)?.apply {
            isAppearanceLightStatusBars = !isLightText
        }
    }

    // Function to hide NavigationBar
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    // Function to hide NavigationBar
    @RequiresApi(Build.VERSION_CODES.R)
    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }

}
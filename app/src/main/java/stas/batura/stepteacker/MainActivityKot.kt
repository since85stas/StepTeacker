package stas.batura.stepteacker

import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import stas.batura.stepteacker.service.StepService

@AndroidEntryPoint
class MainActivityKot : AppCompatActivity(R.layout.main_activity) {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        val navView: BottomNavigationView = nav_view
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.todayFragment, R.id.graphFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        startService()


    }

    override fun onStart() {
        super.onStart()
        addObservers()
        mainViewModel.createServiceConnection()
    }

    override fun onStop() {
        super.onStop()
        val conn: ServiceConnection? = mainViewModel.serviceConnection.value
        if (conn != null) {
            unbindService(conn)
        }
        removeObservers()
    }

    /**
     * adding observers
     */
    private fun addObservers() {

        // fetting service connection
        mainViewModel.serviceConnection.observe(this,
            { serviceConnection -> serviceConnection?.let { bindCurrentService(it) } })
    }

    /**
     * removing observers
     */
    private fun removeObservers() {
        mainViewModel.serviceConnection.removeObservers(this)
    }

    private fun startService() {
        val serviceIntent = Intent(this, StepService::class.java)
        startService(serviceIntent)
    }

    /**
     * Binding service to activity
     *
     * @param serviceConnection
     */
    private fun bindCurrentService(serviceConnection: ServiceConnection) {
        // привязываем сервис к активити
        bindService(
            Intent(applicationContext, StepService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
    }
}
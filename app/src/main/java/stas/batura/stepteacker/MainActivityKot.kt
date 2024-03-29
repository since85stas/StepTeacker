package stas.batura.stepteacker

import android.Manifest
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*
import stas.batura.stepteacker.service.StepService
import timber.log.Timber

const val PHYISCAL_ACTIVITY = 11

@AndroidEntryPoint
class MainActivityKot : AppCompatActivity(R.layout.main_activity) {

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // получаем вью модель
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //ask for permission
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), PHYISCAL_ACTIVITY)
        }

        // определяем контроллер
        configureNavigController()

        // стартуем сервис
        startService()


    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
        addObservers()
        mainViewModel.createServiceConnection()
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
        val conn: ServiceConnection? = mainViewModel.serviceConnection.value
        if (conn != null) {
            unbindService(conn)
        }
        removeObservers()
    }

    /**
     * задаем контроллер и bottom navigation
     */
    private fun configureNavigController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        val navView: BottomNavigationView = nav_view
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.todayFragment, R.id.graphFragment, R.id.historyFragment
            )
        )
        val toolbar = ActivityCompat.requireViewById<MaterialToolbar>(this, R.id.toolbar)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * adding observers
     */
    private fun addObservers() {

        // fetting service connection
        mainViewModel.serviceConnection.observe(this,
            { serviceConnection -> serviceConnection?.let {
                Timber.d("observe connection: $it")
                bindCurrentService(it)
            } })
    }

    /**
     * removing observers
     */
    private fun removeObservers() {
        mainViewModel.serviceConnection.removeObservers(this)
    }

    private fun startService() {
        Timber.d("service started")
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
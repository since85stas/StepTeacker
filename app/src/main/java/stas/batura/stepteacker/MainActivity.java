package stas.batura.stepteacker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;

import dagger.hilt.android.AndroidEntryPoint;
import stas.batura.stepteacker.service.PressureService;
import stas.batura.stepteacker.utils.ContexUtils;



@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public final int EXTERNAL_READ_PERMISSION_GRANT = 112;

    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setContentView(R.layout.main_activity);
//        setContentView(R.layout.main_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MainFragment.newInstance())
//                    .commitNow();
//        }
        if (!ContexUtils.checkStorageAccessPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] array = new String[1];
                array[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//                array[1] = Manifest.permission.LoC
                this.requestPermissions(
                        array,
                        EXTERNAL_READ_PERMISSION_GRANT
                );
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        addObservers();

        startService(
                new Intent(
                        this,
                        PressureService.class
                    )
                );
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeObservers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainViewModel.getServiceConnection().getValue() != null) {

            // unbinding service after destroing activity
            unbindService(mainViewModel.getServiceConnection().getValue());
        }
    }

    /**
     * adding observers
     */
    private void addObservers() {

        // fetting service connection
        mainViewModel.getServiceConnection().observe(this, new Observer<ServiceConnection>() {
            @Override
            public void onChanged(ServiceConnection serviceConnection) {
                if (serviceConnection != null) {
                    bindCurrentService(serviceConnection);
                }
            }
        });

        mainViewModel.getStopServiceLive().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean && mainViewModel.getServiceConnection().getValue() != null) {
                    unbindService(mainViewModel.getServiceConnection().getValue());

                    mainViewModel.closeService();
                }
            }
        });
    }

    /**
     * removing observers
     */
    private void removeObservers() {
        mainViewModel.getServiceConnection().removeObservers(this);
        mainViewModel.getStopServiceLive().removeObservers(this);
    }

    /**
     * Binding service to activity
     * @param serviceConnection
     */
    private void  bindCurrentService(ServiceConnection serviceConnection) {
        // привязываем сервис к активити
        bindService(new Intent(getApplicationContext(), PressureService.class),
        serviceConnection,
                Context.BIND_AUTO_CREATE);
    }


}
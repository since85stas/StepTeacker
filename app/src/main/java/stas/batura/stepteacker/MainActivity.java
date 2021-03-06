package stas.batura.stepteacker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import stas.batura.stepteacker.service.StepService;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity.this.getPackageName()";

    public final int EXTERNAL_READ_PERMISSION_GRANT = 112;

    public final int PHYISCAL_ACTIVITY = 11;

    MainViewModel mainViewModel;

    @Inject
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setContentView(R.layout.main_activity);

        mainViewModel.createService();

//        if(ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
//            //ask for permission
//
//            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHYISCAL_ACTIVITY);
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        addObservers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeObservers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
     *
     * @param serviceConnection
     */
    private void bindCurrentService(ServiceConnection serviceConnection) {
        // привязываем сервис к активити
        bindService(new Intent(getApplicationContext(), StepService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);
    }


}
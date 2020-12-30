package stas.batura.stepteacker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.AndroidEntryPoint;
import stas.batura.stepteacker.service.PressureService;
import stas.batura.stepteacker.utils.ContexUtils;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity.this.getPackageName()";

    public final int EXTERNAL_READ_PERMISSION_GRANT = 112;

    MainViewModel mainViewModel;

    FitnessOptions fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build();

    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        setContentView(R.layout.main_activity);

        account =  GoogleSignIn.getAccountForExtension(getApplicationContext(), fitnessOptions);

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    1, // e.g. 1
                    account,
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }
//        if (!ContexUtils.checkStorageAccessPermissions(this)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                String[] array = new String[1];
//                array[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
////                array[1] = Manifest.permission.LoC
//                this.requestPermissions(
//                        array,
//                        EXTERNAL_READ_PERMISSION_GRANT
//                );
//            }
//        }
    }

    private void accessGoogleFit() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusYears(1);
        long endSeconds = end.atZone(ZoneId.systemDefault()).toEpochSecond();
        long startSeconds = start.atZone(ZoneId.systemDefault()).toEpochSecond();

        DataReadRequest readRequest =new  DataReadRequest.Builder()
                .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startSeconds, endSeconds, TimeUnit.SECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        Fitness.getHistoryClient(this, account)
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.i(TAG, "OnSuccess()");
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "OnFailure()", e));
    }

    @Override
    protected void onStart() {
        super.onStart();
        addObservers();

//        startService(
//                new Intent(
//                        this,
//                        PressureService.class
//                )
//        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeObservers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mainViewModel.getServiceConnection().getValue() != null) {
//
//            // unbinding service after destroing activity
//            unbindService(mainViewModel.getServiceConnection().getValue());
//        }
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
        bindService(new Intent(getApplicationContext(), PressureService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE);
    }


}
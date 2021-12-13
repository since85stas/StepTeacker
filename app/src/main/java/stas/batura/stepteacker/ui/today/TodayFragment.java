package stas.batura.stepteacker.ui.today;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dagger.hilt.android.AndroidEntryPoint;
import stas.batura.stepteacker.MainViewModel;
import stas.batura.stepteacker.R;
import stas.batura.stepteacker.data.room.Day;
import stas.batura.stepteacker.databinding.TodayFragmentBinding;

@AndroidEntryPoint
public class TodayFragment extends Fragment {

    private static final String TAG = TodayFragment.class.getSimpleName();

    private TodayViewModel fragmentModel;

    private MainViewModel mainViewModel;

    private RecyclerView recyclerView;

//    private PressureAdapter adapter;

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentModel = new ViewModelProvider(this).get(TodayViewModel.class);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        TodayFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.today_fragment,
                container,
                false);
        binding.setViewModel(fragmentModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }



    @Override
    public void onStart() {
        super.onStart();
//        addObservers();
    }

    @Override
    public void onStop() {
        super.onStop();
//        removeObservers();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * adding observers
     */
    private void  addObservers() {

    }


    private void movetoGraphFragm() {

    }

    /**
     * removing observers
     */
    private void removeObservers() {


    }
}
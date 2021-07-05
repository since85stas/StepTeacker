package stas.batura.stepteacker.ui.today;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import dagger.hilt.android.AndroidEntryPoint;
import stas.batura.stepteacker.MainViewModel;
import stas.batura.stepteacker.R;
import stas.batura.stepteacker.databinding.TodayFragmentBinding;

@AndroidEntryPoint
public class TodayFragment extends Fragment {

    private static final String TAG = TodayFragment.class.getSimpleName();

    private MainFragmentViewModel fragmentModel;

    private MainViewModel mainViewModel;

    private RecyclerView recyclerView;

    private PressureAdapter adapter;

//    private RecyclerView recyclerRainView;
//
//    private RainAdapter rainAdapter;

    private Button stopButton;

    private RadioGroup radioGroup;

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

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
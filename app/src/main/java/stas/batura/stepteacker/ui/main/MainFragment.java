package stas.batura.stepteacker.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
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

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import stas.batura.stepteacker.MainViewModel;
import stas.batura.stepteacker.R;
import stas.batura.stepteacker.data.room.Step;
import stas.batura.stepteacker.databinding.PressureFragmentBinding;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private MainFragmentViewModel fragmentModel;

    private MainViewModel mainViewModel;

    private RecyclerView recyclerView;

    private PressureAdapter adapter;

//    private RecyclerView recyclerRainView;
//
//    private RainAdapter rainAdapter;

    private Button stopButton;

    private RadioGroup radioGroup;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fragmentModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        PressureFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.pressure_fragment,
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

//        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                Navigation.findNavController(view).navigate(R.id.action_listFragment_to_graphFragment);
//            }
//        };
//
//        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

    }

    /**
     * adding observers
     */
    private void  addObservers() {

        fragmentModel.getPressureLive().observe(getViewLifecycleOwner(), new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                Collections.reverse(steps);
                adapter.submitList(steps);
            }
        });
    }


    private void movetoGraphFragm() {

    }

    /**
     * removing observers
     */
    private void removeObservers() {


    }
}
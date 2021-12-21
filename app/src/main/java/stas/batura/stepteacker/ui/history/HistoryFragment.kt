package stas.batura.stepteacker.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import stas.batura.stepteacker.MainViewModel
import stas.batura.stepteacker.R
import stas.batura.stepteacker.databinding.GraphFragmentBinding
import stas.batura.stepteacker.databinding.HistoryFragmentBinding
import stas.batura.stepteacker.ui.graph.GraphViewModel

@AndroidEntryPoint
class HistoryFragment: Fragment() {

    private val TAG = HistoryFragment::class.java.simpleName

    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var mainViewModel: MainViewModel

    lateinit var binding: HistoryFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
//        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.history_fragment,
            container,
            false
        )
        binding.historyViewModel = historyViewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        addObservers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        removeObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding = GraphFragmentBinding.bind(view)
    }


    private fun addObservers() {

        historyViewModel.daysHistory.observe(viewLifecycleOwner) {days ->
            Log.d(TAG, "addObservers: $days")
        }
        
        historyViewModel.daysHistoryCount.observe(viewLifecycleOwner) {
            Log.d(TAG, "addObservers: $it")

        }
    }

    private fun removeObservers() {

    }

}
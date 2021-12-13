package stas.batura.stepteacker.ui.graph

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.graph_fragment.*
import stas.batura.stepteacker.MainViewModel
import stas.batura.stepteacker.R
import stas.batura.stepteacker.databinding.GraphFragmentBinding


@AndroidEntryPoint
class GraphFragment: Fragment() {

    private val TAG = GraphFragment::class.java.simpleName

    private lateinit var graphViewModel: GraphViewModel

    private lateinit var mainViewModel: MainViewModel

    lateinit var radioGroup: RadioGroup

    private var isStarted = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        graphViewModel = ViewModelProvider(this).get(GraphViewModel::class.java)
//        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val binding: GraphFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.graph_fragment,
                container,
                false)

        binding.graphModel = graphViewModel
        binding.mainViewModel = mainViewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        return binding.getRoot()
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
        isStarted = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private fun addObservers() {
        graphViewModel.stepsList.observe(viewLifecycleOwner) {list ->
            Log.d(TAG, "onViewCreated: ")
        }
    }

    private fun removeObservers() {

    }

 }
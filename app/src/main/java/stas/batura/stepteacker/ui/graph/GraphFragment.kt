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
import stas.batura.stepteacker.data.room.Step
import stas.batura.stepteacker.databinding.GraphFragmentBinding
import stas.batura.stepteacker.utils.getCurrentDayBegin


@AndroidEntryPoint
class GraphFragment: Fragment() {

    private val TAG = GraphFragment::class.java.simpleName

    private lateinit var graphViewModel: GraphViewModel

    private lateinit var mainViewModel: MainViewModel

    lateinit var radioGroup: RadioGroup

    private var isStarted = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val time = getCurrentDayBegin()

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

        graphViewModel.updateRainpower()

        checkedradio()

        isStarted = true
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

        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    // show normal x values
//                     (value/60 - 24).toInt().toString()
                    ""
                } else {
                    // show currency for y values
                    super.formatLabel(value, isValueX)
                }
            }
        }

        radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {

                R.id.rain_0 -> {

                        graphViewModel.saveRainPower(0)
                        mainViewModel.setServiceRain(0)
                    if (isStarted) {
//                        mainViewModel.savePressureValue()
                    }
                }
                R.id.rain_1 -> {

                        graphViewModel.saveRainPower(1)
                        mainViewModel.setServiceRain(1)
                    if (isStarted) {
//                        mainViewModel.savePressureValue()
                    }
                }
                R.id.rain_2 -> {
                        graphViewModel.saveRainPower(2)
                        mainViewModel.setServiceRain(2)
                    if (isStarted) {
//                        mainViewModel.savePressureValue()
                    }
                }
                R.id.rain_3 -> {

                        graphViewModel.saveRainPower(3)
                        mainViewModel.setServiceRain(3)
                    if (isStarted) {
//                        mainViewModel.savePressureValue()
                    }
                }
                R.id.rain_4 -> {

                        graphViewModel.saveRainPower(4)
                        mainViewModel.setServiceRain(4)
                    if (isStarted) {
//                        mainViewModel.savePressureValue()
                    }
                }
                R.id.rain_5 -> {

                        graphViewModel.saveRainPower(5)
                        mainViewModel.setServiceRain(5)
                    if (isStarted) {
//                        mainViewModel.savePressureValue()
                    }
                }
            }
        })

//        graph_to_list.setOnClickListener { v ->
//            run {
//
//                v.findNavController().navigate(R.id.action_graphFragment_to_listFragment)
//
//            }
//        }
    }


    private fun addObservers() {
        graphViewModel.pressList.observe(viewLifecycleOwner, Observer {

            if (it != null) {

                graph.removeAllSeries()
                val newList = shiftTime(it.reversed())
                drawOld(newList, getRainList(newList))
            }

        })

        mainViewModel.lastDayPressures.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                graph.removeAllSeries()
                val newList = shiftTime(it)
                drawOld(newList, getRainList(newList))
            }
        })
    }

    private fun shiftTime(list: List<Step>): List<Step> {
        if (list.size > 0) {
            val initVal = list[0].time
            for (pressure in list) {
                val newPress = pressure.time - initVal
                pressure.time = newPress
            }
        }
        return list
    }


    private fun getRainList(list: List<Step>): List<List<Step>> {
        val rainList = mutableListOf<List<Step>>()
        var powerList = mutableListOf<Step>()

        var pow = 1
        for (i in pow..5) {
            for (pressure in list) {
                if (pressure.rainPower == pow) {
                    powerList.add(pressure)
                }
            }
            rainList.add(powerList)
            powerList = mutableListOf<Step>()
            pow += 1
        }
        return rainList
    }

    private fun removeObservers() {
        graphViewModel.pressList.removeObservers(viewLifecycleOwner)
    }

    /**
     * preparing data for plot
     */
    private fun prepareData(list: List<Step>):  List<List<Step>>{

        val linesList = mutableListOf<List<Step>>()

        var count = 0
        var presuresList = mutableListOf<Step>()
        if (list.size > 0 ) {
            var lastPower = list[0].rainPower
            for (pressure in list) {
                if (pressure.rainPower == lastPower) {
//                val data = DataPoint(pressure.time.toDouble(), pressure.pressure.toDouble())
                    presuresList.add(pressure)
                } else {
                    linesList.add(presuresList)
                    presuresList = mutableListOf<Step>()
                    lastPower = pressure.rainPower
                    presuresList.add(pressure)
                }
                if (presuresList.size > 0) {
                    linesList.add(presuresList)
                }
            }
        }

        return linesList
    }

//    private fun parseAnyData(list: List<Pressure>): List<DataEntry> {
//
//        var outData = mutableListOf<DataEntry>()
//        for(pressure in list) {
////            val entry =
//        }
//    }

    /**
     * checks radio group init value
     */
    private fun checkedradio() {
        radioGroup.check(radioGroup.getChildAt(graphViewModel.lastPress.lastPowr).id)
    }

    /**
     * creating pressue plot using GraphView
     */
    private fun drawLine(array: List<Step>) {
        val data = parseData(array)
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(data)
        series.color = getColor(array[0])
        graph.addSeries(series)
    }

    private fun drawOld(alllist: List<Step>, rainlist: List<List<Step>>) {
        val allpoints = parseDataOld(alllist)

        try{
            val series: LineGraphSeries<DataPoint> = LineGraphSeries(allpoints)
            graph.addSeries(series)
        } catch (e: Exception) {
            Log.d(TAG, "drawOld: " + e)
            Toast.makeText(requireContext(), "Error graph values", Toast.LENGTH_LONG)
                .show()
        }

        if (rainlist.size > 0) {
            for (list in rainlist) {
                if (list.size > 0) {
                    try {
                        val ponts = parseDataOld(list)
                        var pointSeries = PointsGraphSeries<DataPoint>(ponts)
                        pointSeries.size = 5f

                        pointSeries.color = getColor(list[0])
                        graph.addSeries(pointSeries)
                    } catch (e: Exception) {
                        Log.d(TAG, "drawOld: " + e)
                        Toast.makeText(requireContext(), "Error graph values", Toast.LENGTH_LONG)
                                .show()
                    }
                }
            }
            var viewpost = graph.viewport
            viewpost.isScrollable = true
            viewpost.setScalableY(true)
            viewpost.isScalable = true
            viewpost.isXAxisBoundsManual = true
            if (allpoints.size > 0) {
//                viewpost.setMinX(allpoints[0].x)
                viewpost.setMinX(allpoints[allpoints.size - 1].x - 180)
                viewpost.setMaxX(allpoints[allpoints.size - 1].x)
            }
            viewpost.scrollToEnd()
        }

    }

    private fun parseData(list: List<Step>):  Array<DataPoint>{
        var count = 0
        var listM = mutableListOf<DataPoint>()
        if(list.size > 0) {
            for (pressure in list) {
                val timeMin = ((pressure.time)/(1000*60)).toInt()
                val data = DataPoint(timeMin.toDouble(), pressure.pressure.toDouble())
                listM.add(data)
            }
        }
        return listM.toTypedArray()
    }

    private fun parseDataOld(list: List<Step>):  Array<DataPoint>{
        var listM = mutableListOf<DataPoint>()
        if(list.size > 0) {
//            val firstTime = list.get(0).time
            for (pressure in list) {
                val timeMin = ((pressure.time)/(1000*60))
                val data = DataPoint(timeMin.toDouble(), pressure.pressure*0.750064f.toDouble())
                listM.add(data)
            }
        }
        return listM.toTypedArray()
    }

    private fun getNullAltPressure(pressure: Float, altitude: Float): Double {
        val nullPress = pressure/ (Math.pow(10.toDouble(), -0.06*(altitude/1000.0f) ))
        return nullPress;
    }

    private fun getColor(step: Step): Int {
        when(step.rainPower) {
            0 -> return Color.YELLOW
            1 -> return Color.GRAY
            2 -> return Color.CYAN
            3 -> return Color.BLUE
            5 -> return Color.MAGENTA
            6 -> return Color.RED
        }
        return Color.YELLOW
    }

}
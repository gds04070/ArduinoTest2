package com.sopt.arduinotest2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.sopt.arduinotest2.adapter.ChartDataRecyclerViewAdapter
import com.sopt.arduinotest2.db.ChartData
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IndexOutOfBoundsException

class MainActivity : AppCompatActivity() {

    lateinit var ChartDataRecyclerViewAdapter : ChartDataRecyclerViewAdapter
    val dataList: ArrayList<ChartData> by lazy {
        ArrayList<ChartData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecyclerView()

        setOnBtnClickListener()
    }

    private fun setRecyclerView(){
        dataList.add(ChartData(1, "0","0","0", "0","0","0"
        ,"0","0","0","0","0"))
        dataList.add(ChartData(2, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(3, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(4, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(5, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(6, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(7, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(8, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(9, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(10, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        dataList.add(ChartData(11, "0","0","0", "0","0","0"
            ,"0","0","0","0","0"))
        ChartDataRecyclerViewAdapter = ChartDataRecyclerViewAdapter(this,dataList)
        rv_main_chartDataList.adapter = ChartDataRecyclerViewAdapter
        rv_main_chartDataList.layoutManager = LinearLayoutManager(this)
    }

    private fun addItem(){
     //if로 블루투스로 받은데이터가 is not empty면
        val position = ChartDataRecyclerViewAdapter.itemCount
        //ChartDataRecyclerViewAdapter.dataList.add(Chartdata(블루투스로 받은 데이터))
        ChartDataRecyclerViewAdapter.notifyItemInserted(position)
    }

    //버튼 클릭리스너
    private fun setOnBtnClickListener(){
        removeItem()
    }



    private fun removeItem(){
        bt_main_reset.setOnClickListener {
            try{
                dataList.removeAll(dataList)
                ChartDataRecyclerViewAdapter.notifyDataSetChanged()
            }catch (e:IndexOutOfBoundsException){
                Log.e("Index error", e.toString())
            }
        }

    }


}

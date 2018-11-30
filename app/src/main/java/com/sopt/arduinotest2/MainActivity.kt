package com.sopt.arduinotest2

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.sopt.arduinotest2.adapter.ChartDataRecyclerViewAdapter
import com.sopt.arduinotest2.db.ChartData
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.lang.IndexOutOfBoundsException

class MainActivity : AppCompatActivity() {

    private val bt : BluetoothSPP by lazy{
        BluetoothSPP(this)
    }

    lateinit var ChartDataRecyclerViewAdapter : ChartDataRecyclerViewAdapter
    val dataList: ArrayList<ChartData> by lazy {
        ArrayList<ChartData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecyclerView()

        setOnBtnClickListener()

        if(!bt.isBluetoothAvailable){
            Toast.makeText(applicationContext, "Bluetooth is not available", Toast.LENGTH_SHORT).show()
        }

        onBluetooth()

    }

    //블루투스
    private fun onBluetooth(){
        bt.setOnDataReceivedListener(object : BluetoothSPP.OnDataReceivedListener{ //데이터 수신
            override fun onDataReceived(data: ByteArray?, message: String?) {
                Toast.makeText(this@MainActivity,message, Toast.LENGTH_SHORT).show()
                tv_main_receiveText.setText(message) //받아서 리사이클뷰에 additem,
            }
        })

        bt.setBluetoothConnectionListener(object : BluetoothSPP.BluetoothConnectionListener{
            override fun onDeviceConnected(name: String?, address: String?) { //연결되었을때
                applicationContext.toast("Connected to$name\n$address")
            }

            override fun onDeviceDisconnected() { //연결해제
                applicationContext.toast("Connection lost")
            }

            override fun onDeviceConnectionFailed() { //연결실패
                applicationContext.toast("Unable to connect")
            }
        })

        //연결시도
        val btnConnect = findViewById<Button>(R.id.btn_main_btConnect)
        btnConnect.setOnClickListener {
            if(bt.serviceState == BluetoothState.STATE_CONNECTED){
                bt.disconnect()
            }else{
                val intent : Intent = Intent(applicationContext, DeviceList::class.java)
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bt.stopService()
    }

    override fun onStart() {
        super.onStart()
        if(!bt.isBluetoothEnabled){
            val intent : Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT)
        }else{
            if(!bt.isServiceAvailable){
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_OTHER)
                setup()
            }
        }
    }

    private fun setup(){ //데이터 보내는 루트 LED반짝일지 말지
        val btnSend = findViewById<Button>(R.id.btn_main_btSend)
        btnSend.setOnClickListener {
            bt.send("F",true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE){
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data)
        }else if(requestCode==BluetoothState.REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                bt.setupService()
                bt.startService(BluetoothState.DEVICE_OTHER)
                setup()
            }else{
                Toast.makeText(applicationContext, "Bluetooth was not enabled",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //리사이클러뷰


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

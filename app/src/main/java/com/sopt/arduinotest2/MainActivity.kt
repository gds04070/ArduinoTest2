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
import android.widget.ViewAnimator
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import app.akexorcist.bluetotohspp.library.BluetoothState
import app.akexorcist.bluetotohspp.library.DeviceList
import com.sopt.arduinotest2.adapter.ChartDataRecyclerViewAdapter
import com.sopt.arduinotest2.db.ChartData
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.jetbrains.anko.tvInputManager
import java.lang.IndexOutOfBoundsException

class MainActivity : AppCompatActivity() {

    private val bt : BluetoothSPP by lazy{
        BluetoothSPP(this)
    }

    var n : Int = 1


    lateinit var ChartDataRecyclerViewAdapter : ChartDataRecyclerViewAdapter

    val dataList: ArrayList<ChartData> by lazy {
        ArrayList<ChartData>()
    } //리사이클러뷰

    val DataArray: ArrayList<Any> by lazy {
        ArrayList<Any>()
    } //블루투스로 받는 데이터

    val ViewArray: ArrayList<String> by lazy {
        ArrayList<String>()
    } //뷰에 넣을 데이터

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

        var i :Int = 0

        bt.setOnDataReceivedListener(object : BluetoothSPP.OnDataReceivedListener{ //데이터 수신
            override fun onDataReceived(data: ByteArray?, message: String?) {
                Toast.makeText(this@MainActivity,message, Toast.LENGTH_SHORT).show()
                tv_main_receiveText.setText(message) //받아서 리사이클뷰에 additem,

                if(!message.isNullOrEmpty()){
                        DataArray[i] = message!!
                        i = i+1
                }

                if(i >= 10){
                    i = 0

                    var sum : Double = 0.0

                    for(j in 0..9){
                        sum = sum + DataArray[j].toString().toDouble()
                    }

                    DataArray[10] = (sum/10).toString()

                    addItem(n)

                    DataArray.removeAll(DataArray)
                    n = n+1
                }

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
     /*   dataList.add(ChartData(1, "0","0","0", "0","0","0"
        ,"0","0","0","0","0"))

        //addItem()
        ViewArray.add("0")
        ViewArray.add("1")
        ViewArray.add("2")
        ViewArray.add("3")
        ViewArray.add("4")
        ViewArray.add("5")
        ViewArray.add("6")
        ViewArray.add("7")
        ViewArray.add("8")
        ViewArray.add("9")
        ViewArray.add("10")
        val chartData: ChartData = ChartData(0, ViewArray[0], ViewArray[1], ViewArray[2], ViewArray[3], ViewArray[4], ViewArray[5],
            ViewArray[6], ViewArray[7], ViewArray[8], ViewArray[9],ViewArray[10])
        dataList.add(chartData)
        //
*/
        ChartDataRecyclerViewAdapter = ChartDataRecyclerViewAdapter(this,dataList)
        rv_main_chartDataList.adapter = ChartDataRecyclerViewAdapter
        rv_main_chartDataList.layoutManager = LinearLayoutManager(this)
    }

    private fun addItem(n : Int){
     //if로 블루투스로 받은데이터가 is not empty면
        /*ViewArray.add("0")
        ViewArray.add("1")
        ViewArray.add("2")
        ViewArray.add("3")
        ViewArray.add("4")
        ViewArray.add("5")
        ViewArray.add("6")
        ViewArray.add("7")
        ViewArray.add("8")
        ViewArray.add("9")
        ViewArray.add("10")*/
        moveArray()
        val position = ChartDataRecyclerViewAdapter.itemCount

        //ChartDataRecyclerViewAdapter.dataList.add(Chartdata(블루투스로 받은 데이터))
        ChartDataRecyclerViewAdapter.dataList.add(
            ChartData(n, ViewArray[0], ViewArray[1], ViewArray[2], ViewArray[3], ViewArray[4], ViewArray[5], ViewArray[6],
            ViewArray[7], ViewArray[8], ViewArray[9], ViewArray[10])
        )

        ChartDataRecyclerViewAdapter.notifyItemInserted(position)
    }


    private fun moveArray(){//받은거 차트데이터에 연결 dataArray->ViewArray
       /* DataArray.add("0")
        DataArray.add("1")
        DataArray.add("2")
        DataArray.add("3")
        DataArray.add("4")
        DataArray.add("5")
        DataArray.add("6")
        DataArray.add("7")
        DataArray.add("8")
        DataArray.add("9")
        DataArray.add("10")*/
        ViewArray.removeAll(ViewArray)
        ViewArray.add(DataArray[0].toString())
        ViewArray.add(DataArray[1].toString())
        ViewArray.add(DataArray[2].toString())
        ViewArray.add(DataArray[3].toString())
        ViewArray.add(DataArray[4].toString())
        ViewArray.add(DataArray[5].toString())
        ViewArray.add(DataArray[6].toString())
        ViewArray.add(DataArray[7].toString())
        ViewArray.add(DataArray[8].toString())
        ViewArray.add(DataArray[9].toString())
        ViewArray.add(DataArray[10].toString())
    }

    //버튼 클릭리스너
    private fun setOnBtnClickListener(){
        removeItem()
        CalculateExpValue()
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

    private fun CalculateExpValue() {
        bt_main_Calculate.setOnClickListener {
            var Length : String = et_main_lineLength.text.toString()
            var g : Double = 9.8
            if(Length.isNotEmpty()){
                if(Length.toDouble() > 0.0){

                    var expectValue = 2*Math.PI*Math.sqrt(Length.toDouble()/g)
                    tv_main_expectValue.setText(expectValue.toString())

                }else{
                    tv_main_receiveText.setText("유효한 줄의 길이가 아닙니다")
                }
            }
        }
    }
}

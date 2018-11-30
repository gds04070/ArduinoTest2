package com.sopt.arduinotest2.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sopt.arduinotest2.R
import com.sopt.arduinotest2.db.ChartData

class ChartDataRecyclerViewAdapter(val ctx: Context, val dataList : ArrayList<ChartData>) : RecyclerView.Adapter<ChartDataRecyclerViewAdapter.Holder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view : View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_chart_data, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.number.text = dataList[position].number.toString()

        holder.period1.text = dataList[position].period1
        holder.period2.text = dataList[position].period2
        holder.period3.text = dataList[position].period3
        holder.period4.text = dataList[position].period4
        holder.period5.text = dataList[position].period5
        holder.period6.text = dataList[position].period6
        holder.period7.text = dataList[position].period7
        holder.period8.text = dataList[position].period8
        holder.period9.text = dataList[position].period9
        holder.period10.text = dataList[position].period10

        holder.average.text = dataList[position].average
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val number : TextView = itemView.findViewById(R.id.iv_rv_item_chart_number) as TextView

        val period1 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period1) as TextView
        val period2 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period2) as TextView
        val period3 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period3) as TextView
        val period4 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period4) as TextView
        val period5 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period5) as TextView
        val period6 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period6) as TextView
        val period7 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period7) as TextView
        val period8 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period8) as TextView
        val period9 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period9) as TextView
        val period10 : TextView = itemView.findViewById(R.id.iv_rv_item_chart_period10) as TextView

        val average : TextView = itemView.findViewById(R.id.iv_rv_item_chart_average) as TextView
    }

}

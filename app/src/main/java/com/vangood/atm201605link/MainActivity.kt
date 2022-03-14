package com.vangood.atm201605link

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.vangood.atm201605link.databinding.ActivityMainBinding
import com.vangood.atm201605link.databinding.RowDataBinding
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recycler.hasFixedSize()
        //binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager = GridLayoutManager(this,2)
        thread {
            val json = URL("https://atm201605.appspot.com/h").readText()
            val gson = Gson()
            val transactions = gson.fromJson(json, Array<Transation>::class.java).toList()
            transactions.forEach {
                println(it)
            }


            runOnUiThread {
                binding.recycler.adapter = object : RecyclerView.Adapter<TranViewHolder>() {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): TranViewHolder {
                        val binding = RowDataBinding.inflate(layoutInflater, parent, false)
                        return TranViewHolder(binding)
                    }

                    override fun onBindViewHolder(holder: TranViewHolder, position: Int) {
                        val tran = transactions.get(position)
                        holder.account.setText(tran.account.toString())
                        holder.data.setText(tran.date.toString())
                        holder.amount.setText(tran.amount.toString())
                        holder.type.setText(tran.type.toString())
                    }

                    override fun getItemCount(): Int {
                        return transactions.size
                    }

                }
            }
        }


    }
    inner class TranViewHolder(val binding:RowDataBinding):RecyclerView.ViewHolder(binding.root){
        val account  = binding.tnAccount
        val data = binding.tnData
        val amount = binding.tnAmount
        val type = binding.tnType
    }
}
data class Transation(val account :String,val date:String,val amount:Int,val type:Int){

}
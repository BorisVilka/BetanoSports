package com.benatosports.game

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benatosports.game.databinding.BallItemBinding

class MyAdapter(val ctx: Context): RecyclerView.Adapter<MyAdapter.Companion.MyHolder>() {

    var data = mutableListOf<Int>()
    var ind = 0

    init {
        data.add(R.drawable.ball1)
        data.add(R.drawable.ball2)
        data.add(R.drawable.ball3)
        data.add(R.drawable.ball4)
        data.add(R.drawable.ball5)
        data.add(R.drawable.ball6)
    }
    companion object {
        class MyHolder(val binding: BallItemBinding): RecyclerView.ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(BallItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.imageView4.setImageResource(data[position])
        if(ind==position) holder.binding.imageView4.setBackgroundResource(R.drawable.corners)
        else holder.binding.imageView4.setBackgroundResource(android.R.color.transparent)
        holder.binding.root.setOnClickListener {
            ind = position
            ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("ball",data[ind]).apply()
            notifyDataSetChanged()
        }
    }

    override fun onViewRecycled(holder: MyHolder) {
        holder.binding.root.setOnClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return  data.size
    }
}
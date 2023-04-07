package com.benatosports.game

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.benatosports.game.databinding.ScoreItemBinding

class ScoreAdapter(val ctx: Context): RecyclerView.Adapter<ScoreAdapter.Companion.ScoreHolder>() {

    var data = mutableListOf<Int>()

    init {
        data = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getStringSet("score",HashSet<String>())!!.map { it.toInt() }.toMutableList()
        data.sortBy { -it }

    }


    companion object {

        class ScoreHolder(val binding: ScoreItemBinding): RecyclerView.ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreHolder {
        return ScoreHolder(ScoreItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ScoreHolder, position: Int) {
       holder.binding.numberScore.text = "${position+1}"
        holder.binding.countScore.text = "${data[position]}"
    }

    override fun getItemCount(): Int {
       return data.size
    }
}
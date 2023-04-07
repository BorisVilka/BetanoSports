package com.benatosports.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.benatosports.game.databinding.FragmentStartBinding
import kotlin.math.max
import kotlin.math.min


class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    private var ind = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater,container,false)
        binding.left.setOnClickListener {
            ind = max(1,ind-1)
            binding.number.text = "GAME $ind"
            binding.game.setImageResource(if(ind==1) R.drawable.game1 else R.drawable.game2)
        }
        binding.right.setOnClickListener {
            ind = min(2,ind+1)
            binding.number.text = "GAME $ind"
            binding.game.setImageResource(if(ind==1) R.drawable.game1 else R.drawable.game2)
        }
        binding.balls.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.navigate(R.id.ballsFragment)
        }
        binding.top.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.navigate(R.id.fragmentScore)
        }
        binding.play.setOnClickListener {
           if(ind==1) {
               val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
               navController.navigate(R.id.firstGameFragment)
           } else {
               val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
               navController.navigate(R.id.secondGameFragment)
           }
        }
        var music = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",true)
        binding.music.setBackgroundResource(if(music) R.drawable.off else R.drawable.on)
        binding.music.setOnClickListener {
            music = !music
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putBoolean("music",music).apply()
            binding.music.setBackgroundResource(if(music) R.drawable.off else R.drawable.on)
        }

        var sounds = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("sounds",true)
        binding.volume.setBackgroundResource(if(sounds) R.drawable.off else R.drawable.on)
        binding.volume.setOnClickListener {
            sounds = !sounds
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putBoolean("sounds",sounds).apply()
            binding.volume.setBackgroundResource(if(sounds) R.drawable.off else R.drawable.on)
        }
        return binding.root
    }


}
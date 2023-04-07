package com.benatosports.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.benatosports.game.databinding.FragmentEndBinding


class EndFragment : Fragment() {


    private lateinit var binding: FragmentEndBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEndBinding.inflate(inflater,container,false)
        binding.imageView11.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.play.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            navController.navigate(R.id.fragmentScore)
        }
        binding.top.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            if(requireArguments().getString("game")=="1") navController.navigate(R.id.firstGameFragment) else navController.navigate(R.id.secondGameFragment)
        }
        binding.textView7.text = "${requireArguments().getString("score")}"
        binding.number.text = "GAME ${requireArguments().getString("game")}"
        binding.game.setImageResource(if(requireArguments().getString("game")=="1") R.drawable.game1 else R.drawable.game2)
        return binding.root
    }


}
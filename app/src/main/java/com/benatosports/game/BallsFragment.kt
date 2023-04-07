package com.benatosports.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.benatosports.game.databinding.FragmentBallsBinding

class BallsFragment : Fragment() {


    private lateinit var binding: FragmentBallsBinding
    private lateinit var adapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBallsBinding.inflate(inflater,container,false)
        adapter = MyAdapter(requireContext())
        binding.list.adapter = adapter
        binding.textView5.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.imageView2.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        return binding.root
    }


}
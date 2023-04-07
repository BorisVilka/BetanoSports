package com.benatosports.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.benatosports.game.databinding.FragmentFirstGameBinding


class FirstGameFragment : Fragment() {


    private lateinit var binding: FragmentFirstGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFirstGameBinding.inflate(inflater,container,false)
        binding.game.setEndListener(object : FirstGameView.Companion.EndListener {
            override fun end() {
                val set = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getStringSet("score",HashSet<String>())
                var set1 = HashSet<String>().apply {
                    addAll(set!!)
                    if(!contains(binding.game.score.toString())) add(binding.game.score.toString())
                }
                requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putStringSet("score",set1).apply()
                requireActivity().runOnUiThread {
                    val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    navController.popBackStack()
                    navController.navigate(R.id.endFragment, Bundle().apply {
                        putString("score",binding.game.score.toString())
                        putString("game","1")
                    })
                }
            }

            override fun score(score: Int) {

            }

        })
        binding.imageView8.setOnClickListener {
            binding.game.togglePause()
            var dialog = PauseDialog(
                {
                    binding.game.togglePause()
                },{
                    val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    navController.popBackStack()
                    navController.navigate(R.id.firstGameFragment)
                },{
                    val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    navController.popBackStack()
                }
            )
            dialog.show(requireActivity().supportFragmentManager,"TAG")
        }
        return binding.root
    }


}
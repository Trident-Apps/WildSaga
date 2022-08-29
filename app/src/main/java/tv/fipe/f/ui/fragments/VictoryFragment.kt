package tv.fipe.f.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tv.fipe.f.R
import tv.fipe.f.databinding.VictoryFragmentBinding

class VictoryFragment : Fragment() {
    private var _binding: VictoryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = VictoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.victoryBtn.setOnClickListener {
            findNavController().navigate(R.id.gameFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
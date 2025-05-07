package com.example.playlistmaker.medialibrary.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.utils.AppPreferencesKeys.FAVORITES_EMPTY
import com.example.playlistmaker.utils.AppPreferencesKeys.INTERNET_EMPTY
import com.example.playlistmaker.utils.AppPreferencesKeys.LOADING
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.utils.ErrorUtils.ifFragmentErrorShowPlug

class MLFavoritesFragment : Fragment() {

    private val viewModel: MLFavoritesViewModel by viewModel()
    private lateinit var binding: FragmentFavoritesBinding

    companion object {
        fun newInstance() = MLFavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadFromHistory()
    }

    private fun setupObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is MLFavoritesScreenState.Ready -> {
                    ifFragmentErrorShowPlug(requireContext(), FAVORITES_EMPTY)
                }
                MLFavoritesScreenState.Error -> {
                    ifFragmentErrorShowPlug(requireContext(), INTERNET_EMPTY)
                }
                MLFavoritesScreenState.Loading -> {
                    ifFragmentErrorShowPlug(requireContext(), LOADING)
                }
            }
        }
    }
}
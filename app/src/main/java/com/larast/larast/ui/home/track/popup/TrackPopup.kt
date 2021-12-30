package com.larast.larast.ui.home.track.popup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.larast.larast.data.helper.createQrCode
import com.larast.larast.data.helper.downloadPdf
import com.larast.larast.data.helper.handleApiError
import com.larast.larast.data.helper.toast
import com.larast.larast.data.network.Resource
import com.larast.larast.databinding.PopupTrackBinding
import com.larast.larast.ui.home.track.TrackViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackPopup : DialogFragment() {

    private val args by navArgs<TrackPopupArgs>()
    private val viewModel by viewModels<TrackViewModel>()
    private lateinit var binding: PopupTrackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopupTrackBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() {
        viewModel.stateResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        findNavController().navigate(TrackPopupDirections.actionTrackPopupToTrackFragment())
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Failure -> handleApiError(it) {
                    state()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        ivQr.createQrCode(args.track.kode)
        launchState()
    }

    private fun launchState() = with(binding) {
        if (args.track.berkas == "1") {
            btnQR.setOnClickListener {
                downloadPdf(args.track.pdf)
                findNavController().navigate(TrackPopupDirections.actionTrackPopupToTrackFragment())
            }
        } else {
            if (args.track.status == "6") {
                btnQR.text = "Perbaikan Selesai"
                btnQR.setOnClickListener {
                    state()
                }
            } else {
                btnQR.text = "Berkas Online"
                btnQR.setOnClickListener {
                    findNavController().navigate(
                        TrackPopupDirections.actionTrackPopupToFilesFragment(
                            args.track.pemohon
                        )
                    )
                }
            }
        }
    }

    private fun state() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.state(
                "Bearer $it",
                args.track.id_pengantar
            )
        })
    }

}
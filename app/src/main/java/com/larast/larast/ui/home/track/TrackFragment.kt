package com.larast.larast.ui.home.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.larast.larast.data.helper.LoadAdapter
import com.larast.larast.data.helper.toast
import com.larast.larast.data.helper.visible
import com.larast.larast.data.responses.track.Track
import com.larast.larast.databinding.FragmentTrackBinding
import com.larast.larast.ui.home.track.adapter.TrackAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackFragment : Fragment(), TrackAdapter.OnItemClickListener {

    private val viewModel by viewModels<TrackViewModel>()
    private lateinit var binding: FragmentTrackBinding

    private val trackAdapter = TrackAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        track()

        rvTrack.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter.withLoadStateHeaderAndFooter(
                header = LoadAdapter { trackAdapter.retry() },
                footer = LoadAdapter { trackAdapter.retry() },
            )
        }

        trackAdapter.addLoadStateListener {
            progressBar.visible(it.source.refresh is LoadState.Loading)
            ivRetry.visible(it.source.refresh is LoadState.Error)

            val errorState = it.source.append as? LoadState.Error
                ?: it.source.prepend as? LoadState.Error
                ?: it.append as? LoadState.Error
                ?: it.prepend as? LoadState.Error
            errorState?.let { state ->
                requireContext().toast("\uD83D\uDE28 Wooops ${state.error}")
            }
        }
        ivRetry.setOnClickListener {
            trackAdapter.retry()
        }
    }

    private fun track() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.track(
                    "Bearer $it"
                ).collectLatest {
                    trackAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(track: Track) {
        if ((track.kode != null) && (track.status == "2") || (track.status == "6")) {
            findNavController().navigate(
                TrackFragmentDirections.actionTrackFragmentToTrackPopup(
                    track
                )
            )
        }
    }

}
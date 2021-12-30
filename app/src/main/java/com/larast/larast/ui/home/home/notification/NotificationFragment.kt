package com.larast.larast.ui.home.home.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.larast.larast.data.helper.LoadAdapter
import com.larast.larast.data.helper.handleApiError
import com.larast.larast.data.helper.toast
import com.larast.larast.data.helper.visible
import com.larast.larast.data.network.Resource
import com.larast.larast.databinding.FragmentNotificationBinding
import com.larast.larast.ui.home.home.notification.adapter.NotificationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var binding: FragmentNotificationBinding

    private val notificationAdapter = NotificationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() {
        viewModel.notificationResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        Log.i("TAG", "setupObserver: Success")
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Dismiss")
                }
                is Resource.Failure -> handleApiError(it) {
                    read()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        notification()

        read()

        rvNotification.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = notificationAdapter.withLoadStateFooter(
                footer = LoadAdapter { notificationAdapter.retry() },
            )
        }
        notificationAdapter.addLoadStateListener {
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
            notificationAdapter.retry()
        }
    }

    private fun notification() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.notification(
                    "Bearer $it"
                ).collectLatest {
                    notificationAdapter.submitData(it)
                }
            }
        })
    }

    private fun read() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.read(
                    "Bearer $it"
                )
            }
        })
    }

}
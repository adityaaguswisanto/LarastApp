package com.larast.larast.ui.home.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.larast.larast.R
import com.larast.larast.data.helper.LoadAdapter
import com.larast.larast.data.helper.handleApiError
import com.larast.larast.data.helper.toast
import com.larast.larast.data.helper.visible
import com.larast.larast.data.network.Resource
import com.larast.larast.data.responses.home.News
import com.larast.larast.databinding.FragmentHomeBinding
import com.larast.larast.ui.home.home.adapter.EventAdapter
import com.larast.larast.ui.home.home.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), NewsAdapter.OnItemClickListener {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private val newsAdapter = NewsAdapter(this)
    private val eventAdapter = EventAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.notificationResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        when {
                            it.value.data.isEmpty() -> {
                                Log.i("TAG", "setupObserver: null")
                            }
                            it.value.data[0].status == "1" -> {
                                ivNotification.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        root.context,
                                        R.drawable.ic_notif_on
                                    )
                                )
                            }
                            else -> {
                                ivNotification.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        root.context,
                                        R.drawable.ic_notif
                                    )
                                )
                            }
                        }
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Dismiss")
                }
                is Resource.Failure -> handleApiError(it) {
                    notification()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        notification()

        rbNews.isChecked = true
        rbNews.setTextColor(Color.WHITE)
        rbEvent.setTextColor(Color.GRAY)

        newsUI()
        news()

        ivNotification.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment())
        }

        cvSearch.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTrackFragment())
        }

        rbNews.setOnClickListener {
            rbNews.isChecked = true
            rbNews.setTextColor(Color.WHITE)
            rbEvent.setTextColor(Color.GRAY)
            newsUI()
            news()
        }

        rbEvent.setOnClickListener {
            rbEvent.isChecked = true
            rbEvent.setTextColor(Color.WHITE)
            rbNews.setTextColor(Color.GRAY)
            eventUI()
            event()
        }
    }

    private fun newsUI() = with(binding) {
        rvHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoadAdapter { newsAdapter.retry() },
            )
        }
        newsAdapter.addLoadStateListener {
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
            newsAdapter.retry()
        }
    }

    private fun eventUI() = with(binding) {
        rvHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = eventAdapter.withLoadStateFooter(
                footer = LoadAdapter { eventAdapter.retry() },
            )
        }
        eventAdapter.addLoadStateListener {
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
            eventAdapter.retry()
        }
    }

    private fun news() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.news(
                    "Bearer $it"
                ).collectLatest {
                    newsAdapter.submitData(it)
                }
            }
        })
    }

    private fun event() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.event(
                    "Bearer $it"
                ).collectLatest {
                    eventAdapter.submitData(it)
                }
            }
        })
    }

    private fun notification() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.notification(
                "Bearer $it",
                1
            )
        })
    }

    override fun onItemClick(news: News) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewsFragment())
    }

}
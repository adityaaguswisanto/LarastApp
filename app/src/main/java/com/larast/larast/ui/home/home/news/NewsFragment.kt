package com.larast.larast.ui.home.home.news

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
import com.larast.larast.data.responses.home.News
import com.larast.larast.databinding.FragmentNewsBinding
import com.larast.larast.ui.home.home.HomeViewModel
import com.larast.larast.ui.home.home.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment(), NewsAdapter.OnItemClickListener {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentNewsBinding

    private val newsAdapter = NewsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {

        news()

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

    override fun onItemClick(news: News) {
        findNavController().navigate(NewsFragmentDirections.actionNewsFragmentToDetailsNewsFragment(
            news
        ))
    }

}
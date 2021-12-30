package com.larast.larast.ui.home.family

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.larast.larast.data.helper.LoadAdapter
import com.larast.larast.data.helper.toast
import com.larast.larast.data.helper.visible
import com.larast.larast.data.responses.family.Family
import com.larast.larast.databinding.FragmentFamilyBinding
import com.larast.larast.ui.home.family.adapter.FamilyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FamilyFragment : Fragment(), FamilyAdapter.OnItemClickListener {

    private val viewModel by viewModels<FamilyViewModel>()
    private lateinit var binding: FragmentFamilyBinding

    private val familyAdapter = FamilyAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFamilyBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {

        family()

        rvFamily.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = familyAdapter.withLoadStateFooter(
                footer = LoadAdapter { familyAdapter.retry() },
            )
        }

        familyAdapter.addLoadStateListener {
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
            familyAdapter.retry()
        }
    }

    private fun family(){
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.family(
                    "Bearer $it"
                ).collectLatest {
                    familyAdapter.submitData(it)
                }
            }
        })
    }

    override fun onItemClick(family: Family) {
        findNavController().navigate(FamilyFragmentDirections.actionFamilyFragmentToFilesFragment(
            family
        ))
    }

}
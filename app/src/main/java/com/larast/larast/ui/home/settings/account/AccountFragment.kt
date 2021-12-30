package com.larast.larast.ui.home.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.larast.larast.data.helper.*
import com.larast.larast.data.network.Resource
import com.larast.larast.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private val args by navArgs<AccountFragmentArgs>()
    private val viewModel by viewModels<AccountViewModel>()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.userResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    ivSubmit.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    ivSubmit.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    update()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        edtName.setText(args.user.nama)
        edtEmail.setText(args.user.email)
        edtHp.setText(args.user.hp)

        ivSubmit.setOnClickListener {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin update biodata",
                "Ya",
                "Tidak"
            ) { _, _ ->
                update()
            }
        }
    }

    private fun update() = with(binding) {
        val name = edtName.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val hp = edtHp.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || hp.isEmpty()) {
            requireContext().toast("Field masih kosong !")
        } else {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin update biodata",
                "Ya",
                "Tidak"
            ) { _, _ ->
                viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                    viewModel.update(
                        "Bearer $it",
                        name,
                        email,
                        hp
                    )
                })
            }
        }

    }

}
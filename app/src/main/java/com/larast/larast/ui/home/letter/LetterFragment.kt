package com.larast.larast.ui.home.letter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.larast.larast.data.helper.*
import com.larast.larast.data.network.Resource
import com.larast.larast.data.responses.family.Family
import com.larast.larast.data.responses.letter.Pelayanan
import com.larast.larast.databinding.FragmentLetterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LetterFragment : Fragment() {

    private val viewModel by viewModels<LetterViewModel>()
    private lateinit var binding: FragmentLetterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLetterBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.citizensResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        citizens(it.value.pemohon)
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
                    citizens()
                }
            }
        })
        viewModel.servicesResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        services(it.value.pelayanan)
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
                    services()
                }
            }
        })
        viewModel.letterResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                        findNavController().navigate(LetterFragmentDirections.actionLetterFragmentToTrackFragment())
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
                    userLetter()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        citizens()
        services()

        rdOffline.isChecked = true
        file = "1"

        ivSubmit.setOnClickListener {
            userLetter()
        }

        rdOffline.setOnClickListener {
            rdOffline.isChecked = true
            file = "1"
        }
        rdOnline.setOnClickListener {
            rdOnline.isChecked = true
            file = "2"
        }
    }

    private fun userLetter() = with(binding) {
        val hp = edtHp.text.toString().trim()
        val desc = edtDesc.text.toString().trim()

        if (hp.isEmpty() || desc.isEmpty()) {
            requireContext().toast("Isi field secara lengkap !")
        } else {
            requireContext().alertDialog(
                "Tekan Ya jika anda ingin mengajukan surat pengantar",
                "Ya",
                "Tidak"
            ) { _, _ ->
                viewModel.user().asLiveData().observe(viewLifecycleOwner, {
                    viewModel.letter(
                        "Bearer $it",
                        idCitizens,
                        idService,
                        hp,
                        desc,
                        file
                    )
                })
            }
        }
    }

    private fun citizens() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.citizens(
                "Bearer $it"
            )
        })
    }

    private fun services() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.services(
                "Bearer $it"
            )
        })
    }

    private fun services(data: List<Pelayanan>) = with(binding) {
        val listData = ArrayList<String>()
        for (i in data.indices) {
            listData.add(data[i].pelayanan)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listData
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnService.adapter = adapter

        spnService.onItemSelectedListener = object : Spinner() {
            override fun getData(p2: Int) {
                idService = data[p2].id_pelayanan
            }
        }
    }

    private fun citizens(data: List<Family>) = with(binding) {
        val listData = ArrayList<String>()
        for (i in data.indices) {
            listData.add(data[i].nama)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listData
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCitizens.adapter = adapter

        spnCitizens.onItemSelectedListener = object : Spinner() {
            override fun getData(p2: Int) {
                idCitizens = data[p2].id_penduduk
            }
        }
    }

    companion object {
        private var idCitizens = 0
        private var idService = 0
        private lateinit var file: String
    }

}
package com.larast.larast.ui.home.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.larast.larast.R
import com.larast.larast.data.helper.*
import com.larast.larast.data.network.Resource
import com.larast.larast.data.responses.auth.User
import com.larast.larast.databinding.FragmentSettingsBinding
import com.larast.larast.ui.auth.AuthActivity
import com.larast.larast.ui.home.settings.adapter.SettingsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class SettingsFragment : Fragment(), UploadRequestBody.UploadCallback {

    private val viewModel by viewModels<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    private lateinit var settingsAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.userResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        user = it.value.user
                        if (it.value.user.foto == null)
                            ivProfile.setImageResource(R.drawable.ic_logo)
                        else
                            ivProfile.loadImage("${urlAssets()}${it.value.user.foto}")
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                }
                is Resource.Failure -> handleApiError(it) {
                    user()
                }
            }
        })
        viewModel.profileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        requireContext().toast(it.value.message)
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                }
                is Resource.Failure -> handleApiError(it) {
                    profile(profileUri)
                }
            }
        })
        viewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.clear()
                        requireActivity().launchActivity(AuthActivity::class.java)
                        requireContext().toast(it.value.message)
                    }
                }
                is Resource.Loading -> {
                    Log.i("TAG", "setupObserver: Loading")
                }
                is Resource.Dismiss -> {
                    Log.i("TAG", "setupObserver: Dismiss")
                }
                is Resource.Failure -> handleApiError(it) {
                    logout()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {

        user()

        settingsAdapter = SettingsAdapter {
            when (it) {
                "profile" -> {
                    findNavController().navigate(
                        SettingsFragmentDirections.actionSettingsFragmentToAccountFragment(
                            user
                        )
                    )
                }
                "privacy" -> {
                    val url = Intent(Intent.ACTION_VIEW)
                    url.data =
                        Uri.parse("https://www.privacypolicyonline.com/live.php?token=m5NsInTaUw9H49atybcMHupyTEXKyNWk")
                    startActivity(url)
                }
                "logout" -> {
                    requireContext().alertDialog(
                        "Tekan Ya jika anda ingin logout",
                        "Ya",
                        "Tidak"
                    ) { _, _ ->
                        logout()
                    }
                }
            }
        }
        rvSettings.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext())
            adapter = settingsAdapter
        }

        txtPickImage.setOnClickListener {
            pickProfile.launch("image/*")
        }
    }

    private val pickProfile = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            profileUri = it
            binding.ivProfile.loadImage(profileUri.toString())
            profile(profileUri)
        }
    }

    private fun profile(profileUri: Uri?) {
        val profileDescriptor =
            requireContext().contentResolver.openFileDescriptor(profileUri!!, "r", null)
        val inputStreamProfile = FileInputStream(profileDescriptor?.fileDescriptor)
        val fileProfile = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(profileUri)
        )
        val outputStreamProfile = FileOutputStream(fileProfile)
        inputStreamProfile.copyTo(outputStreamProfile)

        val bodyProfile = UploadRequestBody(fileProfile, "image", this@SettingsFragment)
        val profile = MultipartBody.Part.createFormData("foto", fileProfile.name, bodyProfile)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.profile(
                "Bearer $it",
                profile
            )
        })
    }

    private fun user() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.user(
                "Bearer $it"
            )
        })
    }

    private fun logout() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.logout(
                "Bearer $it"
            )
        })
    }

    companion object {
        private var profileUri: Uri? = null
        private lateinit var user: User
    }

    override fun onProgressUpdate(percentage: Int) = with(binding) {
        progressBar.progress = percentage
    }

}
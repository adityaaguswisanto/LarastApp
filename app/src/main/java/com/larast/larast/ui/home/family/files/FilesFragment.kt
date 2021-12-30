package com.larast.larast.ui.home.family.files

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.larast.larast.data.helper.*
import com.larast.larast.data.network.Resource
import com.larast.larast.data.responses.files.Berkas
import com.larast.larast.databinding.FragmentFilesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class FilesFragment : Fragment(), UploadRequestBody.UploadCallback {

    private val args by navArgs<FilesFragmentArgs>()
    private val viewModel by viewModels<FilesViewModel>()
    private lateinit var binding: FragmentFilesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding){
        viewModel.filesResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        setImageUrl(it.value.berkas)
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                }
                is Resource.Failure -> handleApiError(it) {
                    files()
                }
            }
        })
        viewModel.fileResponse.observe(viewLifecycleOwner, {
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
                    kk(kkUri)
                    akte(akteUri)
                    ktps(ktpsUri)
                    ktpi(ktpiUri)
                    nikah(nikahUri)
                    ijazah(ijazahUri)
                    skl(sklUri)
                    agama(agamaUri)
                }
            }
        })
    }

    private fun setImageUrl(berkas: Berkas) = with(binding) {
        ivKk.loadImage("${urlAssets()}${berkas.kk}")
        ivAkte.loadImage("${urlAssets()}${berkas.akte}")
        ivKtpS.loadImage("${urlAssets()}${berkas.ktps}")
        ivKtpI.loadImage("${urlAssets()}${berkas.ktpi}")
        ivMarried.loadImage("${urlAssets()}${berkas.nikah}")
        ivEdu.loadImage("${urlAssets()}${berkas.ijazah}")
        ivSkl.loadImage("${urlAssets()}${berkas.skl}")
        ivReligion.loadImage("${urlAssets()}${berkas.agama}")
    }

    private fun updateUI() = with(binding) {
        files()

        permissionExternalStorage().launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        ivKk.setOnClickListener {
            pickKK.launch("image/*")
        }
        ivAkte.setOnClickListener {
            pickAkte.launch("image/*")
        }
        ivKtpS.setOnClickListener {
            pickKtpS.launch("image/*")
        }
        ivKtpI.setOnClickListener {
            pickKtpI.launch("image/*")
        }
        ivMarried.setOnClickListener {
            pickMarried.launch("image/*")
        }
        ivEdu.setOnClickListener {
            pickEdu.launch("image/*")
        }
        ivSkl.setOnClickListener {
            pickSkl.launch("image/*")
        }
        ivReligion.setOnClickListener {
            pickReligion.launch("image/*")
        }
    }

    private fun files() {
        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.files(
                "Bearer $it",
                args.family.id_penduduk,
            )
        })
    }

    private val pickKK = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            kkUri = it
            binding.ivKk.loadImage(kkUri.toString())
            kk(kkUri)
        }
    }

    private val pickAkte = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            akteUri = it
            binding.ivAkte.loadImage(akteUri.toString())
            akte(akteUri)
        }
    }

    private val pickKtpS = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            ktpsUri = it
            binding.ivKtpS.loadImage(ktpsUri.toString())
            ktps(ktpsUri)
        }
    }

    private val pickKtpI = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            ktpiUri = it
            binding.ivKtpI.loadImage(ktpiUri.toString())
            ktpi(ktpiUri)
        }
    }

    private val pickMarried = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            nikahUri = it
            binding.ivMarried.loadImage(nikahUri.toString())
            nikah(nikahUri)
        }
    }

    private val pickEdu = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            ijazahUri = it
            binding.ivEdu.loadImage(ijazahUri.toString())
            ijazah(ijazahUri)
        }
    }

    private val pickSkl = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            sklUri = it
            binding.ivSkl.loadImage(sklUri.toString())
            skl(sklUri)
        }
    }

    private val pickReligion = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        it?.let {
            agamaUri = it
            binding.ivReligion.loadImage(agamaUri.toString())
            agama(agamaUri)
        }
    }

    private fun kk(kkUri: Uri?) {
        val kkDescriptor =
            requireContext().contentResolver.openFileDescriptor(kkUri!!, "r", null)
        val inputStreamKK = FileInputStream(kkDescriptor?.fileDescriptor)
        val fileKK = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(kkUri)
        )
        val outputStreamKK = FileOutputStream(fileKK)
        inputStreamKK.copyTo(outputStreamKK)

        val bodyKK = UploadRequestBody(fileKK, "image", this@FilesFragment)
        val kk = MultipartBody.Part.createFormData("kk", fileKK.name, bodyKK)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.kk(
                "Bearer $it",
                args.family.id_penduduk,
                kk
            )
        })
    }

    private fun akte(akteUri: Uri?) {
        val akteDescriptor =
            requireContext().contentResolver.openFileDescriptor(akteUri!!, "r", null)
        val inputStreamAkte = FileInputStream(akteDescriptor?.fileDescriptor)
        val fileAkte = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(akteUri)
        )
        val outputStreamAkte = FileOutputStream(fileAkte)
        inputStreamAkte.copyTo(outputStreamAkte)

        val bodyAkte = UploadRequestBody(fileAkte, "image", this@FilesFragment)
        val akte = MultipartBody.Part.createFormData("akte", fileAkte.name, bodyAkte)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.akte(
                "Bearer $it",
                args.family.id_penduduk,
                akte
            )
        })
    }

    private fun ktps(ktpsUri: Uri?) {
        val ktpsDescriptor =
            requireContext().contentResolver.openFileDescriptor(ktpsUri!!, "r", null)
        val inputStreamKtps = FileInputStream(ktpsDescriptor?.fileDescriptor)
        val fileKtps = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(ktpsUri)
        )
        val outputStreamKtps = FileOutputStream(fileKtps)
        inputStreamKtps.copyTo(outputStreamKtps)

        val bodyKtps = UploadRequestBody(fileKtps, "image", this@FilesFragment)
        val ktps = MultipartBody.Part.createFormData("ktps", fileKtps.name, bodyKtps)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.ktps(
                "Bearer $it",
                args.family.id_penduduk,
                ktps
            )
        })
    }

    private fun ktpi(ktpiUri: Uri?) {
        val ktpiDescriptor =
            requireContext().contentResolver.openFileDescriptor(ktpiUri!!, "r", null)
        val inputStreamKtpi = FileInputStream(ktpiDescriptor?.fileDescriptor)
        val fileKtpi = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(ktpiUri)
        )
        val outputStreamKtpi = FileOutputStream(fileKtpi)
        inputStreamKtpi.copyTo(outputStreamKtpi)

        val bodyKtpi = UploadRequestBody(fileKtpi, "image", this@FilesFragment)
        val ktpi = MultipartBody.Part.createFormData("ktpi", fileKtpi.name, bodyKtpi)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.ktpi(
                "Bearer $it",
                args.family.id_penduduk,
                ktpi
            )
        })
    }

    private fun nikah(nikahUri: Uri?) {
        val nikahDescriptor =
            requireContext().contentResolver.openFileDescriptor(nikahUri!!, "r", null)
        val inputStreamNikah = FileInputStream(nikahDescriptor?.fileDescriptor)
        val fileNikah = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(nikahUri)
        )
        val outputStreamNikah = FileOutputStream(fileNikah)
        inputStreamNikah.copyTo(outputStreamNikah)

        val bodyNikah = UploadRequestBody(fileNikah, "image", this@FilesFragment)
        val nikah = MultipartBody.Part.createFormData("nikah", fileNikah.name, bodyNikah)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.nikah(
                "Bearer $it",
                args.family.id_penduduk,
                nikah
            )
        })
    }

    private fun ijazah(ijazahUri: Uri?) {
        val ijazahDescriptor =
            requireContext().contentResolver.openFileDescriptor(ijazahUri!!, "r", null)
        val inputStreamIjazah = FileInputStream(ijazahDescriptor?.fileDescriptor)
        val fileIjazah = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(ijazahUri)
        )
        val outputStreamIjazah = FileOutputStream(fileIjazah)
        inputStreamIjazah.copyTo(outputStreamIjazah)

        val bodyIjazah = UploadRequestBody(fileIjazah, "image", this@FilesFragment)
        val ijazah = MultipartBody.Part.createFormData("ijazah", fileIjazah.name, bodyIjazah)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.ijazah(
                "Bearer $it",
                args.family.id_penduduk,
                ijazah
            )
        })
    }

    private fun skl(sklUri: Uri?) {
        val sklDescriptor =
            requireContext().contentResolver.openFileDescriptor(sklUri!!, "r", null)
        val inputStreamSkl = FileInputStream(sklDescriptor?.fileDescriptor)
        val fileSkl = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(sklUri)
        )
        val outputStreamSkl = FileOutputStream(fileSkl)
        inputStreamSkl.copyTo(outputStreamSkl)

        val bodySkl = UploadRequestBody(fileSkl, "image", this@FilesFragment)
        val skl = MultipartBody.Part.createFormData("skl", fileSkl.name, bodySkl)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.skl(
                "Bearer $it",
                args.family.id_penduduk,
                skl
            )
        })
    }

    private fun agama(agamaUri: Uri?) {
        val agamaDescriptor =
            requireContext().contentResolver.openFileDescriptor(agamaUri!!, "r", null)
        val inputStreamAgama = FileInputStream(agamaDescriptor?.fileDescriptor)
        val fileAgama = File(
            requireContext().cacheDir,
            requireContext().contentResolver.getFileName(agamaUri)
        )
        val outputStreamAgama = FileOutputStream(fileAgama)
        inputStreamAgama.copyTo(outputStreamAgama)

        val bodyAgama = UploadRequestBody(fileAgama, "image", this@FilesFragment)
        val agama = MultipartBody.Part.createFormData("agama", fileAgama.name, bodyAgama)

        viewModel.user().asLiveData().observe(viewLifecycleOwner, {
            viewModel.agama(
                "Bearer $it",
                args.family.id_penduduk,
                agama
            )
        })
    }

    companion object {
        private var kkUri: Uri? = null
        private var akteUri: Uri? = null
        private var ktpsUri: Uri? = null
        private var ktpiUri: Uri? = null
        private var nikahUri: Uri? = null
        private var ijazahUri: Uri? = null
        private var sklUri: Uri? = null
        private var agamaUri: Uri? = null
    }

    override fun onProgressUpdate(percentage: Int) = with(binding) {
        progressBar.progress = percentage
    }

}
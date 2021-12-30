package com.larast.larast.ui.home.home.news.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.larast.larast.data.helper.formatDate
import com.larast.larast.data.helper.loadImage
import com.larast.larast.data.helper.urlAssets
import com.larast.larast.databinding.FragmentDetailsNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsNewsFragment : Fragment() {

    private val args by navArgs<DetailsNewsFragmentArgs>()
    private lateinit var binding: FragmentDetailsNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsNewsBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }

    private fun updateUI() = with(binding) {
        toolbar.title = args.news.judul
        ivImage.loadImage("${urlAssets()}${args.news.foto}")
        txtDateAuthor.text = "${formatDate(args.news.created_at)} - Author : Kecamatan"
        txtContent.text = args.news.keterangan
    }

}
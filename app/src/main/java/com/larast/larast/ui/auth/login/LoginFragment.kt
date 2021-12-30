package com.larast.larast.ui.auth.login

import android.content.ContentValues
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.larast.larast.data.helper.*
import com.larast.larast.data.network.Resource
import com.larast.larast.databinding.FragmentLoginBinding
import com.larast.larast.ui.auth.AuthViewModel
import com.larast.larast.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupObserver()
        updateUI()
        return binding.root
    }

    private fun setupObserver() = with(binding) {
        viewModel.loginResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveToken(it.value.token)
                        requireActivity().launchActivity(HomeActivity::class.java)
                    }
                }
                is Resource.Loading -> {
                    progressBar.visible()
                    btnLogin.invisible()
                }
                is Resource.Dismiss -> {
                    progressBar.invisible()
                    btnLogin.visible()
                }
                is Resource.Failure -> handleApiError(it) {
                    login()
                }
            }
        })
    }

    private fun updateUI() = with(binding) {
        cbPassword.setOnClickListener {
            if (cbPassword.isChecked) edtPassword.transformationMethod =
                HideReturnsTransformationMethod.getInstance() else edtPassword.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        txtForgot.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotFragment())
        }
        edtPassword.addTextChangedListener {
            val email = edtEmail.text.toString().trim()
            btnLogin.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }
        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() = with(binding) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val fcm = task.result
            Log.e(ContentValues.TAG, "token fcm: $fcm")

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            viewModel.login(email, password, fcm!!)
        })

    }

}
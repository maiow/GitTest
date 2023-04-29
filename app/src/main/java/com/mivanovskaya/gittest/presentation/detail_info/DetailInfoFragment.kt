package com.mivanovskaya.gittest.presentation.detail_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mivanovskaya.gittest.databinding.FragmentAuthBinding
import com.mivanovskaya.gittest.databinding.FragmentDetailInfoBinding
import com.mivanovskaya.gittest.presentation.auth.AuthViewModel
import com.mivanovskaya.gittest.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment : BaseFragment<FragmentDetailInfoBinding>() {

    override fun initBinding(inflater: LayoutInflater) = FragmentDetailInfoBinding.inflate(inflater)
    private val viewModel by viewModels<RepositoryInfoViewModel>()
    private val args by navArgs<DetailInfoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

           binding.testText.text = args.repoId
    }

}
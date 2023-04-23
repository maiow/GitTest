package com.mivanovskaya.gittest.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mivanovskaya.gittest.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    private var tokenInputByUser = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setEditText()
        setSignButton()
    }

//    private fun setEditText() {
//        tokenInputByUser = binding.editText.text.toString()
//        setOnTextListener(object : SearchView.OnQueryTextListener,
//            android.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                if (query.isNotEmpty()) viewModel.onSearchButtonClick(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//    }

    private fun setSignButton() {
        binding.signButton.setOnClickListener {
            Log.i("BRED", "edit text is ${binding.editText.text}")
            viewModel.onSignButtonPressed(binding.editText.text.toString())
         //   findNavController().navigate(R.id.action_authFragment_to_repositoriesListFragment)
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_CALL))
//            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object{
        const val AUTH_CALL = ""
    }
}
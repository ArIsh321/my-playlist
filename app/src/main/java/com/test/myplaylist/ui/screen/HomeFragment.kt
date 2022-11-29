package com.test.myplaylist.ui.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import com.test.myplaylist.base.BaseActivity
import com.test.myplaylist.base.BaseFragment
import com.test.myplaylist.databinding.ActivityMainBinding
import com.test.myplaylist.databinding.FragmentHomeBinding
import com.test.myplaylist.extension.provideViewModels
import com.test.myplaylist.util.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var navigator: MainNavigator


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = { inflater, container, attachToParent ->
            FragmentHomeBinding.inflate(inflater, container, attachToParent)
        }

    private val viewModel: HomeViewModel by provideViewModels()



    override fun bindViewModel() {
    }
}
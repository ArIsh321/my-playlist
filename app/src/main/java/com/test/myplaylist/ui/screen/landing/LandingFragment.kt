package com.test.myplaylist.ui.screen.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import com.test.myplaylist.R
import com.test.myplaylist.base.BaseFragment
import com.test.myplaylist.databinding.FragmentLandingScreenBinding
import com.test.myplaylist.extension.provideViewModels
import com.test.myplaylist.util.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingFragment : BaseFragment<FragmentLandingScreenBinding>() {

    @Inject
    lateinit var navigator: MainNavigator

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLandingScreenBinding
        get() = { inflater, container, attachToParent ->
            FragmentLandingScreenBinding.inflate(inflater, container, attachToParent)
        }

    private val viewModel: LandingViewModel by provideViewModels()

    override fun setupView() {
        super.setupView()
        androidVersion()
        this@LandingFragment.viewModel.sleepTimeStart()
    }

    private fun androidVersion() {
        val manager = context?.packageManager
        val info = context?.packageName?.let {
            manager?.getPackageInfo(
                it, 0
            )
        }
        binding.versionCode.text = getString(R.string.version, info?.versionName)
    }

    override fun bindViewModel() {
        viewModel.navigator bindTo navigator::navigate
    }
}
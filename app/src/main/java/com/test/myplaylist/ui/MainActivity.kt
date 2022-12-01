package com.test.myplaylist.ui

import android.view.LayoutInflater
import androidx.activity.viewModels
import com.test.myplaylist.base.BaseActivity
import com.test.myplaylist.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = { inflater -> ActivityMainBinding.inflate(inflater) }

    override val viewModel by viewModels<MainViewModel>()

}
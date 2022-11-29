package com.test.myplaylist.ui.screen.home


import com.test.myplaylist.base.BaseViewModel
import com.test.myplaylist.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

}

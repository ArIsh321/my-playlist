package com.test.myplaylist.util

import androidx.fragment.app.Fragment
import com.test.myplaylist.R
import com.test.myplaylist.base.BaseNavigator
import com.test.myplaylist.base.BaseNavigatorImpl
import com.test.myplaylist.base.NavigationEvent
import com.test.myplaylist.ui.screen.landing.LandingFragmentDirections
import javax.inject.Inject

interface MainNavigator : BaseNavigator

class MainNavigatorImpl @Inject constructor(
    fragment: Fragment
) : BaseNavigatorImpl(fragment), MainNavigator {

    override val navHostFragmentId = R.id.navHostFragment

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.PopBackStack -> popBack()
            is NavigationEvent.OnHome -> navigateToHome()
        }
    }

    private fun navigateToHome() {
        val navController = findNavController()
        when (navController?.currentDestination?.id) {
            R.id.landingFragment -> navController.navigate(
                LandingFragmentDirections.actionLandingFragmentToHomeFragment()
            )
            else -> {

            }
        }
    }

}

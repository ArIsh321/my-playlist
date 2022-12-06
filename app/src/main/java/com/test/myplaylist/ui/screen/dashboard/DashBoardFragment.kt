package com.test.myplaylist.ui.screen.dashboard

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.test.myplaylist.base.BaseActivity
import com.test.myplaylist.base.BaseFragment
import com.test.myplaylist.databinding.ActivityMainBinding
import com.test.myplaylist.databinding.FragmentDashboardBinding
import com.test.myplaylist.databinding.FragmentHomeBinding
import com.test.myplaylist.domain.model.ImagesList
import com.test.myplaylist.domain.model.Users
import com.test.myplaylist.extension.provideViewModels
import com.test.myplaylist.ui.screen.dashboard.adapter.ImageAdapter
import com.test.myplaylist.ui.screen.dashboard.adapter.UsersListAdapter
import com.test.myplaylist.ui.screen.home.HomeViewModel
import com.test.myplaylist.ui.screen.home.MusicListAdapter
import com.test.myplaylist.util.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var navigator: MainNavigator

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding
        get() = { inflater, container, attachToParent ->
            FragmentDashboardBinding.inflate(inflater, container, attachToParent)
        }

    private val viewModel: DashBoardViewModel by provideViewModels()

    private lateinit var usersListAdapter  :  UsersListAdapter


    override fun setupView() {
        super.setupView()
        with(binding){

        }
        setUpDataList()
    }

    private fun setUpDataList() {
        with(binding.rvView) {
            adapter = UsersListAdapter().also { usersListAdapter = it }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun bindViewModel() {
        viewModel.images bindTo :: setUpViewPager
        viewModel.userList bindTo :: bindData
    }

    private fun bindData(list: List<Users>) {
           with(usersListAdapter){
               if(list.isNotEmpty()){
                   items = list.toMutableList()
               }
           }
    }

    private fun setUpViewPager(list: List<ImagesList>) {
        with(binding) {
            includeLayout.onCarouselView.apply {
                adapter = ImageAdapter(list)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                includeLayout.dotsIndicator.setViewPager2(includeLayout.onCarouselView)
                registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // selected position
                        if (position == adapter!!.itemCount - 1) {
                            Handler(Looper.getMainLooper()).postDelayed({
                            }, 500)

                        } else {
                        }
                    }
                })
            }
        }
    }

}
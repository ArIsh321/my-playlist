package com.test.myplaylist.ui.screen.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.test.myplaylist.base.BaseFragment
import com.test.myplaylist.databinding.FragmentDashboardBinding
import com.test.myplaylist.domain.model.ImagesList
import com.test.myplaylist.domain.model.Users
import com.test.myplaylist.extension.provideViewModels
import com.test.myplaylist.ui.screen.dashboard.adapter.ImageAdapter
import com.test.myplaylist.ui.screen.dashboard.adapter.UsersListAdapter
import com.test.myplaylist.util.MainNavigator
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DashBoardFragment : BaseFragment<FragmentDashboardBinding>() {

    @Inject
    lateinit var navigator: MainNavigator

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDashboardBinding
        get() = { inflater, container, attachToParent ->
            FragmentDashboardBinding.inflate(inflater, container, attachToParent)
        }

    private val viewModel: DashBoardViewModel by provideViewModels()

    private lateinit var usersListAdapter: UsersListAdapter

    private var usersList: ArrayList<Users> = ArrayList()


    override fun setupView() {
        super.setupView()
        with(binding) {
            etSearchView.doOnTextChanged { text, _, _, _ ->
                if (text!!.isNotEmpty()) {
                    filter(text.toString())
                } else {
                    viewModel.getUserList(1)
                }
            }
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
        viewModel.images bindTo ::setUpViewPager
        viewModel.userList bindTo ::bindData
    }

    private fun bindData(list: List<Users>) {
        with(usersListAdapter) {
            if (list.isNotEmpty()) {
                usersList =
                    list as ArrayList<Users> /* = java.util.ArrayList<com.test.myplaylist.domain.model.Users> */
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

                        viewModel.getUserList(position+1)
                        // selected position
//                        if (position == adapter!!.itemCount - 1) {
//                            Handler(Looper.getMainLooper()).postDelayed({
//                            }, 500)
//
//                        } else {
//                        }
                    }
                })
            }
        }
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Users> = ArrayList()

        // running a for loop to compare elements.
        for (item in usersList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.firstName.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(activity, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.

            viewModel.fetchUsers(filteredlist)
        }
    }


    override fun bindViewEvents() {
        super.bindViewEvents()
        usersListAdapter.itemClick.bindTo {
            when (it) {
                is UsersListAdapter.OnItemClick.OnItemClickUser -> {
                    onSelectUser(it.data)
                }
            }
        }
    }

    private fun onSelectUser(data: Users) {

    }
}
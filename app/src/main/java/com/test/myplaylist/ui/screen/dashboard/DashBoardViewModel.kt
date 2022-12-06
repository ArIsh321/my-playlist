package com.test.myplaylist.ui.screen.dashboard


import com.test.myplaylist.base.BaseViewModel
import com.test.myplaylist.di.usecase.UseCaseResult
import com.test.myplaylist.di.usecase.UserCase
import com.test.myplaylist.domain.model.ImagesList
import com.test.myplaylist.domain.model.Users
import com.test.myplaylist.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface Input {
    val images: StateFlow<List<ImagesList>>
    val userList: StateFlow<List<Users>>

}

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    dispatchers: DispatchersProvider,
    private val userCase: UserCase

) : BaseViewModel(dispatchers), Input {

    private val _images = MutableStateFlow(ArrayList<ImagesList>())
    override val images: StateFlow<List<ImagesList>>
        get() = _images

    private val _userList = MutableStateFlow(ArrayList<Users>())
    override val userList: StateFlow<List<Users>>
        get() = _userList

    init {
        _images.value = arrayListOf(
            ImagesList(
                "",
                "https://img.freepik.com/premium-photo/tech-devices-icons-connected-digital-planet-earth_117023-449.jpg?w=2000"
            ),
            ImagesList(
                "",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSDL-zbn7zKFL9UOMEalRNTHLo9vO4UcpcX8gKt4FQA&s"
            ),
            ImagesList(
                "",
                "https://media.istockphoto.com/photos/machine-learning-hands-of-robot-and-human-touching-on-big-data-picture-id1206796363?b=1&k=20&m=1206796363&s=612x612&w=0&h=P-Ijv-53HJyQkKKebB7JcQDwbvZXhz38PoDd_NF_gnk="
            ),
            ImagesList(
                "",
                "https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8dGVjaHxlbnwwfHwwfHw%3D&w=1000&q=80"
            ),
            ImagesList(
                "",
                "https://media.istockphoto.com/id/480651173/photo/man-with-hi-tech-circuit-theme.jpg?s=612x612&w=0&k=20&c=T6edxqT6H_m6saz8kqozStwTqULxmBRTQl3PjmTpJUY="
            ),
        )
        getUserList(1)
    }

     fun getUserList(position: Int) {
        execute {
            when (val result =
                userCase.executeUserList(position)) {
                is UseCaseResult.Success -> {
                    _userList.value = result.data.users as ArrayList<Users> /* = java.util.ArrayList<com.test.myplaylist.domain.model.Users> */
                }

                is UseCaseResult.NetworkError -> {

                }
                is UseCaseResult.Error -> {
                }
            }
        }
    }

    fun fetchUsers(filteredlist: ArrayList<Users>) {
        _userList.value = filteredlist
    }
}



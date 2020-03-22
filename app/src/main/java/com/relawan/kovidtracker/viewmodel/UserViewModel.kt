package com.relawan.kovidtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relawan.kovidtracker.data.User
import com.relawan.kovidtracker.data.UserRepository
import kotlinx.coroutines.launch

class UserViewModel internal constructor(private val userRepository: UserRepository): ViewModel() {

   /* private var _user = MutableLiveData<User>().apply {
        value = userRepository.getUser().value
    }*/
    val user: LiveData<User> = userRepository.getUser()

    fun save(ct: User){
//        _user.value = ct
        viewModelScope.launch {
            userRepository.save(ct)
        }
    }
}
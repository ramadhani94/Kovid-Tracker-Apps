package com.relawan.kovidtracker.viewmodel


import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs
import com.relawan.kovidtracker.data.ContactRepository
import com.relawan.kovidtracker.data.UserRepository

class UserVMFactory(
    private val repository: UserRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return UserViewModel(repository) as T
    }
}

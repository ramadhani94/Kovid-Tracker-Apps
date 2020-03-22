package com.relawan.kovidtracker.utilities

import android.content.Context
import androidx.fragment.app.Fragment
import com.relawan.kovidtracker.data.AppDatabase
import com.relawan.kovidtracker.data.ContactRepository
import com.relawan.kovidtracker.data.UserRepository
import com.relawan.kovidtracker.viewmodel.ContactsVMFactory
import com.relawan.kovidtracker.viewmodel.UserVMFactory

object InjectorUtils {

     fun getUserRepository(context: Context): UserRepository {
        return UserRepository.getInstance(
            AppDatabase.getInstance(context).userDao())
    }

     fun getContactRepository(context: Context): ContactRepository {
        return ContactRepository.getInstance(
            AppDatabase.getInstance(context).contactDao())
    }

    fun provideContactsViewModelFactory(
        fragment: Fragment
    ): ContactsVMFactory {
        val repository = getContactRepository(fragment.requireContext())
        return ContactsVMFactory(repository, fragment)
    }

    fun provideUserViewModelFactory(
        fragment: Fragment
    ): UserVMFactory {
        val repository = getUserRepository(fragment.requireContext())
        return UserVMFactory(repository, fragment)
    }
}
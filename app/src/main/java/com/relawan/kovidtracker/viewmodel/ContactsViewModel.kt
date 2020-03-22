package com.relawan.kovidtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.relawan.kovidtracker.data.Contact
import com.relawan.kovidtracker.data.ContactRepository
import kotlinx.coroutines.launch

class ContactsViewModel internal constructor(private val contactRepository: ContactRepository): ViewModel() {
    private val _contactList = MutableLiveData<MutableList<Contact>>().apply {
        value = contactRepository.todayList().value?.toArrayList()
    }
    val contactList: LiveData<List<Contact>> = contactRepository.todayList()
    val lastId: MutableLiveData<Long> = MutableLiveData<Long>().apply { value = 0 }

    fun addContact(ct: Contact){
        _contactList.value?.add(ct)
        viewModelScope.launch {
            lastId.value = contactRepository.save(ct)
        }
    }

    private fun <T> List<T>.toArrayList(): ArrayList<T>{
        return ArrayList(this)
    }
}
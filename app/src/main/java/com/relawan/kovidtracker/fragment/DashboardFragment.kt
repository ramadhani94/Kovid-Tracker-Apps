package com.relawan.kovidtracker.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.relawan.kovidtracker.R
import com.relawan.kovidtracker.data.Contact
import com.relawan.kovidtracker.utilities.InjectorUtils
import com.relawan.kovidtracker.viewmodel.ContactsViewModel

class DashboardFragment : Fragment() {

    private val contactsViewModel: ContactsViewModel by viewModels {
        InjectorUtils.provideContactsViewModelFactory(this)
    }
    private lateinit var root: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        initView()
        return root
    }

    private fun initView(){
        activity?.let {
            contactsViewModel.contactList.observe(it, Observer<List<Contact>> { t ->
                updateText(t)
            })
        }
    }

    private fun updateText(list: List<Contact>?){
        val textView: TextView = root.findViewById(R.id.text_count)
        textView.text = (list?.size ?: 0).toString() + " Unit"
    }

    override fun onResume() {
        super.onResume()
        initView()
    }
}
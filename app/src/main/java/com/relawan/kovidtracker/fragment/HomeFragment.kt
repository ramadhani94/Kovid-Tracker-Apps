package com.relawan.kovidtracker.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.relawan.kovidtracker.R
import com.relawan.kovidtracker.data.Contact
import com.relawan.kovidtracker.data.ContactRepository
import com.relawan.kovidtracker.data.User
import com.relawan.kovidtracker.utilities.InjectorUtils
import com.relawan.kovidtracker.utilities.REQUEST_ENABLE_BT
import com.relawan.kovidtracker.viewmodel.ContactsViewModel
import com.relawan.kovidtracker.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private  var lastLocation: Location? = null
    private var dialog: AlertDialog? = null

    private val contactsViewModel: ContactsViewModel by viewModels {
        InjectorUtils.provideContactsViewModelFactory(this)
    }

    private val userViewModel: UserViewModel by viewModels {
        InjectorUtils.provideUserViewModelFactory(this)
    }

    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(receiver, filter)
        activity?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
            InjectorUtils.getContactRepository(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        init()
        initView()
        initListeners()
        return root
    }

    private fun init(){
        if(bluetoothAdapter == null){
            println("Device not support bluetooth")
        }
        /*val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(receiver)
    }

    private fun initView(){
        val textAddress: TextView = root.findViewById(R.id.text_address)
        val checkBtn: Button = root.findViewById(R.id.check_btn)
        (bluetoothAdapter?.isEnabled == false).let {
            checkBtn.text = when(it) {
                true -> "Turn On Bluetooth"
                else -> "Scan Bluetooth"
            }
        }
//        activity?.let {
            userViewModel.user.observe(viewLifecycleOwner, Observer<User> { t ->
                t?.let {
                    text_home.text = t.name
                    textAddress.text = t.phoneNumber

                    runScanning()
                }
                if(t == null){
//                    showRegisterDialog()
                    activity?.fragmentManager?.popBackStack()
                }
            })
            contactsViewModel.contactList.observe(viewLifecycleOwner, Observer<List<Contact>> { t ->
                println("UPDATE T")
                println(t)
                val textView: TextView = root.findViewById(R.id.text_count)
                textView.text = (t?.size ?: 0).toString() + " Unit"
            })
//        }

    }

    private fun initListeners(){
        val checkBtn: Button = root.findViewById(R.id.check_btn)
        checkBtn.setOnClickListener(){
            runScanning()
        }
    }

    private fun runScanning(){
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//            checkBtn.setBackgroundColor(resources.getColor(R.color.design_default_color_primary_dark))
        }else{
            bluetoothAdapter?.startDiscovery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_ENABLE_BT){
            bluetoothAdapter?.startDiscovery()
        }
    }
    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    addDeviceFound(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE))
                }
            }
        }
    }

    private fun addDeviceFound(device: BluetoothDevice?){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                location?.let {
                    lastLocation = location
                    println(device)
                    device?.let {
                        println(it)
                        val newContact = Contact(device.address, lastLocation?.longitude, lastLocation?.latitude)
                        println(newContact)
                        contactsViewModel.addContact(newContact).let {
                          Toast.makeText(this.activity, "New contact saved", Toast.LENGTH_LONG)
                        }
                    }
                }
            }
    }

    private fun showRegisterDialog(){
        val dialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_register, null)
        val builder = AlertDialog.Builder(this.context).setView(dialogView)
        val dialog = builder.show()
        dialog.setCanceledOnTouchOutside(false)
        dialogView.register_btn.setOnClickListener {
            val nameField = dialogView.fullname
            val phoneField = dialogView.phone
            nameField.text.let {
                val user = User(nameField.text.toString(), phoneField.text.toString())
                println(user)
                userViewModel.save(user).let {
                    dialog.dismiss()
                }
            }

        }
    }

}
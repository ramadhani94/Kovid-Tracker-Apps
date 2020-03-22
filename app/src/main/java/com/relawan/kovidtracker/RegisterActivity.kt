package com.relawan.kovidtracker

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.relawan.kovidtracker.data.User
import com.relawan.kovidtracker.utilities.InjectorUtils
import com.relawan.kovidtracker.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.view.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
        initListener()
    }

    private fun init(){
        userViewModel = UserViewModel(InjectorUtils.getUserRepository(this))
    }

    private fun initListener(){
        val registerButton:Button = findViewById(R.id.register_btn)
        val nameField: TextInputEditText = findViewById(R.id.fullname)
        val phoneField: TextInputEditText = findViewById(R.id.phone)
        registerButton.setOnClickListener {
            val user = User(nameField.text.toString(), phoneField.text.toString())
            println(user)
            userViewModel.save(user)
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}
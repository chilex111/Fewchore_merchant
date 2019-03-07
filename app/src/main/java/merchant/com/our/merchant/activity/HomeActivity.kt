package merchant.com.our.merchant.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import merchant.com.our.merchant.fragment.DashLoanFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import merchant.com.our.merchant.R
import merchant.com.our.merchant.api.BankListAsync
import merchant.com.our.merchant.fragment.CardLoanListFragment
import merchant.com.our.merchant.fragment.HistoryFragment
import merchant.com.our.merchant.fragment.ProfileFragment
import merchant.com.our.merchant.helper.AppUtils
import merchant.com.our.merchant.helper.GMailSender
import merchant.com.our.merchant.helper.SharedPreferenceUtil
import merchant.com.our.merchant.listener.IdListener
import merchant.com.our.merchant.local_database.LocalDatabase
import merchant.com.our.merchant.view.CircleImageView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, IdListener {

    private var title : TextView ?= null
    private var localDB : LocalDatabase ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        localDB = LocalDatabase(this)
        ProfileFragment.listener = this
        DashLoanFragment.idListener = this
        CardLoanListFragment.idListener = this
        title = findViewById(R.id.textTitle)

        BankListAsync(this).execute()



        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val headerview = layoutInflater.inflate(R.layout.nav_header_home, nav_view, false)
        nav_view.addHeaderView(headerview)

        val header = headerview.findViewById<LinearLayout>(R.id.linearHead)
        val headerName = header.findViewById<TextView>(R.id.textName)
        val headerEmail = header.findViewById<TextView>(R.id.textEmail)
        val profileHead = header.findViewById<CircleImageView>(R.id.imageHeader)
        headerEmail.text = AppUtils.getMyEmail(this)
        val profile = AppUtils.getMyProfile(this)
        if (!profile.isNullOrEmpty())
            Picasso.with(this).load(profile).into(profileHead)



        val name = AppUtils.getMyFullName(this)
        headerName.text = name

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, ProfileFragment())
                .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun idListener(id: String, msg: String?, payAmount: Float?) {
        when (id) {
            "invest" -> {
                title!!.text = resources.getText(R.string.request_loan)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DashLoanFragment())
                        .addToBackStack(null)
                        .commit()
            }
            "pay_now" -> {
                title!!.text = resources.getText(R.string.pay_now)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CardLoanListFragment.newInstance("1", payAmount!!,null))
                        .addToBackStack(null)
                        .commit()
            }
            "request" -> {
                title!!.text = resources.getText(R.string.profile)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .addToBackStack(null)
                        .commit()
            }
            "payment successful" ->{
                title!!.text = resources.getText(R.string.profile)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .addToBackStack(null)
                        .commit()
            }
            ""->{
                title!!.text = resources.getText(R.string.profile)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .addToBackStack(null)
                        .commit()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_Profile -> {
                title!!.text = resources.getText(R.string.profile)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment())
                        .commit()
            }
            R.id.nav_borrow -> {
                title!!.text = resources.getText(R.string.request_loan)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DashLoanFragment())
                        .commit()
            }
            R.id.nav_history -> {
                title!!.text = resources.getText(R.string.loan_history)
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .commit()
            }
            R.id.nav_faq -> {
            title!!.text = resources.getText(R.string.support)
            supportForm()
        }
            R.id.nav_signout -> {
                signOut()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun supportForm() {

        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.dialog_contact_support)

        val editTitle = dialog.findViewById<EditText>(R.id.storyTitle)
        dialog.findViewById<ImageButton>(R.id.contactLine).setOnClickListener {
            val msg = "You can call us week days Monday-Friday between the hours 8:00am - 5:00pm"
            val localBuilder = android.support.v7.app.AlertDialog.Builder(this)
            localBuilder.setMessage(msg)
            localBuilder.setNeutralButton(R.string.ok) { _, _ ->

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:09058699681")
                startActivity(intent)
            }
            localBuilder.create().show()
        }

        val editBody = dialog.findViewById<EditText>(R.id.editStory)
        val sendButton = dialog.findViewById<ImageButton>(R.id.buttonSend)
        val phone = dialog.findViewById<EditText>(R.id.ediPhone)
        val name= dialog.findViewById<EditText>(R.id.editFullname)
        dialog.show()
        dialog.findViewById<ImageButton>(R.id.buttonBack).setOnClickListener {
            dialog.dismiss()
        }
        sendButton.setOnClickListener {
            val bodyText = editBody.text.toString()
            val titleText = editTitle.text.toString()
            val phoneText = phone.text.toString()
            val nameText = name.text.toString()

            if (bodyText.isEmpty())
                editBody.error = "This field is required"

            if (nameText.isEmpty())
                name.error = "This field is required"
            if (phoneText.isEmpty())
                phone.error ="This field is required"
            if (titleText.isEmpty())
                editTitle.error ="This field is required"
            else
                sendEmail(titleText, bodyText, nameText, phoneText)
            dialog.dismiss()
        }
    }
    private fun sendEmail( title: String?, body: String?, name: String?,phone: String?) {

        /*
            if (!email.contains("@") && !email.contains(".")) {
                appUtils.showAlert("Please contact Radix Pension Customer Care to change Email")
            } else {*/
        val email = "fewchoremfb@gmail.com"
        Thread(Runnable {
            try {
                val sender = GMailSender(email,
                        "fewchoremfb@123")


                sender.sendMail(title, "Hi, \n My name is$name. \n$body and my phone number: $phone \n Thanks.",
                        email, "merchant@fewchorefinance.com,$email")

            } catch (e: Exception) {
                Log.e("SendMail", e.message, e)
            }
        }).start()

        val msg = "Request successful Submitted."
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setNeutralButton(R.string.ok) { _, _ -> }

        builder.create().show()
        //  }
    }

    private fun signOut() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please confirm sign out")
                .setPositiveButton("Sign Out") { _, _ ->
                    val sharedPreferenceUtils = SharedPreferenceUtil

                    sharedPreferenceUtils.clearSharedPreference(this@HomeActivity)
                    localDB!!.clearDB()

                    startActivity(Intent(this@HomeActivity,LoginActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create().show()
    }

}

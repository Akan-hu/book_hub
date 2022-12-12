 package android.example.kotlinproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

 class MainActivity : AppCompatActivity() {

     lateinit var drawerLayout: DrawerLayout
     lateinit var navigationView: NavigationView
     lateinit var coordinatorLayout: CoordinatorLayout
     lateinit var frameLayout: FrameLayout
     lateinit var toolbar: Toolbar

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         drawerLayout = findViewById(R.id.drawer_layout)
         navigationView = findViewById(R.id.navigation)
         coordinatorLayout = findViewById(R.id.coordinater)
         frameLayout = findViewById(R.id.frame)
         toolbar = findViewById(R.id.toolbar)

         setToolBar()
         replaceFragment()

         val actionBarDrawerToggle = ActionBarDrawerToggle(
             this@MainActivity, drawerLayout,
             R.string.open_drawer, R.string.close_drawer
         )
         navigationView.setCheckedItem(R.id.dash_board)
         drawerLayout.addDrawerListener(actionBarDrawerToggle)
         actionBarDrawerToggle.syncState()
         navigationView.setNavigationItemSelectedListener {

             when (it.itemId) {
                 R.id.dash_board -> {
                     replaceFragment()
                 }
                 R.id.my_favourites -> {
                     supportFragmentManager.beginTransaction().replace(R.id.frame,FavoutiteFragment())
                         .commit()
                     supportActionBar?.title = "Favourites"
                     drawerLayout.close()

                 }
                 R.id.profile -> {
                     supportFragmentManager.beginTransaction().replace(R.id.frame,ProfileFragment()).commit()
                     supportActionBar?.title = "My Profile"
                     drawerLayout.close()
                 }
                 R.id.about -> {
                       supportFragmentManager.beginTransaction()
                         .replace(R.id.frame, AboutFragment())
                         .commit()
                     supportActionBar?.title = "My Notes"
                     drawerLayout.close()
                 }
                 R.id.cart_fragment ->{
                     supportFragmentManager.beginTransaction().replace(R.id.frame,MyCartFragment()).commit()
                     supportActionBar?.title = "My Cart"
                     drawerLayout.close()
                 }
             }

             return@setNavigationItemSelectedListener true
         }
     }

     private fun replaceFragment() {
         val fragment = DashboardFragment()
         val transaction = supportFragmentManager.beginTransaction()
         transaction.replace(R.id.frame,fragment)
         transaction.commit()
         supportActionBar?.title = "Dashboard"
         drawerLayout.close()
     }
     private fun setToolBar() {
         setSupportActionBar(toolbar)
         supportActionBar?.title = "Book Store"
         supportActionBar?.setHomeButtonEnabled(true)
         supportActionBar?.setDisplayHomeAsUpEnabled(true)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         val id = item.itemId
         if (id == android.R.id.home) {
             drawerLayout.openDrawer(START)
         }
         return super.onOptionsItemSelected(item)
     }

     override fun onBackPressed() {
         val frame = supportFragmentManager.findFragmentById(R.id.frame)
         when (frame) {
             !is DashboardFragment -> {
                 replaceFragment()
                 navigationView.setCheckedItem(R.id.dash_board)

             }

             else -> super.onBackPressed()
         }

     }

 }




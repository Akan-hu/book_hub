package android.example.kotlinproject.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager

class ConnectionManager {

    //this function is used to check connectivity of app with internet
    fun checkConnectivity(context : Context) : Boolean {

        //connectivity manager checks that our device is connected with internet or not
        //connectivity manager contains the device connectivity information with internet
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //below code is used to check activeness of network, checking for if device is not broken or hardware is working
        val activeNetwork : NetworkInfo? = connectivityManager.activeNetworkInfo

        if(activeNetwork?.isConnected != null){
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }
}
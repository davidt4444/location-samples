package com.google.android.gms.location.sample.foregroundlocation
import android.content.Context
import android.location.Location
import android.util.Log
import java.io.IOException
import java.io.OutputStreamWriter
import java.security.AccessController.getContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMetadata@Inject constructor(
)
{
    val instance = this
    var runname = "test_run"
    var username = "test"
    var password = "test"
    var twitter_handle = "test_handle"
    var email = "test@aggregatedatasystems.com"
    var locations = emptyList<LocationMetadata>()
    fun addLocation(loc :Location?)
    {
        if (loc != null) {
            locations += LocationMetadata(loc)
        }
    }
    fun printUserMetadata():String
    {
        var stuff = "runname, timestamp, latitude, longitude, username, twitter_handle, email\n"
        for(loc in locations.reversed())
        {
            stuff += runname+", "+loc.dateTime+", "+loc.location?.latitude+", "+loc.location?.longitude+", "+username+", "+twitter_handle+", "+email+"\n"
        }

        return stuff
    }
    fun writeToFile(data: String, context: Context?) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context?.openFileOutput("location_data.csv", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }
    fun thisUserMetadata():UserMetadata
    {
        return instance
    }
}


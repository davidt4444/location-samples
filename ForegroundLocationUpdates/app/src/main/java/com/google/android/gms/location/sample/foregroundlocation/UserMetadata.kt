package com.google.android.gms.location.sample.foregroundlocation
import android.content.Context
import android.location.Location
import android.util.Log
import java.io.IOException
import java.io.OutputStreamWriter
import javax.inject.Inject
import javax.inject.Singleton
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Singleton
class UserMetadata@Inject constructor(
)
{
    val instance = this
    var userToken = "1234"
    var username = "test"
    var password = "test"
    var twitter_handle = "test_handle"
    var email = "test@aggregatedatasystems.com"
    var locations = emptyList<LocationMetadata>()
    fun addLocation(loc :LocationMetadata?)
    {
        if (loc != null) {
            locations += loc
        }
    }
    fun printUserMetadata():String
    {
        var stuff = "userToken,runname,timestamp,latitude,longitude\n"
        for(loc in locations.reversed())
        {
            stuff += loc.userToken.replace(",", "")+loc.runname.replace(",", "")+","+loc.dateTime+","+loc.location.latitude+","+loc.location.longitude+"\n"
        }
        stuff += userToken+"\n"
        stuff += username+"\n"
        stuff += password+"\n"
        stuff += twitter_handle+"\n"
        stuff += email+"\n"

        return stuff
    }
    fun loadUserMetadata(stuff: String): String? {
        var response = ""
        val lines = stuff.split("\n").toTypedArray()
        try {
            for (i in 1 until lines.size) {
                val cols = lines[i].split(",").toTypedArray()
                if (cols.size == 5) {
                    var loc = LocationMetadata()
                    loc.userToken = cols[0]
                    loc.runname = cols[1]
                    //DateFormat df = DateFormat.getDateInstance();
                    val df: DateFormat = SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy")
                    val temp_date: Date? = df.parse(cols[2])
                    loc.dateTime = temp_date
                    val temp_lat = java.lang.Double.valueOf(cols[3])
                    val temp_long = java.lang.Double.valueOf(cols[4])
                    loc.location.latitude = temp_lat
                    loc.location.longitude = temp_long
                    addLocation(loc)
                } else if (i == lines.size-5) {
                    userToken = cols[0]
                } else if (i == lines.size-4) {
                    username = cols[0]
                } else if (i == lines.size-3) {
                    password = cols[0]
                } else if (i == lines.size-2) {
                    twitter_handle = cols[0]
                } else if (i == lines.size-1) {
                    email = cols[0]
                } else {
                    throw Exception("The number of columns does not match up with the expected 5 for userToken, runname, timestamp, latitude, longitude and 1 for userToken, username, password, twitter_handle, email!")
                }
            }
        } catch (e: Exception) {
            response += """
            ${e.message}
            
            """.trimIndent()
            response += """
            ${e.stackTrace}
            
            """.trimIndent()
            userToken = "1234"
            username = "test"
            password = "test"
            twitter_handle = "test_handle"
            email = "test@aggregatedatasystems.com"
            locations = ArrayList()
        }
        return response
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


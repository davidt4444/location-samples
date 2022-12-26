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
        var stuff = "runname, timestamp, latitude, longitude, username, twitter_handle, email\n"
        for(loc in locations.reversed())
        {
            stuff += loc.runname.replace(",", "")+","+loc.dateTime+","+loc.location?.latitude+","+loc.location?.longitude+","+username.replace(",", "")+","+twitter_handle.replace(",", "")+","+email.replace(",", "")+"\n"
        }
        stuff += password+"\n"

        return stuff
    }
    fun loadUserMetadata(stuff: String): String? {
        var response = ""
        val lines = stuff.split("\n").toTypedArray()
        try {
            for (i in 1 until lines.size) {
                val cols = lines[i].split(",").toTypedArray()
                if (cols.size == 7) {
                    val runname = cols[0]
                    //DateFormat df = DateFormat.getDateInstance();
                    val df: DateFormat = SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy")
                    val temp_date: Date = df.parse(cols[1])
                    val temp_lat = java.lang.Double.valueOf(cols[2])
                    val temp_long = java.lang.Double.valueOf(cols[3])
                    val temp_loc = Location("")
                    temp_loc.latitude = temp_lat
                    temp_loc.longitude = temp_long
                    val temp_loc_meta = LocationMetadata(temp_loc, runname, temp_date)
                    addLocation(temp_loc_meta)
                    username = cols[4]
                    twitter_handle = cols[5]
                    email = cols[6]
                } else if (cols.size == 1) {
                    password = cols[0]
                } else {
                    throw Exception("The number of columns does not match up with the expected 7 for runname, temp_date, latitude, longitude, username, twitter_handle and email.")
                }
            }
        } catch (e: Exception) {
            response += """
            ${e.message}
            
            """.trimIndent()
            response += """
            ${e.stackTrace}
            
            """.trimIndent()
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


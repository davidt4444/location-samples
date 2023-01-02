package com.google.android.gms.location.sample.foregroundlocation
import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.OutputStreamWriter
import javax.inject.Singleton
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Singleton
class UserMetadata{
    val instance = this
    var userToken = "1234"
    var locations = emptyList<LocationMetadata>()
    constructor(
        _userToken:String
    )
    {
        userToken = _userToken
    }
        fun addLocation(loc :LocationMetadata?)
        {
            if (loc != null) {
                locations += loc
            }
        }
        fun printUserMetadata():String
        {
            var stuff = "userToken,timestamp,latitude,longitude\n"
            for(loc in locations.reversed())
            {
                stuff += loc.userToken.replace(",", "")+","+loc.dateTime+","+loc.location.latitude+","+loc.location.longitude+"\n"
            }
            stuff += userToken

            return stuff
        }
        fun loadUserMetadata(stuff: String): String? {
            var response = ""
            val lines = stuff.split("\n").toTypedArray()
            try {
                for (i in 1 until lines.size) {
                    val cols = lines[i].split(",").toTypedArray()
                    if (cols.size == 4) {
                        var loc = LocationMetadata()
                        loc.userToken = cols[0]
                        //DateFormat df = DateFormat.getDateInstance();
                        val df: DateFormat = SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy")
                        val temp_date: Date? = df.parse(cols[1])
                        loc.dateTime = temp_date
                        val temp_lat = java.lang.Double.valueOf(cols[2])
                        val temp_long = java.lang.Double.valueOf(cols[3])
                        loc.location.latitude = temp_lat
                        loc.location.longitude = temp_long
                        addLocation(loc)
                    } else if (i == lines.size-1) {
                        userToken = cols[0]
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
                locations = ArrayList()
            }
            return response
        }

        fun writeToFile(data: String?, context: Context?) {
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


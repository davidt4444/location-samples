package com.google.android.gms.location.sample.foregroundlocation
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.Instant
import java.util.Date
import java.util.Calendar;
import javax.inject.Inject


class LocationMetadata{
    var location: Location = Location("")
    var runname = "test"
    var userToken = "1234"
    var dateTime : Date? = Calendar.getInstance().getTime()
    constructor(
        _userToken: String = "1234",
        _runname: String= "test",
        _loc: Location = Location(""),
        _datetime: Date? = Calendar.getInstance().getTime()
    ) {
        location = _loc
        runname = _runname
        userToken = _userToken
        dateTime = _datetime

    }
}



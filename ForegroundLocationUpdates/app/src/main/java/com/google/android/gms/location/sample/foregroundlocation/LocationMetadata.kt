package com.google.android.gms.location.sample.foregroundlocation
import android.location.Location
import java.util.Date
import java.util.Calendar;


class LocationMetadata{
    var location: Location = Location("")
    var userToken:String = "1234"
    var dateTime : Date? = Calendar.getInstance().getTime()
    constructor(
        _userToken: String = "1234",
        _loc: Location = Location(""),
        _datetime: Date? = Calendar.getInstance().getTime()
    ) {
        location = _loc
        userToken = _userToken
        dateTime = _datetime

    }
}



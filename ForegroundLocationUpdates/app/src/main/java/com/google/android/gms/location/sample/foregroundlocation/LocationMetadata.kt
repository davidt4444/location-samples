package com.google.android.gms.location.sample.foregroundlocation
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.Instant
import java.util.Date
import java.util.Calendar;
import javax.inject.Inject


class LocationMetadata @Inject constructor(
    private val _loc: Location?,
    private val _datetime: Date = Calendar.getInstance().getTime()
) {
    var location = _loc
    var dateTime = _datetime

}



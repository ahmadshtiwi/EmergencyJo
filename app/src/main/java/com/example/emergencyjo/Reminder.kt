package com.example.emergencyjo

data class Reminder(
    var key: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var personalID:String="",
    var name:String="",
    var phone:String="",
    var description:String="",
)

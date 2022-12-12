package com.example.emergencyjo

class Expression {
    companion object
    {
        val expPersonalID=Regex("\\d{10}")
        val expCheck=Regex("\\d{3}/\\d{3}")
        val expPassword=Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")

    }
}
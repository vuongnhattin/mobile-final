package com.example.mobilefinal.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AlarmViewModel(private val context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("alarm_timer", Context.MODE_PRIVATE)

    var timerStarted by mutableStateOf(false)
        private set
    var hours by mutableStateOf(0)
        private set
    var minutes by mutableStateOf(0)
        private set
    var day by mutableStateOf(0)
        private set
    var month by mutableStateOf(0)
        private set
    var year by mutableStateOf(0)
        private set
    var ringing by mutableStateOf(false)
        private set

    init {
        loadTimerState()
    }

    fun startRinging() {
        ringing = true
        val editor = sharedPreferences.edit()
        editor.putBoolean("ringing", true)
        editor.apply()
    }

    fun stopRinging() {
        ringing = false
        val editor = sharedPreferences.edit()
        editor.putBoolean("ringing", false)
        editor.apply()
    }

    // Load saved timer state from SharedPreferences
    private fun loadTimerState() {
        timerStarted = sharedPreferences.getBoolean("timer_started", false)
        hours = sharedPreferences.getInt("hours", 0)
        minutes = sharedPreferences.getInt("minutes", 0)
//        seconds = sharedPreferences.getInt("seconds", 0)
        day = sharedPreferences.getInt("day", 0)
        month = sharedPreferences.getInt("month", 0)
        year = sharedPreferences.getInt("year", 0)
        ringing = sharedPreferences.getBoolean("ringing", false)
    }

    // Set the time and save it to SharedPreferences
    fun setTime(hours: Int, minutes: Int) {
        this.hours = hours
        this.minutes = minutes
//        this.seconds = seconds

        saveTimerState()
    }

    fun setDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day

        saveTimerState()
    }

    // Start the timer and save the state
    fun startTimer() {
        timerStarted = true
        saveTimerState()
    }

    // Stop the timer and save the state
    fun stopTimer() {
        timerStarted = false
        saveTimerState()
    }

    // Save the timer state to SharedPreferences
    private fun saveTimerState() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("timer_started", timerStarted)
        editor.putInt("hours", hours)
        editor.putInt("minutes", minutes)
//        editor.putInt("seconds", seconds)
        editor.putInt("day", day)
        editor.putInt("month", month)
        editor.putInt("year", year)
        editor.apply()  // Apply changes asynchronously
    }
}
package com.thell.mutenotification.helper.mutestate

interface IMuteStateAction {

    fun getMuteState():Boolean

    fun setMuteState(value: Boolean)

    fun switchMuteState():Boolean

    fun sendBroadcast()
}
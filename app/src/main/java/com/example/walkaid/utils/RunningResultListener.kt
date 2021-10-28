package com.example.walkaid.utils


interface RunningResultListener {
    fun setDuration(value: String)

    fun setDistance(value: String)

    fun setCalorie(value: String)

    fun setSteps(value: String)

    fun setAnalysisFootStrikeMain(strike: Int, percent: String)

    fun setAnalysisForeFootStrikeSub(percent: String)

    fun setAnalysisMidFootStrikeSub(percent: String)

    fun setAnalysisHeelFootStrikeSub(percent: String)

    fun setForeFootIndicator()

    fun setMidFootIndicator()

    fun setHeelFootIndicator()

    fun setPace(value_km: String, value_mile: String)

    fun setCadence(value: Int)

    fun setGroundContactTime(value: Double)

    fun onClickRoute(runningId: Int)

    fun onClickDetailAndSplits(runningId: Int)

    fun setDateTitle(timeStamp: String)

    fun setDisableButton()
}

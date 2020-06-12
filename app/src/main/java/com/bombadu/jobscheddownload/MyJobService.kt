package com.bombadu.jobscheddownload

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.net.Uri
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MyJobService : JobService() {
    private var jobCancelled = false


    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        Log.d(TAG, " Job Started")
        doBackgroundWork(jobParameters)
        return true
    }

    private fun doBackgroundWork(jobParameters: JobParameters?) {
        Thread(Runnable {

           val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .build()

            client.newCall(request).execute().use {  response ->
                if(!response.isSuccessful) throw IOException("Unexpected code $response")

                println(response.body!!.string())
            }






            if (jobCancelled) {
                return@Runnable
            }

            Log.d(TAG, "Job Finished")
            jobFinished(jobParameters, false)

        }).start()
    }


    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "Job Cancelled before Completion")
        jobCancelled = true
        return false
    }

    companion object {
        private const val TAG = "MYJobService"
        private const val url = "https://newsapi.org/v1/articles?source=engadget&apiKey=e784da02bc1c452ebf5d10a1df98a162"
    }


}
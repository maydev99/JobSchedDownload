package com.bombadu.jobscheddownload

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloadButton = findViewById<Button>(R.id.downloadButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        downloadButton.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val info = JobInfo.Builder(756, componentName)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 1000.toLong())
                    .build()

            val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = scheduler.schedule(info)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                makeAToast("Job Scheduled")
                Log.d(TAG, "Job Scheduled")
            } else {
                makeAToast("Job Scheduling Failed")
                Log.d(TAG, "Job Scheduling Failed")
            }
        }

        cancelButton.setOnClickListener {
            val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancel(756)
            makeAToast("Job Cancelled")
            Log.d(TAG, "Job Cancelled")
        }

        



    }



    companion object {
        private const val TAG = "MainActivity"
    }

    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }
}
package perangkaikode.com.cobadownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var requestDownloader: RequestDownloader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestDownloader = RequestDownloader(this)

        initOnClick()
        responseDownload()
    }

    private fun initOnClick(){
        start_download_btn.setOnClickListener {
            requestDownloader?.prepareDownload("http://dl-ssl.google.com/android/repository/platform-tools_r09-windows.zip")
        }
        cancel_download_btn.setOnClickListener {
            requestDownloader?.cancel()
        }
        pause_download_btn.setOnClickListener {
            requestDownloader?.pause()
        }
        resume_download_btn.setOnClickListener {
            requestDownloader?.prepareDownload("http://dl-ssl.google.com/android/repository/platform-tools_r09-windows.zip")
            requestDownloader?.resume()
        }
    }

    private fun responseDownload(){
        requestDownloader?.getResponse()?.observe(this, Observer { response ->
            when (response?.type){
                RequestDownloader.UPDATE_PROGRESS -> {
                    val output = response.output as OnProgressDownload
                    percent_txt.text = output.percent.toString().plus("%")
                    size_txt.text = output.downloadedSize
                    download_progress.progress = output.percent
                }
                RequestDownloader.MESSAGE -> {
                    val percent = response.output as String
                    percent_txt.text = percent
                }
            }
        })
    }
}

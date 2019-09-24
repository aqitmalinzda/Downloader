package perangkaikode.com.cobadownloader

import alirezat775.lib.downloader.Downloader
import alirezat775.lib.downloader.core.OnDownloadListener
import android.content.Context
import android.os.Handler
import android.widget.Toast
import java.io.File
import androidx.lifecycle.MutableLiveData

class RequestDownloader(val context: Context) {

    private var downloader: Downloader? = null
    private val handler: Handler = Handler()
    private var nameFile = ""

    private var response = MutableLiveData<ResponseDownloader>()

    fun getResponse(): MutableLiveData<ResponseDownloader> {
        return response
    }

    fun cancel(){
        downloader?.cancelDownload()
    }

    fun pause(){
        downloader?.pauseDownload()
    }

    fun resume(){
        downloader?.resumeDownload()
    }

    //disini bisa ditambahkan nama file dan id file
    fun prepareDownload(out: String) {
        val fileName = out.substring(out.lastIndexOf("/") + 1)
        //ambil email dari shared preference
        val key = ".malin@gmail.com"

        val zipFromServerDone = File(Constant.bookDirectory, key + "_" + fileName)

        nameFile = zipFromServerDone.nameWithoutExtension
        getDownloader(out)

        if (zipFromServerDone.exists()) {
            //jika menerapkan konsep pause resume ini nanti diganti log aja
            Toast.makeText(context, "Maaf buku sudah ada.", Toast.LENGTH_SHORT).show()
        } else {
            downloader?.download()
        }
    }

    private fun getDownloader(out: String) {
        downloader = Downloader.Builder(context, out)
            .downloadDirectory(Constant.BOOK_DIRECTORY)
            .fileName(nameFile, "zip")
            .downloadListener(object : OnDownloadListener {
                override fun onStart() {}
                override fun onPause() {}
                override fun onResume() {}
                override fun onProgressUpdate(percent: Int, downloadedSize: Int, totalSize: Int) {
                    handler.post {
                        response.postValue(
                            ResponseDownloader(
                                UPDATE_PROGRESS,
                                OnProgressDownload(percent, getSize(downloadedSize))
                            )
                        )
                    }
                }

                override fun onCompleted(file: File?) {
                    handler.post {
                        response.postValue(ResponseDownloader(MESSAGE, "Download selesai."))
                    }
                }

                override fun onFailure(reason: String?) {
                    handler.post {
                        response.postValue(ResponseDownloader(MESSAGE, reason!!))
                    }
                }

                override fun onCancel() {}
            }).build()
    }

    private fun getSize(size: Int): String {
        var s = ""
        val kb = (size / 1024).toDouble()
        val mb = kb / 1024
        val gb = kb / 1024
        val tb = kb / 1024
        if (size < 1024) {
            s = "$size Bytes"
        } else if (size >= 1024 && size < 1024 * 1024) {
            s = String.format("%.2f", kb) + " KB"
        } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
            s = String.format("%.2f", mb) + " MB"
        } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
            s = String.format("%.2f", gb) + " GB"
        } else if (size >= 1024 * 1024 * 1024 * 1024) {
            s = String.format("%.2f", tb) + " TB"
        }
        return s
    }

    companion object {
        val UPDATE_PROGRESS = 1
        val MESSAGE = 2
    }
}
package perangkaikode.com.cobadownloader

import android.os.Environment

import java.io.File

object Constant {
    var BOOK_DIRECTORY = Environment
        .getExternalStorageDirectory().absolutePath + "/.iSample/datas/"

    var bookDirectory = File(BOOK_DIRECTORY)
//    var imageDirectory = File(bookDirectory, "images")
//    var noMediaFile = File(imageDirectory, ".nomedia")
//    var tempFolder = File(bookDirectory, "temp")
}

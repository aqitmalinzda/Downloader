package perangkaikode.com.cobadownloader

data class ResponseDownloader(
    val type: Int,
    val output: Any
)

data class OnProgressDownload(
    val percent: Int,
    val downloadedSize: String
)
package akturankpredictorbyamitmaity.example.akturankpredictor

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RankClient {
    private val URL ="https://maityamit.github.io/Aktu_Rank_Predictor/"
    //CREATE HTTP CLIENT
    private val okHttp = OkHttpClient.Builder()

    //retrofit builder
    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    //create retrofit Instance
    private val retrofit = builder.build()

    //we will use this class to create an anonymous inner class function that
    //implements Country service Interface

    fun <T> buildService (serviceType :Class<T>):T{
        return retrofit.create(serviceType)
    }
}
package akturankpredictorbyamitmaity.example.akturankpredictor.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RankAPISERVICE {
    @GET("{uid}")
    fun getApiResponseAKTUBTECH(@Path("uid") uid: String?): Call<List<ModelClass>>
}

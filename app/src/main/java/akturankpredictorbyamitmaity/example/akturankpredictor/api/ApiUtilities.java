package akturankpredictorbyamitmaity.example.akturankpredictor.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static akturankpredictorbyamitmaity.example.akturankpredictor.services.Constants.BASE_URL;

public class ApiUtilities {

    private static Retrofit retrofit = null;
    public static ApiInterface getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }

}
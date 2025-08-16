package akturankpredictorbyamitmaity.example.akturankpredictor.data.repository

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.FilterOptions
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterOptionsRepository(private val context: Context) {
    
    companion object {
        private const val TAG = "FilterOptionsRepository"
    }
    
    suspend fun getFilterOptions(): ApiResponse<FilterOptions> = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d(TAG, "Loading filter options from assets...")
            val jsonString = loadFilterOptionsFromAssets()
            Log.d(TAG, "Filter options JSON loaded successfully")
            
            val filterOptions = Gson().fromJson(jsonString, FilterOptions::class.java)
            Log.d(TAG, "Parsed filter options: ${filterOptions.states.size} states, ${filterOptions.genders.size} genders, ${filterOptions.quotas.size} quotas")
            
            ApiResponse.Success(filterOptions)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading filter options: ${e.message}", e)
            ApiResponse.Error("Failed to load filter options: ${e.message}")
        }
    }
    
    private fun loadFilterOptionsFromAssets(): String {
        return try {
            Log.d(TAG, "Opening filter_options.json from assets...")
            val inputStream = context.assets.open("filter_options.json")
            val size = inputStream.available()
            Log.d(TAG, "File size: $size bytes")
            
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            Log.d(TAG, "Successfully loaded filter options JSON from assets")
            jsonString
        } catch (e: Exception) {
            Log.e(TAG, "Error reading filter options from assets: ${e.message}", e)
            throw Exception("Could not load filter_options.json from assets: ${e.message}")
        }
    }
}

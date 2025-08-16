package akturankpredictorbyamitmaity.example.akturankpredictor.data.repository

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExamRepository(private val context: Context) {
    
    companion object {
        private const val TAG = "ExamRepository"
    }
    
    suspend fun getExams(): ApiResponse<List<Exam>> = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d(TAG, "Loading exams from assets...")
            val jsonString = loadExamsFromAssets()
            Log.d(TAG, "JSON loaded: $jsonString")
            
            val examListType = object : TypeToken<List<Exam>>() {}.type
            val exams = Gson().fromJson<List<Exam>>(jsonString, examListType)
            Log.d(TAG, "Parsed ${exams?.size ?: 0} exams")
            
            val activeExams = exams?.filter { it.isActive } ?: emptyList()
            Log.d(TAG, "Active exams: ${activeExams.size}")
            ApiResponse.Success(activeExams)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading exams: ${e.message}", e)
            ApiResponse.Error("Failed to load exams: ${e.message}")
        }
    }
    
    private fun loadExamsFromAssets(): String {
        return try {
            Log.d(TAG, "Opening exams.json from assets...")
            val inputStream = context.assets.open("exams.json")
            val size = inputStream.available()
            Log.d(TAG, "File size: $size bytes")
            
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            Log.d(TAG, "Successfully loaded JSON from assets")
            jsonString
        } catch (e: Exception) {
            Log.e(TAG, "Error reading from assets: ${e.message}", e)
            throw Exception("Could not load exams.json from assets: ${e.message}")
        }
    }
    
    fun getExamById(exams: List<Exam>, examId: String): Exam? {
        return exams.find { it.id == examId }
    }
}

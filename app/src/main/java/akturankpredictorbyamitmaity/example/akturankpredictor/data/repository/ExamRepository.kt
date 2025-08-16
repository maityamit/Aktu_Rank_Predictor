package akturankpredictorbyamitmaity.example.akturankpredictor.data.repository

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.Exam
import android.content.Context
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
            val jsonString = loadExamsFromAssets()

            val examListType = object : TypeToken<List<Exam>>() {}.type
            val exams = Gson().fromJson<List<Exam>>(jsonString, examListType)

            val activeExams = exams?.filter { it.isActive } ?: emptyList()
            ApiResponse.Success(activeExams)
        } catch (e: Exception) {
            ApiResponse.Error("Failed to load exams: ${e.message}")
        }
    }
    
    private fun loadExamsFromAssets(): String {
        return try {
            val inputStream = context.assets.open("exams.json")
            val size = inputStream.available()

            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            
            val jsonString = String(buffer, Charsets.UTF_8)
            jsonString
        } catch (e: Exception) {
            throw Exception("Could not load exams.json from assets: ${e.message}")
        }
    }
    
    fun getExamById(exams: List<Exam>, examId: String): Exam? {
        return exams.find { it.id == examId }
    }
}

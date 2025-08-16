package akturankpredictorbyamitmaity.example.akturankpredictor.service

import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.ApiResponse
import akturankpredictorbyamitmaity.example.akturankpredictor.data.model.FilterOptions
import akturankpredictorbyamitmaity.example.akturankpredictor.data.repository.FilterOptionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterOptionsService(private val filterOptionsRepository: FilterOptionsRepository) {
    
    suspend fun getFilterOptions(): ApiResponse<FilterOptions> = withContext(Dispatchers.IO) {
        return@withContext filterOptionsRepository.getFilterOptions()
    }
}

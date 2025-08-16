package akturankpredictorbyamitmaity.example.akturankpredictor.data.model

data class Exam(
    val id: String,
    val name: String,
    val endpoint: String,
    val description: String,
    val icon: String,
    val isActive: Boolean = true
)

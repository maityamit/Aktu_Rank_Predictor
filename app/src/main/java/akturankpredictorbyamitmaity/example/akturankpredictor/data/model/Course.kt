package akturankpredictorbyamitmaity.example.akturankpredictor.data.model

data class Course(
    val name: String,
    val link: String,
    val image: String,
    val price: String,
    val mentors: List<String>,
    val last_date: String,
    val description: String = "",
    val duration: String = "",
    val rating: Float = 0.0f,
    val students_enrolled: Int = 0,
    val is_featured: Boolean = false
)

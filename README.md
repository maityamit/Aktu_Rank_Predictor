# 🎓 AKTU Rank Predictor - Multi-Exam College Predictor with Live Courses

[![Android](https://img.shields.io/badge/Android-API%2026+-green.svg)](https://developer.android.com/about/versions)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.8+-blue.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive Android application that helps students predict college admissions based on their rank and preferences, now featuring **Live Courses** as the primary revenue stream.

## 🚀 Features

### **🎯 Core Functionality**
- **Multi-Exam Support**: JEE Main, JEE Advance, AKTU B.Tech, AKTU CUET, HBTU B.Tech, WBJEE
- **Advanced Filtering**: Rank, State, Gender, Quota-based college matching
- **Real-time Data**: Live college data from GitHub Pages API
- **Offline Configuration**: Exam and filter options loaded from local assets

### **🔥 Live Courses (Primary Revenue Stream)**
- **Featured Course Display**: Prominent courses section on home page
- **Course Management**: Complete course catalog with search functionality
- **Dynamic Content**: Course images, pricing, mentors, enrollment dates
- **Direct Enrollment**: One-click course enrollment via external links
- **Revenue Optimization**: Featured courses, ratings, student counts for social proof

### **🎨 Modern UI/UX**
- **Material Design**: Clean, modern interface with gradients and animations
- **Responsive Layout**: Optimized for all screen sizes and orientations
- **Loading States**: Circular progress indicators and smooth transitions
- **Search Functionality**: Real-time search across courses and colleges

## 🏗️ Architecture

### **Clean Architecture Pattern**
```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
├─────────────────────────────────────────────────────────────┤
│  Activities │ Adapters │ ViewModels │ UI Components        │
├─────────────────────────────────────────────────────────────┤
│                    Domain Layer                             │
├─────────────────────────────────────────────────────────────┤
│  Services │ Use Cases │ Business Logic │ Validation        │
├─────────────────────────────────────────────────────────────┤
│                    Data Layer                               │
├─────────────────────────────────────────────────────────────┤
│  Repositories │ API Services │ Local Storage │ Models       │
└─────────────────────────────────────────────────────────────┘
```

### **Layer Breakdown**

#### **Presentation Layer**
- **Activities**: MainActivity, SelectRankActivity, ShowCollegesActivity, CoursesActivity
- **Adapters**: ExamAdapter, CollegeAdapter, CourseAdapter
- **UI Components**: Custom drawables, layouts, animations

#### **Domain Layer**
- **Services**: CollegeService, ExamService, CourseService, FilterOptionsService
- **Business Logic**: Filtering algorithms, validation, data processing
- **Use Cases**: College prediction, course management, user preferences

#### **Data Layer**
- **Repositories**: CollegeRepository, ExamRepository, CourseRepository
- **API Services**: Retrofit-based network calls
- **Models**: College, Exam, Course, UserPreferences, ApiResponse

## 🛠️ Technical Stack

### **Core Technologies**
- **Language**: Kotlin
- **Platform**: Android (API 26+)
- **Architecture**: MVVM with Clean Architecture
- **UI Framework**: Android Views with Material Design

### **Libraries & Dependencies**
- **Networking**: Retrofit 2.11.0, OkHttp 4.12.0
- **Image Loading**: Glide 4.16.0
- **Asynchronous**: Kotlin Coroutines 1.7.3
- **Lifecycle**: AndroidX Lifecycle 2.7.0
- **Firebase**: Messaging, Analytics, In-App Messaging
- **UI Components**: Material Design, CardView, RecyclerView

## 📊 Data Structure

### **Standardized College Data Format**
```json
{
  "institute": "College Name",
  "course": "Course Name",
  "state_quota": "HS/OS/AI",
  "quota": "General/EWS/OBC/SC/ST",
  "gender": "Both/Male/Female",
  "or": 12345,
  "cr": 67890,
  "state": "State Name"
}
```

### **Course Data Structure**
```json
{
  "name": "Course Name",
  "link": "Enrollment URL",
  "image": "Course Image URL",
  "price": "₹2,999",
  "mentors": ["Instructor 1", "Instructor 2"],
  "last_date": "31st Dec 2024",
  "description": "Course description",
  "duration": "6 Months",
  "rating": 4.8,
  "students_enrolled": 1250,
  "is_featured": true
}
```

### **Field Definitions**
- **state_quota**: HS (Home State), OS (Other State), AI (All India)
- **quota**: General, EWS, OBC, SC, ST categories
- **gender**: Both, Male, Female preferences
- **or/cr**: Opening Rank and Closing Rank
- **is_featured**: Featured course for home page display

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Arctic Fox or later
- Android SDK API 26+
- Kotlin 1.8+
- Internet connection for API calls

### **Installation**
1. Clone the repository:
   ```bash
   git clone https://github.com/maityamit/Aktu_Rank_Predictor.git
   ```

2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Configure Firebase (optional):
   - Add `google-services.json` to the `app/` directory
   - Enable Firebase services in the Firebase Console

5. Build and run the application

### **Configuration**
- **API Base URL**: `https://maityamit.github.io/Aktu_Rank_Predictor/`
- **Course Endpoint**: `/exams/courses.json`
- **College Endpoints**: `/exams/{examId}.json`
- **Local Assets**: `exams.json`, `filter_options.json`

## 📁 Project Structure

```
app/src/main/
├── java/akturankpredictorbyamitmaity/example/akturankpredictor/
│   ├── adapter/           # RecyclerView adapters
│   ├── chat/             # Chat functionality
│   ├── data/             # Data layer
│   │   ├── api/          # API services
│   │   ├── model/        # Data models
│   │   └── repository/   # Data repositories
│   ├── messaging/        # Firebase messaging
│   ├── service/          # Business logic services
│   └── *.kt              # Activity files
├── res/
│   ├── drawable/         # Custom drawables and backgrounds
│   ├── layout/           # UI layouts
│   └── values/           # Resources and themes
└── assets/
    ├── exams.json        # Exam configuration
    └── filter_options.json # Filter options
```

## ⚙️ Configuration Files

### **exams.json**
```json
[
  {
    "id": "jee_main",
    "name": "JEE Main",
    "endpoint": "jee_main",
    "description": "Joint Entrance Examination Main",
    "icon": "jee_main_logo",
    "isActive": true
  }
]
```

### **filter_options.json**
```json
{
  "states": ["All", "Uttar Pradesh", "Delhi", ...],
  "genders": ["All", "Male", "Female"],
  "quotas": ["All", "General", "Ews", "Obc", "Sc", "St"]
}
```

## 🎨 UI Components

### **Custom Drawables**
- **Gradient Backgrounds**: Modern gradient designs
- **Circular Progress**: Custom loading indicators
- **Button Styles**: Consistent button designs
- **Card Backgrounds**: Material Design cards

### **Color Scheme**
- **Primary**: #1976D2 (Blue)
- **Secondary**: #FF6B35 (Orange)
- **Success**: #4CAF50 (Green)
- **Warning**: #FF9800 (Orange)
- **Error**: #F44336 (Red)

## 🔄 State Management

### **Loading States**
- **ApiResponse**: Sealed class for Success/Error/Loading
- **Progress Indicators**: Circular progress bars
- **Error Handling**: User-friendly error messages

### **Data Flow**
1. **User Input** → Validation → API Call
2. **API Response** → Data Processing → UI Update
3. **Filtering** → Business Logic → Results Display

## 🧪 Testing

### **Unit Tests**
- Repository layer testing
- Service layer testing
- Data model validation

### **UI Tests**
- Activity navigation testing
- User interaction testing
- Layout validation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### **Development Guidelines**
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Maintain clean architecture principles

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developers

- **Amit Maity** - [LinkedIn](https://www.linkedin.com/in/maityamit)
- **Nitish Kumar** - [LinkedIn](https://www.linkedin.com/in/infiniteesh)

## 🙏 Acknowledgments

- **Material Design** for UI components
- **Retrofit** for network communication
- **Glide** for image loading
- **Firebase** for backend services

## 📞 Support

For support, email support@akturankpredictor.com or create an issue in the repository.

---

**Note**: This application is designed to help students make informed decisions about college admissions. The predictions are based on historical data and should be used as a reference only.

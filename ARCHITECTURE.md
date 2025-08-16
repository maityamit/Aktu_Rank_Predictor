# AKTU Rank Predictor - New Architecture

## Overview

The AKTU Rank Predictor has been refactored to use a scalable, maintainable architecture that supports multiple exams and provides better separation of concerns. The app now supports JEE Main data with a standardized data structure.

## Architecture Layers

### 1. Data Layer

#### Models (`data/model/`)
- **College.kt**: Data class for college information (JEE Main format)
- **Exam.kt**: Data class for exam configuration
- **UserPreferences.kt**: Data class for user filters
- **ApiResponse.kt**: Sealed class for API response handling
- **FilterOptions.kt**: Data class for filter options (states, genders, quotas)

#### API (`data/api/`)
- **CollegeApiService.kt**: Interface for college data API calls
- **ApiClient.kt**: Retrofit client configuration

#### Repository (`data/repository/`)
- **CollegeRepository.kt**: Handles college data operations
- **ExamRepository.kt**: Handles exam configuration operations
- **FilterOptionsRepository.kt**: Handles filter options operations

### 2. Service Layer (`service/`)
- **CollegeService.kt**: Business logic for college operations
- **ExamService.kt**: Business logic for exam operations
- **FilterOptionsService.kt**: Business logic for filter options operations

### 3. Presentation Layer
- **Activities**: UI components
- **Adapters**: RecyclerView adapters for data display

## Key Features

### Scalable Exam System
- Exams are configured via JSON files in assets
- Easy to add new exams by updating `app/src/main/assets/exams.json`
- Each exam has its own endpoint and data structure

### JEE Main Data Support
- **API Endpoint**: `https://maityamit.github.io/Aktu_Rank_Predictor/exams/jee_main.json`
- **Data Structure**: Optimized for JEE Main college data
- **Filtering Logic**: Advanced filtering based on JEE Main specific requirements

### Standardized Filter Options
- **States**: All Indian states and union territories (loaded from `filter_options.json`)
- **Gender**: All, Male, Female
- **Quota**: All, General, Ews, Obc, Sc, St

### Offline-First Approach
- Exam list loaded from assets (offline)
- Filter options loaded from assets (offline)
- Only college data is fetched from network

### User Filters
- **Rank**: User's entrance exam rank
- **State**: User's state preference (All Indian states)
- **Gender**: User's gender (All, Male, Female)
- **Quota**: User's quota preference (All, General, Ews, Obc, Sc, St)

### JEE Main Data Structure
Each college entry contains:
```json
{
  "institute": "College Name",
  "course": "Course Name",
  "quota": ["HS", "General"],
  "gender": "Both/Female",
  "or": 58341,
  "cr": 79511,
  "state": "Punjab"
}
```

### Filtering Logic for JEE Main

#### Rank Filtering
- `input_rank <= cr` (Closing Rank)

#### State Filtering
- **If `input_state == college_state`** → `state_quota` must be "HS" OR "AI"
- **If `input_state != college_state`** → `state_quota` must be "OS" OR "AI"
- **"AI" (All India)** is always included regardless of state

#### Gender Filtering
- `All` → Shows both "Both" and "Female" entries
- `Male` → Shows only "Both" entries
- `Female` → Shows only "Female" entries

#### Quota Filtering
- Exact match with quota array in JSON
- Maps: General, Ews, Obc, Sc, St

## Adding New Exams

1. **Update `app/src/main/assets/exams.json`**:
```json
{
  "id": "new_exam_id",
  "name": "New Exam Name",
  "endpoint": "new_exam_endpoint",
  "description": "Exam Description",
  "isActive": true
}
```

2. **Create data file**: `https://maityamit.github.io/Aktu_Rank_Predictor/exams/{exam_id}.json`

3. **Deploy to API server**: The app will automatically fetch the new exam configuration

## API Endpoints

- **GET `https://maityamit.github.io/Aktu_Rank_Predictor/exams/{examId}.json`**: Fetch colleges for specific exam

## Asset Files

### `app/src/main/assets/exams.json`
Contains the list of available exams with their configurations.

### `app/src/main/assets/filter_options.json`
Contains standardized filter options:
- All Indian states and union territories
- Gender options (All, Male, Female)
- Quota options (All, General, Ews, Obc, Sc, St)

## UI Display

The college list shows:
- **Institute**: College name
- **Course**: Course name
- **Quota**: Quota categories (comma-separated)
- **Gender**: Both/Female
- **OR/CR**: Opening Rank and Closing Rank
- **State**: College state

## Benefits of New Architecture

1. **Scalability**: Easy to add new exams without code changes
2. **Maintainability**: Clear separation of concerns
3. **Testability**: Each layer can be tested independently
4. **Flexibility**: Support for different data structures per exam
5. **Performance**: Efficient filtering and search capabilities
6. **Offline Support**: Exam list and filter options work without network
7. **Standardization**: Consistent filter options across all exams
8. **JEE Main Optimized**: Specifically designed for JEE Main data structure

## Migration Notes

This is a breaking change from the previous architecture. The app now:
- Fetches exam list from assets instead of network
- Uses standardized filter options loaded from assets
- Implements proper error handling and loading states
- Supports all Indian states and standardized quota categories
- Only college data requires network connectivity
- Optimized for JEE Main data structure and filtering logic

## Future Enhancements

- Caching layer for college data offline support
- Advanced filtering options
- College comparison features
- User preferences persistence
- Analytics and insights
- Multi-language support
- Support for other exam formats

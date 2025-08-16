# Task Management App - Room Database Implementation

## Overview
This is a Task Management app for a small team that demonstrates Room database implementation with various data access patterns.

## Architecture
- **Room Database**: Local SQLite database with Room ORM
- **Repository Pattern**: Abstracts data access from ViewModel
- **ViewModel**: Manages UI state and business logic
- **Jetpack Compose**: Modern UI implementation

## Database Schema

### Entities
1. **User** (id, name, email) - Primary key with unique index on id
2. **Project** (id, title, ownerId) - Foreign key to User
3. **Task** (id, description, projectId) - Foreign key to Project
4. **Attachment** (id, filePath, taskId) - Foreign key to Task

### Relationships
- **One-to-Many**: User → Project (one user can have many projects)
- **One-to-Many**: Project → Task (one project can have many tasks)
- **One-to-Many**: Task → Attachment (one task can have many attachments)

## Key Features

### TypeConverters
- **List<String> ↔ String**: Comma-separated string conversion
- **Date ↔ Long**: Timestamp conversion

### DAO Patterns
- **Suspend Functions**: One-time data snapshots (e.g., `getAllProjectsOnce()`)
- **Flow Functions**: Reactive data streams (e.g., `getAllProjectsFlow()`)

### Suspend vs Flow Difference
**Suspend functions** like `getAllProjectsOnce()` return a one-time snapshot of data as a `List<Project>` and are ideal for operations that don't need to observe changes over time, such as initial data loading or one-time data exports. **Flow functions** like `getAllProjectsFlow()` provide a continuous stream of data updates, automatically emitting new values whenever the underlying data changes, making them perfect for reactive UI updates that need to stay in sync with the database.

### Performance Testing
- **Room Query**: Uses Room's @Query annotation
- **Raw Query**: Uses @RawQuery for direct SQL execution
- **Performance comparison**: Measures execution time for both approaches

## Testing
The app includes comprehensive test functions that:
1. Insert sample data and log results
2. Test suspend vs Flow DAO methods
3. Compare Room vs Raw query performance
4. Demonstrate TypeConverters and relationships

## Logs
All database operations are logged with appropriate tags:
- `DB_TEST`: Sample data insertion and relationship testing
- `DAO_TEST`: Suspend vs Flow comparison
- `PERF`: Performance testing results
- `Repository`: Data access operations
- `ViewModel`: Business logic operations

# Storage Solutions Comparison Matrix

## Overview
This document compares three different storage solutions used in Android development: Files, DataStore, and Room. Each has different characteristics that make them suitable for different use cases.

## Comparison Table

| Storage Solution | Data Type | Storage Capacity | ACID Support | Backup Difficulty | Example from This App |
|------------------|------------|------------------|--------------|-------------------|----------------------|
| **Files** | Unstructured (binary/text) | Limited by device storage | No | Easy | Store image attachments, PDF documents, or exported project reports |
| **DataStore** | Structured (key-value pairs) | Limited by device storage | No | Medium | Store user preferences like dark mode, app settings, or user authentication tokens |
| **Room** | Structured (relational database) | Limited by device storage | **Yes** | Hard | Store Projects, Tasks, Users, and Attachments with many-to-many relationships |

## Detailed Analysis

### Files
- **Best for**: Binary data, large files, documents, images
- **Pros**: Simple, direct access, no overhead
- **Cons**: No data validation, no relationships, manual file management
- **Use case in this app**: Storing actual file attachments (images, PDFs, etc.)

### DataStore
- **Best for**: Configuration, preferences, simple key-value data
- **Pros**: Type-safe, reactive, handles data migration
- **Cons**: Limited to simple data structures, no complex queries
- **Use case in this app**: User settings, app configuration, theme preferences

### Room
- **Best for**: Complex data with relationships, structured queries
- **Pros**: Full SQL support, relationships, data validation, reactive
- **Cons**: More complex setup, larger APK size, learning curve
- **Use case in this app**: Core business logic - Projects, Tasks, Users, and their many-to-many relationships

## Database Schema Changes

### Many-to-Many Relationship
The app now uses a **many-to-many relationship** between Projects and Tasks:
- **Before**: One-to-many (one project could have many tasks, but each task belonged to only one project)
- **After**: Many-to-many (one project can have many tasks, and one task can belong to many projects)

### Implementation Details
- **Cross-reference table**: `ProjectTaskCrossRef` manages the many-to-many relationship
- **Task entity**: No longer has a `projectId` field
- **Relations**: Uses Room's `@Junction` annotation with the cross-reference table
- **Flexibility**: Tasks can now be shared across multiple projects
- **Note**: The `ProjectTaskCrossRef` is defined only in the entities package to avoid duplicate definitions

## Recommendations

- **Use Files** when you need to store actual file content or large binary data
- **Use DataStore** when you need to store simple configuration or user preferences
- **Use Room** when you need to store structured data with complex relationships and queries

## Performance Considerations

| Storage Solution | Read Performance | Write Performance | Memory Usage |
|------------------|------------------|-------------------|--------------|
| Files | Fast (direct access) | Fast (direct write) | Low |
| DataStore | Fast (cached) | Fast (async) | Low |
| Room | Fast (indexed) | Medium (transaction overhead) | Medium |

## Migration Strategy

When choosing between storage solutions, consider:
1. **Data complexity** - Simple data → DataStore, Complex data → Room
2. **Performance requirements** - High performance → Files, Structured queries → Room
3. **Data relationships** - No relationships → Files/DataStore, With relationships → Room
4. **Backup requirements** - Easy backup → Files, Complex backup → Room
5. **Relationship flexibility** - Simple relationships → Direct foreign keys, Complex relationships → Cross-reference tables

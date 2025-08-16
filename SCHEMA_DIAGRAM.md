# Database Schema UML Diagram

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│    User     │     │   Project   │     │    Task     │     │ Attachment  │
├─────────────┤     ├─────────────┤     ├─────────────┤     ├─────────────┤
│ id (PK)     │     │ id (PK)     │     │ id (PK)     │     │ id (PK)     │
│ name        │     │ title       │     │ description │     │ filePath    │
│ email       │     │ ownerId(FK) │────▶│ projectId   │────▶│ taskId (FK) │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
       ▲                     │                     │
       │                     │                     │
       └─────────────────────┴─────────────────────┘
                   1:N Relationship
```

## Relationships:
- **User → Project**: One-to-Many (1:N)
  - One user can have multiple projects
  - Each project belongs to one user (ownerId foreign key)

- **Project → Task**: One-to-Many (1:N)
  - One project can have multiple tasks
  - Each task belongs to one project (projectId foreign key)

- **Task → Attachment**: One-to-Many (1:N)
  - One task can have multiple attachments
  - Each attachment belongs to one task (taskId foreign key)

## Indices:
- `users.id` (unique)
- `projects.ownerId`
- `tasks.projectId`
- `attachments.taskId`

## Foreign Keys:
- `projects.ownerId` → `users.id` (CASCADE DELETE)
- `tasks.projectId` → `projects.id` (CASCADE DELETE)
- `attachments.taskId` → `tasks.id` (CASCADE DELETE)

# TinyTasks Nr. 1

## Task: A user can create and list tasks

### Summary

As a user, I want to **add a task to my task list with/without a due date** so that I can start tracking my tasks.

### Acceptance criteria

For this issue to be seen as **Done**, it must satisfy the following:

- the user can create a new task.
- If the task creation payload includes a due date, the due date should be persisted along other details of the task.
- the task list is sorted by due date
  - tasks with a due date go to the top of the list
  - tasks without a due date go to the bottom of the list

### Tech Approach

Please keep these things in mind:

- Backend: A TaskController already exists along with a TaskRepository, use this as a starting point. Extend the `Task` entity to include a field for due-date if necessary.
- Frontend: TaskFormComponent is used to enter task data, TaskListComponent is used to show all tasks
- Documentation for material datepicker: https://v11.material.angular.io/components/datepicker/overview

### Out of Scope

This ticket should not solve:

- to be discussed

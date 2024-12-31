# TinyTasks Nr. 3

## Task: Basic logging for endpoint latency 

### Summary

As an engineer, I want to log the time spent by each REST endpoint on the task service, so that I can tell
which endpoints needs optimization.
### Acceptance criteria

For this issue to be seen as **Done**, it must satisfy the following:

- A log entry of level INFO is added on console each time an endpoint is invoked.
- The log entry has the format `Timed: Executing <ClassName.methodName()> took x seconds`. 
  - An example for this for invoking the createTask endpoint on TaskController could be -
    `Timed: Executing TaskController.createTask() took 0.05 seconds`


### Tech Approach

Please keep these things in mind:

- Backend: We already have naive debug level logging every invocation of the endpoints on TaskController.
- Frontend: No frontend is required for this task.

### Out of Scope

This ticket should not solve:

- to be discussed

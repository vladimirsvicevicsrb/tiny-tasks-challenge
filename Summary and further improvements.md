## Summary for the Repository tiny-tasks-challenge

- Frontend:
    - Added a due date field in the frontend.
    - Added logic to upload multiple task files.
    - Introduced a task list item dialog component to display task file fields and logic for file
      download and file removal.
    - Upgraded Angular from v11 to v12.
    - Integrated ngx-mat-timepicker to include a time section for the due date.
    - Organized the codebase by creating separate folders for services, models, and components.
    - Adjusted README.md to specify the correct Node.js version for Angular v12.


- Backend:
    - Added logic to upload multiple task files.
    - Implemented logic to delete task and individual task files.
    - Added Java documentation, OpenAPI specifications
    - Introduced simple logging to measure execution time (in milliseconds) for each REST endpoint
      using AOP.
        - Implemented AOP logging to flexibly log execution time for all endpoints in annotated
          classes.
    - Added full junit test coverage
    - Added integration test

General:

- Added a document outlining further improvements for the project.
- Added end-to-end (E2E) Cypress tests for task-form fields.

## Further improvements to be implemented

### Backend

- [ ] upgrade spring boot to the latest version
    - [ ] add docker compose support
- [ ] upgrade java to 21
- [ ] upgrade db from h2 to postgres
    - [ ] add integration tests
- [ ] use S3 / localstack to store files
- [ ] implement more robust observability solution beyond simple logging using Micrometer and
  Prometheus for metrics and Grafana for their visualization
    - I implemented simple logging using AOP and logged in milliseconds as found better suitable
      than seconds.

### Frontend

- [ ] upgrade angular to the latest version (v19)
    - [ ] use angular material timepicker made available since angular material (v19)
- [ ] add prettier / es-lint for formatting
- [ ] add tailwind for easier styling
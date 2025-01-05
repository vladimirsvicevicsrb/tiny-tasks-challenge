## List of further improvements to be implemented

### Backend

- [ ] upgrade spring boot to the latest version
    - [ ] add docker compose support
- [ ] upgrade java to 21
- [ ] upgrade db from h2 to postgres
    - [ ] add integration tests
- [ ] use S3 / localstack to store files
- [ ] implement more robust observability solution beyond simple logging using Micrometer and
  Prometheus for metrics and Grafana for their visualization
  - I implemented simple logging using AOP and logged in milliseconds as found better suitable than seconds.

### Frontend

- [ ] upgrade angular to the latest version (v19)
    - [ ] use angular material timepicker made available since angular material (v19)
- [ ] add prettier / es-lint for formatting
- [ ] add tailwind for easier styling
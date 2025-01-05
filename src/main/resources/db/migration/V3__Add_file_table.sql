CREATE TABLE task_file (
    id VARCHAR(36) CONSTRAINT file_id_pkey PRIMARY KEY,
    task_id VARCHAR(36) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    file_content BLOB,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);
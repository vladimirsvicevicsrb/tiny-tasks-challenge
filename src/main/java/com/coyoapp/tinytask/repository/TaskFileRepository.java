package com.coyoapp.tinytask.repository;

import com.coyoapp.tinytask.domain.TaskFile;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskFileRepository extends ListCrudRepository<TaskFile, String> {

  Optional<TaskFile> findByTaskIdAndFileName(String taskId, String fileName);
}

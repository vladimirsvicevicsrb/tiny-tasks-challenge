import { Observable } from "rxjs";

import { Task } from "app/tasks/models/task.model";

/**
 * Service interface for implementations that handle tiny tasks.
 */
export interface TaskService {
  /**
   * Returns the list of all tasks.
   *
   * @returns an `Observable` holding the list of tasks
   */
  getAll(): Observable<Task[]>;

  /**
   * Adds a new task to the list of tasks.
   *
   * @param name the task's name
   * @returns an `Observable` holding the created task
   */
  create(formData: FormData): Observable<Task>;

  /**
   * Removes the task with the given ID from the list of tasks.
   *
   * @param id the ID of the task to be removed
   * @returns an empty `Observable`
   */
  delete(id: string): Observable<void>;

  /**
   * Returns the download URL for the file with the given ID.
   * @param fileId the ID of the file
   * @returns the download URL
   */
  getFileDownloadUrl(fileId: string): string;

  /**
   * Deletes the file with the given ID.
   * @param fileId the ID of the file
   * @returns an empty `Observable`
   */
  deleteFile(fileId: string): Observable<void>;

  /**
   * Toggles the completion status of a task.
   * @param taskId the ID of the task
   * @returns an `Observable` holding the updated task
   */
  toggleTaskCompletion(taskId: string): Observable<Task>;
}

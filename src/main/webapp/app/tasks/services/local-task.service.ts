import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { v4 as uuid } from "uuid";

import { TaskService } from "./task.service";
import { TaskRequest } from "../models/task-request.model";
import { Task } from "../models/task.model";

@Injectable()
export class LocalTaskService implements TaskService {
  public static readonly STORAGE_KEY: string = "tiny.tasks";

  getAll(): Observable<Task[]> {
    return of(this.readTasks());
  }

  create(formData: FormData): Observable<Task> {
    const taskRequestBlob = formData.get("taskRequest") as Blob;

    if (!taskRequestBlob) {
      throw new Error("No taskRequest data found in FormData");
    }

    return new Observable<Task>((observer) => {
      taskRequestBlob
        .text()
        .then((text) => {
          const taskRequest: TaskRequest = JSON.parse(text);

          const tasks = this.readTasks();
          const task: Task = {
            id: uuid(),
            name: taskRequest.name,
            dueDate: taskRequest.dueDate,
            completed: false,
          };

          tasks.push(task);
          this.writeTasks(tasks);

          observer.next(task);
          observer.complete();
        })
        .catch((err) => {
          observer.error(
            new Error(`Failed to process taskRequest: ${err.message}`)
          );
        });
    });
  }

  delete(id: string): Observable<void> {
    const tasks = this.readTasks();
    const index = tasks.findIndex((task) => task.id === id);
    if (index !== -1) {
      tasks.splice(index, 1);
      this.writeTasks(tasks);
    }
    return of(null);
  }

  private readTasks(): Task[] {
    const tasks = localStorage.getItem(LocalTaskService.STORAGE_KEY);
    return tasks ? JSON.parse(tasks) : [];
  }

  private writeTasks(tasks: Task[]): void {
    localStorage.setItem(LocalTaskService.STORAGE_KEY, JSON.stringify(tasks));
  }

  getFileDownloadUrl(fileId: string): string {
    // not important for local test
    return "";
  }

  deleteFile(fileId: string): Observable<void> {
    // not important for local test
    return of(null);
  }

  toggleTaskCompletion(taskId: string): Observable<Task> {
    const tasks = this.readTasks();
    const taskIndex = tasks.findIndex(task => task.id === taskId);
    
    if (taskIndex === -1) {
      throw new Error(`Task with id ${taskId} not found`);
    }

    // Toggle completion status
    tasks[taskIndex].completed = !tasks[taskIndex].completed;
    
    // Set completion timestamp
    if (tasks[taskIndex].completed) {
      tasks[taskIndex].completedAt = new Date().toISOString();
    } else {
      tasks[taskIndex].completedAt = undefined;
    }

    this.writeTasks(tasks);
    return of(tasks[taskIndex]);
  }
}

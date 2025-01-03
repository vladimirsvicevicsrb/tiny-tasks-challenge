import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { v4 as uuid } from "uuid";

import { TaskService } from "./task.service";
import { TaskRequest } from "../models/task-request.model";
import { Task } from "../models/task.model";

@Injectable()
export class LocalTaskService implements TaskService {
  private static readonly STORAGE_KEY: string = "tiny.tasks";

  getAll(): Observable<Task[]> {
    return of(this.readTasks());
  }

  create(formData: FormData): Observable<Task> {
    const taskRequestObj = formData.get("taskRequest") as string;
    const taskRequest: TaskRequest = JSON.parse(taskRequestObj);

    const tasks = this.readTasks();
    const task = {
      id: uuid(),
      name: taskRequest.name,
      dueDate: taskRequest.dueDate,
    };
    tasks.push(task);
    this.writeTasks(tasks);
    return of(task);
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
}

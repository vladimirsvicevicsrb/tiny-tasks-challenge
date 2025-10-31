import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Task } from '../models/task.model';

/**
 * Service for managing task state reactively across components.
 * Eliminates the need for polling and enables real-time updates.
 */
@Injectable({
  providedIn: 'root'
})
export class TaskStateService {
  private tasksSubject = new BehaviorSubject<Task[]>([]);
  private selectedTaskSubject = new BehaviorSubject<Task | null>(null);

  // Public observables that components can subscribe to
  public readonly tasks$: Observable<Task[]> = this.tasksSubject.asObservable();
  public readonly selectedTask$: Observable<Task | null> = this.selectedTaskSubject.asObservable();

  /**
   * Update the tasks array
   */
  updateTasks(tasks: Task[]): void {
    this.tasksSubject.next([...tasks]);
  }

  /**
   * Update a specific task in the tasks array
   */
  updateTask(updatedTask: Task): void {
    const currentTasks = this.tasksSubject.value;
    const taskIndex = currentTasks.findIndex(task => task.id === updatedTask.id);
    
    if (taskIndex !== -1) {
      const newTasks = [...currentTasks];
      newTasks[taskIndex] = updatedTask;
      this.tasksSubject.next(newTasks);
    }
  }

  /**
   * Set the currently selected task (for dialog)
   */
  setSelectedTask(task: Task | null): void {
    this.selectedTaskSubject.next(task);
  }

  /**
   * Get current tasks synchronously
   */
  getCurrentTasks(): Task[] {
    return this.tasksSubject.value;
  }

  /**
   * Get a specific task by ID
   */
  getTaskById(taskId: string): Task | undefined {
    return this.tasksSubject.value.find(task => task.id === taskId);
  }

  /**
   * Initialize the service with tasks from parent component
   */
  initializeTasks(tasks: Task[]): void {
    this.tasksSubject.next([...tasks]);
  }

  /**
   * Replace all tasks in the service (used when parent component refreshes data)
   */
  replaceAllTasks(tasks: Task[]): void {
    this.tasksSubject.next([...tasks]);
  }
}
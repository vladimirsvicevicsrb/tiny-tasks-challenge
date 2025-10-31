import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Inject,
  Input,
  OnChanges,
  OnDestroy,
  Output,
} from "@angular/core";

import { Task } from "../../models/task.model";
import { TaskFile } from "../../models/task-file.model";
import { TaskService } from "app/tasks/services/task.service";
import { TaskStateService } from "../../services/task-state.service";
import { MatDialog } from "@angular/material/dialog";
import { TaskListItemDialogComponent } from "./task-list-item/task-list-item-dialog.component";
import { Subscription } from "rxjs";

/**
 * A list of tiny tasks.
 */
@Component({
  selector: "tiny-task-list",
  templateUrl: "./task-list.component.html",
  styleUrls: ["./task-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskListComponent implements OnChanges, OnDestroy {
  @Input() public tasks: Task[];

  @Output() public deleted: EventEmitter<Task> = new EventEmitter();
  @Output() public updated: EventEmitter<Task> = new EventEmitter();

  onFileDeletedSubscription: Subscription;
  deleteTaskSubscription: Subscription;

  constructor(
    @Inject("TaskService") private taskService: TaskService,
    private taskStateService: TaskStateService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef
  ) {
    // Subscribe to reactive task updates
    this.taskStateService.tasks$.subscribe(tasks => {
      // Update local tasks array to match TaskStateService for template binding
      this.tasks = tasks;
      this.cdr.markForCheck();
    });
  }
  
  ngOnDestroy(): void {
    this.onFileDeletedSubscription.unsubscribe();
    this.deleteTaskSubscription.unsubscribe();
  }
  
  // Update TaskStateService when parent provides new tasks
  ngOnChanges(): void {
    if (this.tasks) {
      this.taskStateService.replaceAllTasks(this.tasks);
    }
  }

  protected openTaskDetails(task: Task): void {
    // Set the selected task in the state service for reactive updates
    this.taskStateService.setSelectedTask(task);
    
    let dialogRef = this.dialog.open(TaskListItemDialogComponent, {
      width: "600px",
      data: task,
      disableClose: false, // Allow ESC key to close dialog
      autoFocus: false
    });
    
    this.onFileDeletedSubscription = dialogRef.componentInstance.onFileDeleted.subscribe(() => {
      this.deleted.emit();
    });
    
    // Clean up when dialog closes
    dialogRef.afterClosed().subscribe(() => {
      this.taskStateService.setSelectedTask(null);
    });
  }

  protected getDueDateFormatted(dueDate: string): string {
    if (!dueDate) {
      return "No Due Date";
    }

    const now = new Date();
    const targetDate = new Date(dueDate);

    // Calculate time difference in milliseconds
    const timeDiff = targetDate.getTime() - now.getTime();

    // Calculate days, hours, and minutes
    const totalMinutes = Math.ceil(timeDiff / (1000 * 60));
    const days = Math.floor(totalMinutes / (60 * 24));
    const hours = Math.floor((totalMinutes % (60 * 24)) / 60);
    const minutes = totalMinutes % 60;

    if (totalMinutes < 0) {
      // Overdue logic
      const overdueMinutes = Math.abs(totalMinutes);
      const overdueDays = Math.floor(overdueMinutes / (60 * 24));
      const overdueHours = Math.floor((overdueMinutes % (60 * 24)) / 60);
      const overdueMins = overdueMinutes % 60;

      return `Overdue by ${
        overdueDays > 0
          ? `${overdueDays} day${overdueDays > 1 ? "s" : ""}, `
          : ""
      }${
        overdueHours > 0
          ? `${overdueHours} hour${overdueHours > 1 ? "s" : ""}, `
          : ""
      }${overdueMins} minute${overdueMins > 1 ? "s" : ""}`;
    }

    // Due date logic
    if (days === 0 && hours === 0) {
      return `In ${minutes} minute${minutes > 1 ? "s" : ""}`;
    } else if (days === 0) {
      return `In ${hours} hour${hours > 1 ? "s" : ""}, ${minutes} minute${
        minutes > 1 ? "s" : ""
      }`;
    } else {
      return `In ${days} day${days > 1 ? "s" : ""}, ${hours} hour${
        hours > 1 ? "s" : ""
      }, ${minutes} minute${minutes > 1 ? "s" : ""}`;
    }
  }

  delete(task: Task): void {
    this.deleteTaskSubscription = this.taskService.delete(task.id).subscribe(() => {
      this.deleted.emit(task);
    });
  }

  toggleTaskCompletion(task: Task): void {
    this.taskService.toggleTaskCompletion(task.id).subscribe((updatedTask: Task) => {
      // Update via TaskStateService for reactive sync across components
      this.taskStateService.updateTask(updatedTask);
      // Notify parent component to refresh data
      this.updated.emit(updatedTask);
    });
  }

  protected downloadFile(file: TaskFile): string {
    if (!file) return;
    return this.taskService.getFileDownloadUrl(file.id);
  }
}

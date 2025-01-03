import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Inject,
  Input,
  OnDestroy,
  Output,
} from "@angular/core";

import { Task } from "../../models/task.model";
import { TaskFile } from "../../models/task-file.model";
import { TaskService } from "app/tasks/services/task.service";
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
export class TaskListComponent implements OnDestroy {
  @Input() public tasks: Task[];

  @Output() public deleted: EventEmitter<Task> = new EventEmitter();

  private onFileDeletedSubscription: Subscription;
  private deleteTaskSubscription: Subscription;

  constructor(
    @Inject("TaskService") private taskService: TaskService,
    private dialog: MatDialog
  ) {}
  ngOnDestroy(): void {
    this.onFileDeletedSubscription.unsubscribe();
    this.deleteTaskSubscription.unsubscribe();
  }

  protected openTaskDetails(task: Task): void {
    let dialogRef = this.dialog.open(TaskListItemDialogComponent, {
      width: "600px",
      data: task,
      disableClose: true,
      autoFocus: false
    });
    this.onFileDeletedSubscription = dialogRef.componentInstance.onFileDeleted.subscribe(() => {
      this.deleted.emit();
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
      this.deleted.emit();
    });
  }

  protected downloadFile(file: TaskFile): string {
    if (!file) return;
    return this.taskService.getFileDownloadUrl(file.id);
  }
}

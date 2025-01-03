import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Inject,
  Input,
  Output,
} from "@angular/core";

import { Task } from "../models/task.model";
import { TaskService } from "../task.service";
import { TaskFile } from "../models/task-file.model";

/**
 * A list of tiny tasks.
 */
@Component({
  selector: "tiny-task-list",
  templateUrl: "./task-list.component.html",
  styleUrls: ["./task-list.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskListComponent {
  @Input() tasks: Task[];

  @Output() deleted: EventEmitter<Task> = new EventEmitter();

  constructor(@Inject("TaskService") private taskService: TaskService) {}

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
    this.taskService.delete(task.id).subscribe(() => {
      this.deleted.emit(task);
    });
  }

  protected downloadFile(file: TaskFile): string {
    if (!file) return;
    return this.taskService.getFileDownloadUrl(file.id);
  }

  protected formatFileSize(sizeInBytes: number): string {
    const units = ["B", "KB", "MB", "GB"];
    let i = 0;
    while (sizeInBytes >= 1024 && i < units.length - 1) {
      sizeInBytes /= 1024;
      i++;
    }
    return `${sizeInBytes.toFixed(2)} ${units[i]}`;
  }
}

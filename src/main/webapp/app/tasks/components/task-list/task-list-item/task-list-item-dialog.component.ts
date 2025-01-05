import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Inject,
  ViewEncapsulation,
} from "@angular/core";
import { TaskFile } from "app/tasks/models/task-file.model";
import { TaskService } from "app/tasks/services/task.service";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Task } from "app/tasks/models/task.model";
import * as moment from "moment";

@Component({
  selector: "tiny-task-list-item-dialog",
  templateUrl: "./task-list-item-dialog.component.html",
  styleUrls: ["./task-list-item-dialog.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class TaskListItemDialogComponent {
  
  public onFileDeleted = new EventEmitter();

  constructor(
    @Inject(MAT_DIALOG_DATA) public task: Task,
    @Inject("TaskService") private taskService: TaskService,
    private dialogRef: MatDialogRef<TaskListItemDialogComponent>,
    private cdr: ChangeDetectorRef
  ) {}

  protected downloadFile(event: Event, file: TaskFile): void {
    event.preventDefault();
    const url = this.taskService.getFileDownloadUrl(file.id);
    window.open(url, "_blank");
  }

  protected deleteFile(event: Event, file: TaskFile): void {
    event.preventDefault();
    this.taskService.deleteFile(file.id).subscribe(() => {
      this.task.files = this.task.files.filter((f) => f.id !== file.id);
      this.cdr.detectChanges();
      this.onFileDeleted.emit();
    });
  }

  protected close(): void {
    this.dialogRef.close();
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

  protected getFormattedDueDate(dueDate: string): string {
    return moment(dueDate).format('llll');
  }
}

import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Inject,
  OnDestroy,
  OnInit,
  ViewEncapsulation,
} from "@angular/core";
import { TaskFile } from "app/tasks/models/task-file.model";
import { TaskService } from "app/tasks/services/task.service";
import { TaskStateService } from "../../../services/task-state.service";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Task } from "app/tasks/models/task.model";
import * as moment from "moment";
import { Subscription } from "rxjs";

@Component({
  selector: "tiny-task-list-item-dialog",
  templateUrl: "./task-list-item-dialog.component.html",
  styleUrls: ["./task-list-item-dialog.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class TaskListItemDialogComponent implements OnInit, OnDestroy {
  
  public onFileDeleted = new EventEmitter();
  // Keep track of the current task being displayed
  private currentTask: Task;
  private taskUpdateSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public task: Task,
    @Inject("TaskService") private taskService: TaskService,
    private taskStateService: TaskStateService,
    private dialogRef: MatDialogRef<TaskListItemDialogComponent>,
    private cdr: ChangeDetectorRef
  ) {
    // Store the initial task
    this.currentTask = { ...task };
  }

  ngOnInit(): void {
    // Subscribe to reactive task updates from TaskStateService
    this.taskUpdateSubscription = this.taskStateService.selectedTask$.subscribe(selectedTask => {
      if (selectedTask && selectedTask.id === this.currentTask.id) {
        // Update the current task with the latest data
        this.currentTask = { ...selectedTask };
        this.cdr.markForCheck();
      }
    });
  }

  ngOnDestroy(): void {
    if (this.taskUpdateSubscription) {
      this.taskUpdateSubscription.unsubscribe();
    }
  }

  protected downloadFile(event: Event, file: TaskFile): void {
    event.preventDefault();
    const url = this.taskService.getFileDownloadUrl(file.id);
    window.open(url, "_blank");
  }

  protected deleteFile(event: Event, file: TaskFile): void {
    event.preventDefault();
    this.taskService.deleteFile(file.id).subscribe(() => {
      this.currentTask.files = this.currentTask.files.filter((f) => f.id !== file.id);
      this.cdr.markForCheck();
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
    return dueDate ? moment(dueDate).format('llll') : "No Due Date";
  }

  protected formatCompletionDate(completedAt: string): string {
    return completedAt ? moment(completedAt).format('llll') : "";
  }

  // Provide access to the current task data
  protected get taskData(): Task {
    return this.currentTask;
  }
}

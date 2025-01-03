import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Inject,
  Output,
  ViewChild,
} from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";

import * as moment from "moment";
import { Task } from "../models/task.model";
import { TaskService } from "../task.service";

/**
 * A form to create tiny tasks.
 */
@Component({
  selector: "tiny-task-form",
  templateUrl: "./task-form.component.html",
  styleUrls: ["./task-form.component.scss"],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskFormComponent {
  @Output() created: EventEmitter<Task> = new EventEmitter();

  @ViewChild("fileInput") fileInputElement: ElementRef;

  protected currentDate: Date = new Date();
  protected selectedFiles: File[] = [];
  protected errorMessage: string;

  taskForm: FormGroup = new FormGroup({
    name: new FormControl("", Validators.required),
    date: new FormControl(null), // Optional date field
    time: new FormControl(null), // Optional time field
  });

  constructor(
    @Inject("TaskService") private taskService: TaskService,
    private cdr: ChangeDetectorRef
  ) {}

  protected onSubmit(): void {
    const formData = new FormData();

    const { name, date, time } = this.taskForm.value;
    const dueDate = this.formatDueDate(date, time);

    const taskData = {
      name,
      dueDate,
    };

    formData.append(
      "taskRequest",
      new Blob([JSON.stringify(taskData)], {
        type: "application/json",
      })
    );

    Array.from(this.selectedFiles).forEach((file) => {
      formData.append("files", file, file.name);
    });

    this.taskService.create(formData).subscribe({
      next: (task) => {
        this.created.emit(task);
        this.taskForm.reset();
        this.selectedFiles = [];
        this.fileInputElement.nativeElement.value = "";
      },
      error: (err) => {
        this.errorMessage = err.error.message;
        this.cdr.detectChanges();
      },
    });
  }

  protected onFilesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.selectedFiles = Array.from(input.files);
    }
  }

  private formatDueDate(date: Date | null, time: Date | null): string | null {
    if (!date) {
      return null; // Send null if no date is selected
    }

    const dateString = moment(date).format("YYYY-MM-DD");
    const timeString = time ? moment(time).format("HH:mm") : "00:00";
    const dateTime = `${dateString}T${timeString}:00`;

    return dateTime; // Format as "YYYY-MM-DDT00:00:00"
  }
}

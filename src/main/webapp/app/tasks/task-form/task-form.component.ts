import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Inject,
  Output,
} from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";

import * as moment from "moment";
import { Task } from "../task.model";
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

  protected currentDate: Date = new Date();

  taskForm: FormGroup = new FormGroup({
    name: new FormControl("", Validators.required),
    date: new FormControl(null), // Optional date field
    time: new FormControl(null), // Optional time field
  });

  constructor(@Inject("TaskService") private taskService: TaskService) {}

  onSubmit(): void {
    const { name, date, time } = this.taskForm.value;
    const dueDate = this.formatDueDate(date, time);

    const taskData = {
      name,
      dueDate
    };

    this.taskService.create(taskData).subscribe((task) => {
      this.created.emit(task);
      this.taskForm.reset();
    });
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

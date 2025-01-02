import { ChangeDetectionStrategy, Component, EventEmitter, Inject, Input, Output } from '@angular/core';

import { Task } from '../task.model';
import { TaskService } from '../task.service';

/**
 * A list of tiny tasks.
 */
@Component({
  selector: 'tiny-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskListComponent {

  @Input() tasks: Task[];

  @Output() deleted: EventEmitter<Task> = new EventEmitter();

  constructor(@Inject('TaskService') private taskService: TaskService) { }

  protected getDueDateFormatted(dueDate: string): string {
    if (!dueDate) {
      return 'No Due Date';
    }

    const now = new Date();

    // Calculate time difference in milliseconds
    const timeDiff =  new Date(dueDate).getTime() - now.getTime();

    // Calculate days difference
    const daysDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));

    if (daysDiff === 0) {
      return 'Today';
    } else if (daysDiff > 0) {
      return `In ${daysDiff} day${daysDiff > 1 ? 's' : ''}`;
    } else {
      return 'Overdue';
    }
  }

  delete(task: Task): void {
    this.taskService.delete(task.id).subscribe(() => {
      this.deleted.emit(task);
    });
  }
}

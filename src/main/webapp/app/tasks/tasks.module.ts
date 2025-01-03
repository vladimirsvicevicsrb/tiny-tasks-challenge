import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatTimepickerModule, MatNativeDateTimeModule } from "@dhutaryan/ngx-mat-timepicker";
import { MatDialogModule } from '@angular/material/dialog';

import { TaskFormComponent } from './components/task-form/task-form.component';
import { TaskListComponent } from './components/task-list/task-list.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TaskListItemDialogComponent } from './components/task-list/task-list-item/task-list-item-dialog.component';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [TaskFormComponent, TaskListComponent, TaskListItemDialogComponent],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatTimepickerModule,
        MatNativeDateTimeModule,
        MatFormFieldModule,
        MatDialogModule
    ],
  exports: [TaskFormComponent, TaskListComponent, TaskListItemDialogComponent],
  providers: [
      MatNativeDateModule
  ]
})
export class TasksModule { }

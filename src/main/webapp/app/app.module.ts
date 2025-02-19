import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';

import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { BASE_URL } from './app.tokens';
import { DefaultTaskService } from './tasks/services/default-task.service';
import { LocalTaskService } from './tasks/services/local-task.service';
import { TasksModule } from './tasks/tasks.module';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatToolbarModule,
    MatDialogModule,
    MatFormFieldModule,
    TasksModule,
    HttpClientModule
  ],
  providers: [
    {provide: BASE_URL, useValue: '/api'},
    {provide: 'TaskService', useClass: (environment.useLocalStorage) ? LocalTaskService : DefaultTaskService}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

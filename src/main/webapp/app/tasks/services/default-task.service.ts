import { HttpClient } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { TaskService } from "./task.service";
import { BASE_URL } from "app/app.tokens";
import { Task } from "../models/task.model";

@Injectable()
export class DefaultTaskService implements TaskService {
  constructor(
    private http: HttpClient,
    @Inject(BASE_URL) private baseUrl: string
  ) {}

  create(formData: FormData): Observable<Task> {
    return this.http.post<Task>(this.baseUrl + "/tasks", formData);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(this.baseUrl + "/tasks/" + id);
  }

  getAll(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl + "/tasks");
  }

  getFileDownloadUrl(fileId: string): string {
    return `${this.baseUrl}/files/${fileId}/download`;
  }

  deleteFile(fileId: string): Observable<void> {
    return this.http.delete<void>(this.baseUrl + "/files/" + fileId);
  }

}

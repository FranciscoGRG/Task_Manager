import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TaskModel } from '../models/task-model';
import { TaskRequestModel } from '../models/task-request-model';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private apiUrl = 'http://localhost:8082/tasks';
  private readonly TOKEN_KEY = 'jwt_token';
  private http = inject(HttpClient);

  createTask(task: TaskRequestModel, file: File | null): Observable<any> {
    const formData = new FormData();
    formData.append('task', new Blob([JSON.stringify(task)], { type: 'application/json' }));

    if (file) {
      formData.append('file', file);
    }

    const headers = new HttpHeaders();

    return this.http.post(this.apiUrl, formData, { headers });
  }

  getTasks(): Observable<TaskModel[]> {
    return this.http.get<TaskModel[]>(`${this.apiUrl}`);
  }
}

import { Component, inject, OnInit, signal } from '@angular/core';
import { TaskService } from '../../services/task-service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TaskModel } from '../../models/task-model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './task-component.html',
})
export class TaskComponent implements OnInit {
  private taskService = inject(TaskService);
  private fb = inject(FormBuilder);
  tasks = signal<TaskModel[]>([])
  taskForm!: FormGroup;

  selectedFile: File | null = null;
  response: any;

  ngOnInit(): void {
    this.taskService.getTasks().subscribe({
      next: (res) => {
        this.tasks.set(res);
      },
      error: (err) => {
        console.error('Error al obtener tareas:', err);
      },
    });

    this.taskForm = this.fb.group({
      title: [''],
      description: [''],
      dueDate: [''],
      completed: [false],
    });
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  submitTask() {
    if (!this.taskForm.valid) {
      return;
    }

    const task = this.taskForm.value;

    this.taskService.createTask(task, this.selectedFile).subscribe({
      next: (res) => {
        this.response = res;
        console.log('Tarea creada:', res);
        this.tasks.update(tasks => [...tasks, res]); // Add new task to list
        this.taskForm.reset(); // Reset form
        this.selectedFile = null; // Reset file
      },
      error: (err) => {
        console.error('Error al crear tarea:', err);
        this.response = err;
      },
    });
  }

  downloadAttachment(attachmentUrl: string) {
    this.taskService.getAttachment(attachmentUrl).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = attachmentUrl.split('/').pop() || 'download';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Error downloading attachment:', err);
      }
    });
  }
}

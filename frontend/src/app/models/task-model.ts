export interface TaskModel {
    id: number,
    userId: number,
    title: string,
    description: string,
    dueDate: string,
    completed: boolean,
    attachmentUrl: string
}

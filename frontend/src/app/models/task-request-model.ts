export interface TaskRequestModel {
    title: string;
    description: string;
    dueDate: string;
    completed: boolean;
    attachmentUrl?: string;
}

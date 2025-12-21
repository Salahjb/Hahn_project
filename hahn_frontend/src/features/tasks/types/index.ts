
export type TaskStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';


export interface Task {
  id: number;
  title: string;
  description: string;
  status: TaskStatus;
  dueDate: string;
//   priority: string; // If you added this in backend, otherwise remove
  projectId: number;
}

export interface CreateTaskRequest {
  title: string;
  description: string;
  status: TaskStatus;
  dueDate: string; // YYYY-MM-DD
  projectId: number;
}
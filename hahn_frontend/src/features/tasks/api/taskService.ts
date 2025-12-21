import api from '../../../lib/axios';
import { type Task, type CreateTaskRequest, type TaskStatus } from '../types';

// Spring Boot Page Structure
export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; 

  last: boolean;  
  first: boolean; 
  empty: boolean; 
}

// Filters
interface TaskQueryParams {
  projectId: number;
  page?: number;
  search?: string;
  status?: string;
}
// Define what data we can update 
export type UpdateTaskRequest = Partial<CreateTaskRequest> & {
    status?: TaskStatus;
}

export const taskService = {
  getByProject: async ({ projectId, page = 0, search = '', status = '' }: TaskQueryParams): Promise<PageResponse<Task>> => {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', '5'); 
    if (search) params.append('search', search);
    if (status && status !== 'ALL') params.append('status', status);

    const response = await api.get<PageResponse<Task>>(`/projects/${projectId}/tasks?${params.toString()}`);
    return response.data;
  },

  create: async (data: CreateTaskRequest): Promise<Task> => {
    const response = await api.post<Task>(`/projects/${data.projectId}/tasks`, data);
    return response.data;
  },
  
  updateStatus: async (taskId: number, status: TaskStatus): Promise<Task> => {
      const response = await api.put<Task>(`/tasks/${taskId}`, { status });
      return response.data;
  },

  update: async (taskId: number, data: UpdateTaskRequest): Promise<Task> => {
      const response = await api.put<Task>(`/tasks/${taskId}`, data);
      return response.data;
  },

  delete: async (taskId: number): Promise<void> => {
      await api.delete(`/tasks/${taskId}`);
  }
};
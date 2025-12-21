import api from '../../../lib/axios';
import { type Project, type CreateProjectRequest } from '../types';

export const projectService = {
    // GET /api/projects
    getAll: async (): Promise<Project[]> => {
        const response = await api.get<Project[]>('/projects');
        return response.data;
    },

    // GET /api/projects/{id}
    getOne: async (id: number): Promise<Project> => {
        const response = await api.get<Project>(`/projects/${id}`);
        return response.data;
    },

    // POST /api/projects
    create: async (data: CreateProjectRequest): Promise<Project> => {
        const response = await api.post<Project>('/projects', data);
        return response.data;
    },

    // DELETE /api/projects/{id}
    delete: async (id: number): Promise<void> => {
        await api.delete(`/projects/${id}`);
    },
};
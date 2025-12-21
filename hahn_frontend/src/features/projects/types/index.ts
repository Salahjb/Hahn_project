export interface Project {
    id: number;
    title: string;
    description: string;
    createdAt: string; // Dates usually come as strings from JSON
    updatedAt: string;
    totalTasks: number;
    completedTasks: number;
    progress: number;
}

export interface CreateProjectRequest {
    title: string;
    description: string;
}
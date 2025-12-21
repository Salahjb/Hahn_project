import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { projectService } from '../api/projectService';
import { type CreateProjectRequest } from '../types';
import { Plus, Trash2, AlertCircle, LogOut } from 'lucide-react'; 
import { useAuthStore } from '../../../stores/useAuthStore'; 

export const ProjectList = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const logout = useAuthStore((state) => state.logout);
    const { register, handleSubmit, reset } = useForm<CreateProjectRequest>();

    const { data: projects, isLoading, isError } = useQuery({
        queryKey: ['projects'],
        queryFn: projectService.getAll,
    });

    const createMutation = useMutation({
        mutationFn: projectService.create,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['projects'] });
            reset();
        },
    });

    const deleteMutation = useMutation({
        mutationFn: projectService.delete,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['projects'] });
        },
    });

    const onSubmit = (data: CreateProjectRequest) => {
        createMutation.mutate(data);
    };

    const handleLogout = () => { 
        if (window.confirm('Are you sure you want to log out?')) {
            logout();
            queryClient.removeQueries(); 
            navigate('/login');
        }
    };

    if (isLoading) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
                    <p className="text-gray-600 font-medium">Loading projects...</p>
                </div>
            </div>
        );
    }

    if (isError) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50 flex items-center justify-center">
                <div className="bg-white rounded-2xl p-8 shadow-lg border border-red-200 max-w-md">
                    <div className="flex items-center gap-3 mb-4">
                        <div className="p-3 bg-red-100 rounded-xl">
                            <AlertCircle className="w-6 h-6 text-red-600" />
                        </div>
                        <h2 className="text-xl font-bold text-gray-900">Error Loading Projects</h2>
                    </div>
                    <p className="text-gray-600">Unable to load your projects. Please try again later.</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50">
            <div className="max-w-6xl mx-auto p-6">
                
                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
                    <div>
                        <h1 className="text-4xl font-bold text-gray-900 mb-2">My Projects</h1>
                        <p className="text-gray-600">Manage and track all your projects in one place</p>
                    </div>
                    
                    <button
                        onClick={handleLogout}
                        className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-red-600 bg-white border border-red-100 rounded-lg hover:bg-red-50 hover:border-red-200 shadow-sm transition-all duration-200"
                    >
                        <LogOut className="w-4 h-4" />
                        Sign Out
                    </button>
                </div>

                {/* Create Project Section */}
                <div className="bg-white rounded-2xl p-6 shadow-lg border border-gray-100 mb-8">
                    <h2 className="text-xl font-semibold text-gray-900 mb-4">Create New Project</h2>
                    
                    <form 
                        onSubmit={handleSubmit(onSubmit)}
                        className="flex flex-col sm:flex-row gap-4"
                    >
                        <input
                            {...register('title', { required: true })}
                            placeholder="Project Title"
                            className="flex-1 px-4 py-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-100 outline-none transition-all duration-200"
                        />
                        <input
                            {...register('description')}
                            placeholder="Description"
                            className="flex-1 px-4 py-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-100 outline-none transition-all duration-200"
                        />
                        <button
                            type="submit" 
                            disabled={createMutation.isPending}
                            className="px-6 py-3 text-sm font-medium text-white bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg hover:from-blue-700 hover:to-indigo-700 shadow-sm hover:shadow-md transition-all duration-200 transform hover:scale-105 whitespace-nowrap disabled:opacity-50"
                        >
                            {createMutation.isPending ? 'Creating...' : 'Create Project'}
                        </button>
                    </form>
                </div>

                {/* Projects Grid */}
                <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    {projects && projects.length > 0 ? (
                        projects.map((project) => (
                            <div 
                                key={project.id} 
                                onClick={() => navigate(`/projects/${project.id}`)}
                                className="group bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-xl hover:border-blue-200 transition-all duration-300 cursor-pointer transform hover:-translate-y-1"
                            >
                                <div className="flex justify-between items-start mb-4">
                                    <div className="flex-1">
                                        <h3 className="text-xl font-bold text-gray-900 mb-2 group-hover:text-blue-600 transition-colors duration-200">
                                            {project.title}
                                        </h3>
                                        <p className="text-sm text-gray-600 line-clamp-2">{project.description}</p>
                                    </div>
                                    
                                    <button
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            deleteMutation.mutate(project.id);
                                        }}
                                        className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all duration-200"
                                    >
                                        <Trash2 className="w-4 h-4" />
                                    </button>
                                </div>

                                <div className="space-y-2">
                                    <div className="flex justify-between items-center text-sm">
                                        <span className="text-gray-600 font-medium">Progress</span>
                                        <span className="text-gray-900 font-semibold">{Math.round(project.progress)}%</span>
                                    </div>
                                    <div className="w-full h-2.5 bg-gray-100 rounded-full overflow-hidden">
                                        <div
                                            className="h-full bg-gradient-to-r from-blue-500 to-indigo-500 rounded-full transition-all duration-500 ease-out"
                                            style={{ width: `${project.progress}%` }}
                                        />
                                    </div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className="col-span-full bg-white rounded-xl p-12 text-center border-2 border-dashed border-gray-200">
                            <div className="inline-flex p-4 bg-gray-100 rounded-full mb-4">
                                <Plus className="w-8 h-8 text-gray-400" />
                            </div>
                            <p className="text-gray-600 font-medium">No projects yet</p>
                            <p className="text-sm text-gray-400 mt-1">Create your first project to get started</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};
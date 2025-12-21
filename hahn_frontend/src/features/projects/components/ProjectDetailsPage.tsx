import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { projectService } from '../api/projectService';
import { taskService } from '../../tasks/api/taskService';
import { TaskItem } from '../../tasks/components/TaskItem';
import { CreateTaskForm } from '../../tasks/components/CreateTaskForm';
import { ArrowLeft, Search, Filter, FileText, ChevronLeft, ChevronRight } from 'lucide-react';

export const ProjectDetailsPage = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const projectId = Number(id);

    // STATE: Manage Filters locally
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState('');
    const [statusFilter, setStatusFilter] = useState('');

    // FETCH: Pass filters to the query
    const { data: taskPage, isLoading: loadingTasks } = useQuery({
        queryKey: ['tasks', projectId, page, search, statusFilter],
        queryFn: () => taskService.getByProject({ 
            projectId, 
            page, 
            search, 
            status: statusFilter 
        }),
        enabled: !!projectId && !isNaN(projectId),
        placeholderData: (prev) => prev, 
    });

    const { data: project, isLoading: loadingProject } = useQuery({
        queryKey: ['project', projectId],
        queryFn: () => projectService.getOne(projectId),
        enabled: !!projectId && !isNaN(projectId),
    });

    if (loadingProject) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50 flex items-center justify-center">
                <div className="text-center">
                    <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mb-4"></div>
                    <p className="text-gray-600 font-medium">Loading Project...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-50">
            <div className="max-w-5xl mx-auto p-6">
                <button 
                    onClick={() => navigate('/dashboard')} 
                    className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6 transition-colors duration-200 group"
                >
                    <ArrowLeft className="w-5 h-5 group-hover:-translate-x-1 transition-transform duration-200" />
                    <span className="font-medium">Back to Dashboard</span>
                </button>

                {/* Project Header */}
                <div className="bg-white rounded-2xl p-8 shadow-lg border border-gray-100 mb-8">
                    <div className="flex items-start gap-4">
                        <div className="p-3 bg-gradient-to-br from-blue-100 to-indigo-100 rounded-xl">
                            <FileText className="w-6 h-6 text-blue-600" />
                        </div>
                        <div className="flex-1">
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">
                                {project?.title}
                            </h1>
                            <p className="text-gray-600 leading-relaxed">
                                {project?.description}
                            </p>
                        </div>
                    </div>
                </div>

                {/* Create Task Form */}
                <CreateTaskForm projectId={projectId} />

                {/* Search & Filter Bar */}
                <div className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 mb-6">
                    <div className="flex flex-col sm:flex-row gap-4">
                        <div className="flex-1 relative">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                            <input 
                                type="text"
                                placeholder="Search tasks by title..."
                                value={search}
                                onChange={(e) => { 
                                    setSearch(e.target.value); 
                                    setPage(0); 
                                }}
                                className="w-full pl-10 pr-4 py-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-100 outline-none transition-all duration-200"
                            />
                        </div>

                        <div className="relative sm:w-48">
                            <Filter className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400 pointer-events-none" />
                            <select 
                                value={statusFilter}
                                onChange={(e) => { 
                                    setStatusFilter(e.target.value); 
                                    setPage(0); 
                                }}
                                className="w-full pl-10 pr-8 py-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-100 outline-none transition-all duration-200 bg-white appearance-none cursor-pointer"
                            >
                                <option value="">All Statuses</option>
                                <option value="PENDING">Pending</option>
                                <option value="IN_PROGRESS">In Progress</option>
                                <option value="COMPLETED">Completed</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className="space-y-4 min-h-[200px]">
                    {loadingTasks ? (
                        <div className="bg-white rounded-xl p-12 text-center border border-gray-100 shadow-sm">
                            <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mb-3"></div>
                            <p className="text-gray-500 font-medium">Loading tasks...</p>
                        </div>
                    ) : taskPage?.content && taskPage.content.length > 0 ? (
                        taskPage.content.map((task) => (
                            <TaskItem key={task.id} task={task} />
                        ))
                    ) : (
                        <div className="bg-white rounded-xl p-12 text-center border-2 border-dashed border-gray-200">
                            <div className="inline-flex p-4 bg-gray-100 rounded-full mb-4">
                                <FileText className="w-8 h-8 text-gray-400" />
                            </div>
                            <p className="text-gray-600 font-medium mb-1">No tasks found</p>
                            <p className="text-sm text-gray-400">
                                {search || statusFilter 
                                    ? 'Try adjusting your search or filters' 
                                    : 'Create your first task to get started'}
                            </p>
                        </div>
                    )}
                </div>

                {taskPage && taskPage.totalPages > 1 && (
                    <div className="flex justify-center items-center gap-4 mt-8">
                        <button
                            onClick={() => setPage((old) => Math.max(old - 1, 0))}
                            disabled={page === 0}
                            className="flex items-center gap-2 px-4 py-2.5 border border-gray-200 rounded-lg bg-white hover:bg-gray-50 hover:border-blue-300 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:border-gray-200 transition-all duration-200 font-medium text-gray-700"
                        >
                            <ChevronLeft className="w-4 h-4" />
                            Previous
                        </button>
                        
                        <div className="flex items-center gap-2">
                            <span className="text-sm text-gray-600 font-medium px-4 py-2 bg-white border border-gray-200 rounded-lg">
                                Page <span className="text-gray-900 font-semibold">{page + 1}</span> of <span className="text-gray-900 font-semibold">{taskPage.totalPages}</span>
                            </span>
                        </div>
                        
                        <button
                            onClick={() => setPage((old) => (!taskPage.last ? old + 1 : old))}
                            disabled={taskPage.last}
                            className="flex items-center gap-2 px-4 py-2.5 border border-gray-200 rounded-lg bg-white hover:bg-gray-50 hover:border-blue-300 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:border-gray-200 transition-all duration-200 font-medium text-gray-700"
                        >
                            Next
                            <ChevronRight className="w-4 h-4" />
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};
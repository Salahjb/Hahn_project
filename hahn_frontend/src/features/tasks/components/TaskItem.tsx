import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from 'react-hook-form';
import { taskService, type UpdateTaskRequest } from '../api/taskService';
import { type Task } from '../types';
import { CheckCircle2, Circle, Clock, Calendar, Trash2, Pencil, X, Save } from 'lucide-react';

interface TaskItemProps {
    task: Task;
}

export const TaskItem = ({ task }: TaskItemProps) => {
    const queryClient = useQueryClient();
    const [isEditing, setIsEditing] = useState(false);

    const todayStr = new Date().toLocaleDateString('en-CA');

    const { register, handleSubmit, reset, formState: { errors } } = useForm<UpdateTaskRequest>({
        defaultValues: {
            title: task.title,
            description: task.description,
            dueDate: task.dueDate
        }
    });

    // --- MUTATIONS ---
    const updateStatusMutation = useMutation({
        mutationFn: ({ taskId, status }: { taskId: number; status: Task['status'] }) =>
            taskService.updateStatus(taskId, status),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['tasks', task.projectId] });
            queryClient.invalidateQueries({ queryKey: ['projects'] });
        }
    });

    const deleteTaskMutation = useMutation({
        mutationFn: (taskId: number) => taskService.delete(taskId),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['tasks', task.projectId] });
            queryClient.invalidateQueries({ queryKey: ['projects'] });
        }
    });

    const updateTaskMutation = useMutation({
        mutationFn: (data: UpdateTaskRequest) => taskService.update(task.id, {
            ...data,
            projectId: task.projectId,
            status: task.status
        }),
        onSuccess: () => {
            setIsEditing(false);
            queryClient.invalidateQueries({ queryKey: ['tasks', task.projectId] });
        },
        onError: () => alert("Failed to update task")
    });

    // --- HANDLERS ---
    const onSubmit = (data: UpdateTaskRequest) => {
        updateTaskMutation.mutate(data);
    };

    const handleDelete = () => {
        deleteTaskMutation.mutate(task.id);
    };

    const getStatusIcon = (status: Task['status']) => {
        switch (status) {
            case 'COMPLETED': return <CheckCircle2 className="w-5 h-5 text-green-500" />;
            case 'IN_PROGRESS': return <Clock className="w-5 h-5 text-amber-500" />;
            default: return <Circle className="w-5 h-5 text-gray-400" />;
        }
    };
    if (isEditing) {
        return (
            <div className="bg-white rounded-xl p-5 shadow-lg border-2 border-blue-100 ring-2 ring-blue-50 transition-all">
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-3">
                    <div>
                        <input
                            {...register('title', { required: "Title is required" })}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none font-semibold text-gray-900"
                            placeholder="Task Title"
                            autoFocus
                        />
                        {errors.title && <p className="text-red-500 text-xs mt-1">{errors.title.message}</p>}
                    </div>

                    <textarea
                        {...register('description')}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none text-sm text-gray-700 resize-none"
                        placeholder="Description (optional)"
                        rows={2}
                    />

                    {/* Due Date Input with Validation */}
                    <div>
                        <input
                            type="date"
                            {...register('dueDate', {
                                validate: (value) => {
                                    if (!value) return true;
                                    if (value === task.dueDate) return true;
                                    return value >= todayStr || "Date must be today or in the future";
                                }
                            })}
                            min={todayStr}
                            className={`w-full px-3 py-2 border ${
                                errors.dueDate ? 'border-red-300 focus:border-red-500 focus:ring-red-100' : 'border-gray-300 focus:border-blue-500 focus:ring-blue-100'
                            } rounded-lg outline-none text-sm`}
                        />
                        {errors.dueDate && (
                            <span className="text-red-500 text-xs mt-1 block">
                                {errors.dueDate.message}
                            </span>
                        )}
                    </div>

                    <div className="flex justify-end gap-2 pt-2">
                        <button
                            type="button"
                            onClick={() => {
                                setIsEditing(false);
                                reset();
                            }}
                            className="flex items-center gap-1 px-3 py-1.5 text-sm font-medium text-gray-600 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
                        >
                            <X className="w-4 h-4" /> Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={updateTaskMutation.isPending}
                            className="flex items-center gap-1 px-3 py-1.5 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors shadow-sm disabled:opacity-50"
                        >
                            <Save className="w-4 h-4" /> 
                            {updateTaskMutation.isPending ? 'Saving...' : 'Save'}
                        </button>
                    </div>
                </form>
            </div>
        );
    }
    return (
        <div className="group bg-white rounded-xl p-5 shadow-sm border border-gray-100 hover:shadow-lg hover:border-blue-200 transition-all duration-300 transform hover:-translate-y-0.5">
            <div className="flex items-start gap-4">
                <div className="mt-1 flex-shrink-0">
                    {getStatusIcon(task.status)}
                </div>
                
                <div className="flex-1 min-w-0">
                    <div className="flex items-start justify-between gap-3 mb-2">
                        <h4 className={`font-semibold text-gray-900 ${task.status === 'COMPLETED' ? 'line-through text-gray-400' : ''}`}>
                            {task.title}
                        </h4>
                        
                        <div className="flex gap-2 flex-shrink-0 items-center opacity-0 group-hover:opacity-100 transition-opacity duration-200">
                            {task.status === 'PENDING' && (
                                <button
                                    onClick={() => updateStatusMutation.mutate({ taskId: task.id, status: 'IN_PROGRESS' })}
                                    className="px-2 py-1 text-xs font-medium text-blue-700 bg-blue-50 rounded hover:bg-blue-100"
                                >
                                    Start
                                </button>
                            )}
                            {task.status !== 'COMPLETED' && (
                                <button
                                    onClick={() => updateStatusMutation.mutate({ taskId: task.id, status: 'COMPLETED' })}
                                    className="px-2 py-1 text-xs font-medium text-green-700 bg-green-50 rounded hover:bg-green-100"
                                >
                                    Done
                                </button>
                            )}

                            <button
                                onClick={() => setIsEditing(true)}
                                className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
                                title="Edit Task"
                            >
                                <Pencil className="w-4 h-4" />
                            </button>

                            <button
                                onClick={handleDelete}
                                className="p-1.5 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-md transition-colors"
                                title="Delete Task"
                            >
                                <Trash2 className="w-4 h-4" />
                            </button>
                        </div>
                    </div>
                    
                    {task.description && (
                        <p className={`text-sm mb-3 ${task.status === 'COMPLETED' ? 'text-gray-400' : 'text-gray-600'}`}>
                            {task.description}
                        </p>
                    )}
                    
                    <div className="flex flex-wrap gap-2">
                        <span className="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border bg-gray-50 text-gray-600">
                            {task.status.replace('_', ' ')}
                        </span>
                        {task.dueDate && (
                            <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-purple-50 text-purple-700 border border-purple-200">
                                <Calendar className="w-3 h-3" />
                                {task.dueDate}
                            </span>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};
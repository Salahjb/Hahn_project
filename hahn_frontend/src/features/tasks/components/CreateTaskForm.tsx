import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { taskService } from '../api/taskService';
import { type CreateTaskRequest } from '../types';
import { Plus } from 'lucide-react';

interface CreateTaskFormProps {
    projectId: number;
}

export const CreateTaskForm = ({ projectId }: CreateTaskFormProps) => {
    const queryClient = useQueryClient();
    const [isExpanded, setIsExpanded] = useState(false);
    const { register, handleSubmit, reset, formState: { errors } } = useForm<CreateTaskRequest>();

    const todayStr = new Date().toLocaleDateString('en-CA');

    const createTaskMutation = useMutation({
        mutationFn: taskService.create,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['tasks', projectId] });
            queryClient.invalidateQueries({ queryKey: ['projects'] });
            queryClient.invalidateQueries({ queryKey: ['project', projectId] });
            reset();
            setIsExpanded(false);
        }
    });

    const onSubmit = (data: CreateTaskRequest) => {
        createTaskMutation.mutate({
            ...data,
            projectId,
            status: 'PENDING'
        });
    };

    return (
        <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-6 shadow-sm border border-blue-100 mb-8">
            <div className="flex items-center gap-2 mb-4">
                <Plus className="w-5 h-5 text-blue-600" />
                <h3 className="font-semibold text-gray-900">Add New Task</h3>
            </div>
            
            <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
                <div className="flex flex-col sm:flex-row gap-3">
                    <div className="flex-1">
                        <input
                            {...register('title', { required: 'Task title is required' })}
                            onFocus={() => setIsExpanded(true)}
                            placeholder="What needs to be done?"
                            className={`w-full px-4 py-3 rounded-lg border ${
                                errors.title ? 'border-red-300 focus:border-red-500 focus:ring-red-100' : 'border-gray-200 focus:border-blue-500 focus:ring-blue-100'
                            } focus:ring-4 outline-none transition-all duration-200 bg-white`}
                        />
                        {errors.title && (
                            <span className="text-red-500 text-xs mt-1 ml-1 block">{errors.title.message}</span>
                        )}
                    </div>
                    
                    <div className="flex flex-col">
                        <input
                            {...register('dueDate', {
                                validate: (value) => {
                                    if (!value) return true;
                                    return value >= todayStr || "Date must be today or in the future";
                                }
                            })}
                            type="date"
                            min={todayStr}
                            className={`px-4 py-3 rounded-lg border ${
                                errors.dueDate ? 'border-red-300 focus:border-red-500 focus:ring-red-100' : 'border-gray-200 focus:border-blue-500 focus:ring-blue-100'
                            } focus:ring-4 outline-none transition-all duration-200 bg-white`}
                        />
                         {errors.dueDate && (
                            <span className="text-red-500 text-xs mt-1 ml-1 block">
                                {errors.dueDate.message}
                            </span>
                        )}
                    </div>
                </div>

                {isExpanded && (
                    <div>
                        <textarea
                            {...register('description')}
                            placeholder="Add a description (optional)"
                            rows={3}
                            className="w-full px-4 py-3 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-4 focus:ring-blue-100 outline-none transition-all duration-200 bg-white resize-none"
                        />
                    </div>
                )}

                <div className="flex justify-end gap-3">
                    {isExpanded && (
                        <button
                            type="button" 
                            onClick={() => {
                                setIsExpanded(false);
                                reset();
                            }}
                            className="px-5 py-2.5 text-sm font-medium text-gray-700 bg-white rounded-lg hover:bg-gray-50 border border-gray-200 transition-colors duration-200"
                        >
                            Cancel
                        </button>
                    )}
                    <button
                        type="submit" 
                        disabled={createTaskMutation.isPending}
                        className="px-5 py-2.5 text-sm font-medium text-white bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg hover:from-blue-700 hover:to-indigo-700 shadow-sm hover:shadow-md transition-all duration-200 transform hover:scale-105 disabled:opacity-50"
                    >
                        {createTaskMutation.isPending ? 'Adding...' : 'Add Task'}
                    </button>
                </div>
            </form>
        </div>
    );
};
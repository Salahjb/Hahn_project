import { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { authService } from '../api/authService';
import { useAuthStore } from '../../../stores/useAuthStore';
import { type LoginRequest, type RegisterRequest } from '../types';
import { LogIn, UserPlus, Mail, Lock, User } from 'lucide-react';

export const LoginPage = () => {
    const navigate = useNavigate();
    const setAuth = useAuthStore((state) => state.setAuth);
    const logout = useAuthStore((state) => state.logout);

    // Toggle State (Login vs Register)
    const [isRegister, setIsRegister] = useState(false);

    useEffect(() => {
        logout();
        localStorage.removeItem('auth-storage');
    }, [logout]);

    const { register, handleSubmit, formState: { errors }, reset } = useForm<RegisterRequest & LoginRequest>();

    const toggleMode = () => {
        setIsRegister(!isRegister);
        reset();
    };

    // Mutation Logic
    const mutation = useMutation({
        mutationFn: (data: RegisterRequest & LoginRequest) => {
            if (isRegister) {
                return authService.register({
                    username: data.username,
                    email: data.email,
                    password: data.password
                });
            } else {
                return authService.login({
                    email: data.email,
                    password: data.password
                });
            }
        },
        onSuccess: (data) => {
            setAuth(data.token, data.userDto);
            navigate('/dashboard');
        },
        onError: (error: any) => {
            const msg = error.response?.data?.message || 'Authentication failed. Please check your credentials.';
            alert(msg);
        }
    });

    const onSubmit = (data: RegisterRequest & LoginRequest) => {
        mutation.mutate(data);
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 via-indigo-50 to-purple-50 p-4">
            <div className="max-w-md w-full">
                {/* Header Card */}
                <div className="text-center mb-8">
                    <div className="inline-flex p-4 bg-gradient-to-br from-blue-600 to-indigo-600 rounded-2xl shadow-lg mb-4">
                        {isRegister ? (
                            <UserPlus className="w-8 h-8 text-white" />
                        ) : (
                            <LogIn className="w-8 h-8 text-white" />
                        )}
                    </div>
                    <h2 className="text-3xl font-bold text-gray-900 mb-2">
                        {isRegister ? 'Create Account' : 'Welcome Back'}
                    </h2>
                    <p className="text-gray-600">
                        {isRegister ? 'Join the Project Manager today' : 'Sign in to access your projects'}
                    </p>
                </div>

                {/* Form Card */}
                <div className="bg-white rounded-2xl shadow-xl border border-gray-100 p-8">
                    <form className="space-y-6" onSubmit={handleSubmit(onSubmit)}>
                        
                        {isRegister && (
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Full Name
                                </label>
                                <div className="relative">
                                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <User className="h-5 w-5 text-gray-400" />
                                    </div>
                                    <input
                                        {...register('username', { required: isRegister ? 'Full Name is required' : false })}
                                        className={`block w-full pl-10 pr-3 py-3 border ${
                                            errors.username ? 'border-red-300 focus:border-red-500 focus:ring-red-100' : 'border-gray-300 focus:border-blue-500 focus:ring-blue-100'
                                        } rounded-lg focus:ring-4 outline-none transition-all duration-200`}
                                        placeholder="John Doe"
                                    />
                                </div>
                                {errors.username && (
                                    <p className="text-red-500 text-xs mt-1 ml-1">{errors.username.message}</p>
                                )}
                            </div>
                        )}

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Email Address
                            </label>
                            <div className="relative">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <Mail className="h-5 w-5 text-gray-400" />
                                </div>
                                <input
                                    type="email"
                                    {...register('email', { required: 'Email is required' })}
                                    className={`block w-full pl-10 pr-3 py-3 border ${
                                        errors.email ? 'border-red-300 focus:border-red-500 focus:ring-red-100' : 'border-gray-300 focus:border-blue-500 focus:ring-blue-100'
                                    } rounded-lg focus:ring-4 outline-none transition-all duration-200`}
                                    placeholder="you@example.com"
                                />
                            </div>
                            {errors.email && (
                                <p className="text-red-500 text-xs mt-1 ml-1">{errors.email.message}</p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Password
                            </label>
                            <div className="relative">
                                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <Lock className="h-5 w-5 text-gray-400" />
                                </div>
                                <input
                                    type="password"
                                    {...register('password', { 
                                        required: 'Password is required', 
                                        minLength: { value: 6, message: 'Password must be at least 6 characters' } 
                                    })}
                                    className={`block w-full pl-10 pr-3 py-3 border ${
                                        errors.password ? 'border-red-300 focus:border-red-500 focus:ring-red-100' : 'border-gray-300 focus:border-blue-500 focus:ring-blue-100'
                                    } rounded-lg focus:ring-4 outline-none transition-all duration-200`}
                                    placeholder="••••••••"
                                />
                            </div>
                            {errors.password && (
                                <p className="text-red-500 text-xs mt-1 ml-1">{errors.password.message}</p>
                            )}
                        </div>

                        <button
                            type="submit" 
                            disabled={mutation.isPending}
                            className="w-full flex justify-center items-center gap-2 py-3 px-4 border border-transparent text-sm font-medium rounded-lg text-white bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 focus:outline-none focus:ring-4 focus:ring-blue-100 disabled:opacity-50 disabled:cursor-not-allowed shadow-sm hover:shadow-md transition-all duration-200 transform hover:scale-[1.02]"
                        >
                            {mutation.isPending ? (
                                <>
                                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                                    Processing...
                                </>
                            ) : (
                                <>
                                    {isRegister ? <UserPlus className="w-4 h-4" /> : <LogIn className="w-4 h-4" />}
                                    {isRegister ? 'Create Account' : 'Sign In'}
                                </>
                            )}
                        </button>
                    </form>

                    <div className="mt-6 text-center">
                        <p className="text-sm text-gray-600">
                            {isRegister ? "Already have an account?" : "Don't have an account?"}{' '}
                            <button
                                type="button"
                                onClick={toggleMode}
                                className="font-semibold text-blue-600 hover:text-blue-700 transition-colors duration-200"
                            >
                                {isRegister ? 'Sign In' : 'Create Account'}
                            </button>
                        </p>
                    </div>
                </div>
                <div className="mt-8 pt-6 border-t border-gray-100 text-center space-y-1.5">
                    <p className="text-xs font-semibold text-gray-600 uppercase tracking-wide">
                        Hahn Software Morocco
                    </p>
                    <p className="text-xs text-gray-500">
                        End of Studies Internship 2026 • Technical Test
                    </p>
                </div>
            </div>
        </div>
    );
};
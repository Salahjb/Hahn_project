import { type User } from '../../../types';

export interface AuthResponse {
    token: string;
    userDto: User;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
}
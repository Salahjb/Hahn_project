import axios from "axios";
import { useAuthStore } from "../stores/useAuthStore";

const api = axios.create({
  //baseURL: 'http://localhost:8080/api',
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
});

// Add Token to requests
api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle 401 (Token Expired)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      useAuthStore.getState().logout();
      window.location.href = "/login";
    }
    return Promise.reject(error);
  },
);

export default api;


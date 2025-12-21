import React from 'react'; // <--- Add this import
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { LoginPage } from './features/auth/components/LoginPage';
import { useAuthStore } from './stores/useAuthStore';
import {ProjectList} from "./features/projects/components/ProjectList";
import { ProjectDetailsPage} from "./features/projects/components/ProjectDetailsPage";


// Temporary Dashboard
// const Dashboard = () => {
//     const { user, logout } = useAuthStore();
//     return (
//         <div className="p-10">
//             <h1 className="text-2xl font-bold">Welcome, {user?.username}!</h1>
//             <p className="text-gray-600">You are logged in.</p>
//             <button
//                 onClick={logout}
//                 className="mt-4 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
//             >
//                 Logout
//             </button>
//         </div>
//     );
// };

// Protected Route Wrapper
// FIX: Changed 'JSX.Element' to 'React.ReactNode'
const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
    const token = useAuthStore((state) => state.token);
    if (!token) {
        return <Navigate to="/login" replace />;
    }
    // ReactNode cannot be directly returned as JSX in some strict configs,
    // so we wrap it in a Fragment (<>) just to be safe.
    return <>{children}</>;
};

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />

                <Route
                    path="/dashboard"
                    element={
                        <ProtectedRoute>
                            <ProjectList />
                        </ProtectedRoute>
                    }
                />
                <Route 
                    path="/projects/:id" 
                    element={
                        <ProtectedRoute>
                        <ProjectDetailsPage />
                        </ProtectedRoute>
                    } 
                    />

                <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
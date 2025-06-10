import { createContext, useContext, useState, useCallback, useMemo } from 'react';
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('user');
    return stored ? JSON.parse(stored) : null;
  });

  const login = useCallback((newToken) => {
    const decoded = jwtDecode(newToken);
    const discordRoles = decoded.discordRoles || [];

    let role = 'ROLE_GUEST';
    if (discordRoles.includes('1120403312185974814') || discordRoles.includes('1120404614328635473')) {
      role = 'ROLE_ADMIN';
    } else if (discordRoles.includes('1120403755658129418')) {
      role = 'ROLE_USER';
    }
    
    const userData = {
      username: decoded.username ?? decoded.sub,
      id: decoded.id,
      avatar: decoded.avatar || `https://cdn.discordapp.com/embed/avatars/0.png`,
      role: decoded.role ?? "ROLE_GUEST",
    };

    setToken(newToken);
    setUser(userData);
    localStorage.setItem('token', newToken);
    localStorage.setItem('user', JSON.stringify(userData));
  }, []);

  const logout = useCallback(() => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }, []);

  const value = useMemo(() => ({ token, user, login, logout }), [token, user, login, logout]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => useContext(AuthContext);

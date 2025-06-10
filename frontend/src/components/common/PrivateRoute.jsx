import { Navigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const PrivateRoute = ({ children, requiredRoles }) => {
  const { user } = useAuth();

  const isAllowed =
    user &&
    (!requiredRoles || requiredRoles.includes(user.role));

  if (!isAllowed) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default PrivateRoute;

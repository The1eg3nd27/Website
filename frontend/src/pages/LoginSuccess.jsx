import { useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const LoginSuccess = () => {
  const [params] = useSearchParams();
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const token = params.get("token");

    if (!token) {
      navigate("/");
      return;
    }

    try {
      login(token);
      navigate("/");
    } catch (error) {
      console.error("Login failed:", error);
      navigate("/");
    }
  }, [params, login, navigate]);

  return <p>Connecting to Discord...</p>;
};

export default LoginSuccess;

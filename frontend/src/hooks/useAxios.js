import axios from "axios";

const useAxios = () => {
  const token = localStorage.getItem("token");

  const instance = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });

  return instance;
};

export default useAxios;

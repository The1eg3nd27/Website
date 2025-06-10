import axios from "axios";
import { useCallback } from "react";

const useTradeRouteApi = () => {
  const token = localStorage.getItem("token");

  const getTradeRoute = useCallback(async (payload) => {
    const res = await axios.post("/api/tools/tradeRoute", payload, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return res.data;
  }, [token]);

  return { getTradeRoute };
};

export default useTradeRouteApi;
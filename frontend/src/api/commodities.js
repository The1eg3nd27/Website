import axios from "axios";
import { useCallback } from "react";

const useCommodityApi = () => {
  const token = localStorage.getItem("token");

  const getCommodityNames = useCallback(async () => {
    const res = await axios.get("/api/tools/commodities/names", {
      headers: { Authorization: `Bearer ${token}` },
    });
    return res.data;
  }, [token]);

  const getCommodityInfo = useCallback(async ({ name, quantity }) => {
    const res = await axios.post(
      "/api/tools/commodities",
      { name, quantity },
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    return res.data;
  }, [token]);

  return { getCommodityNames, getCommodityInfo };
};

export default useCommodityApi;

import axios from "axios";

const useShipCompareApi = () => {
  const compareShips = async (shipA, shipB) => {
    const token = localStorage.getItem("token");

    const res = await axios.post(
      "/api/tools/shipcompare",
      { shipA, shipB },
      { headers: { Authorization: `Bearer ${token}` } }
    );

    return res.data;
  };

  return { compareShips };
};

export default useShipCompareApi;

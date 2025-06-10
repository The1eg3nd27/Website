import axios from "axios";

const useShipInfoApi = () => {
  const getShipInfo = async (name) => {
    const token = localStorage.getItem("token");
    const response = await axios.post(
      "/api/tools/shipinfo",
      { name },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    return response.data;
  };

  return { getShipInfo };
};

export default useShipInfoApi;

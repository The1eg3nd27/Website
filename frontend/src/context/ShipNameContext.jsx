import { createContext, useState, useEffect } from "react";
import axios from "axios";

export const ShipNameContext = createContext();

export const ShipNameProvider = ({ children }) => {
  const [shipNames, setShipNames] = useState([]);

  const fetchShipNames = async () => {
    try {
      const res = await axios.get("/api/tools/shipnames");
      setShipNames(res.data);
    } catch (err) {
      console.error("❌ Failed to fetch ship names:", err);
    }
  };

  useEffect(() => {
    fetchShipNames();
  }, []);

  const refreshShipNames = () => {
    fetchShipNames();
  };

  return (
    <ShipNameContext.Provider value={{ shipNames, refreshShipNames }}>
      {children}
    </ShipNameContext.Provider>
  );
};

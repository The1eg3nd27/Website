import React, { useState, useContext } from "react";
import useShipCompareApi from "../../api/shipCompare";
import { ShipNameContext } from "../../context/ShipNameContext";
import { RadarChart, PolarGrid, PolarAngleAxis, Radar, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip } from "recharts";

export default function ShipCompare() {
  const { shipNames } = useContext(ShipNameContext);
  const { compareShips } = useShipCompareApi();

  const [shipA, setShipA] = useState("");
  const [shipB, setShipB] = useState("");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  const handleCompare = async () => {
    try {
      const res = await compareShips(shipA, shipB);
      setResult(res);
      setError("");
    } catch (err) {
      setResult(null);
      setError("One or both ships not found.");
    }
  };

  const handleReset = () => {
    setShipA("");
    setShipB("");
    setResult(null);
    setError("");
  };

  const radarData = result
    ? [
        {
          stat: "HP",
          [result.shipA.name]: result.shipA.hp,
          [result.shipB.name]: result.shipB.hp,
        },
        {
          stat: "SCM Speed",
          [result.shipA.name]: result.shipA.scmSpeed,
          [result.shipB.name]: result.shipB.scmSpeed,
        },
        {
          stat: "Max Speed",
          [result.shipA.name]: result.shipA.navMaxSpeed,
          [result.shipB.name]: result.shipB.navMaxSpeed,
        },
        {
          stat: "Cargo",
          [result.shipA.name]: result.shipA.cargoCapacity,
          [result.shipB.name]: result.shipB.cargoCapacity,
        },
        {
          stat: "Shield",
          [result.shipA.name]: result.shipA.shieldHp,
          [result.shipB.name]: result.shipB.shieldHp,
        },
      ]
    : [];

  return (
    <div className="page ship-compare-page">
      <h2>⚔️ Ship Comparison</h2>

      <div style={{ display: "flex", gap: "1rem", flexWrap: "wrap", marginBottom: "1rem" }}>
        <div>
          <label>Ship A:</label>
          <input list="ships" value={shipA} onChange={(e) => setShipA(e.target.value)} />
        </div>
        <div>
          <label>Ship B:</label>
          <input list="ships" value={shipB} onChange={(e) => setShipB(e.target.value)} />
        </div>
        <datalist id="ships">
          {shipNames.map((name, idx) => (
            <option key={idx} value={name} />
          ))}
        </datalist>
      </div>

      <div style={{ display: "flex", gap: "1rem", marginBottom: "1rem" }}>
        <button onClick={handleCompare}>🔍 Compare</button>
        <button onClick={handleReset}>❌ Reset</button>
      </div>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {result && (
        <>
          <h3>📊 Stats</h3>
          <RadarChart outerRadius={120} width={500} height={300} data={radarData}>
            <PolarGrid />
            <PolarAngleAxis dataKey="stat" />
            <Radar name={result.shipA.name} dataKey={result.shipA.name} stroke="#8884d8" fill="#8884d8" fillOpacity={0.6} />
            <Radar name={result.shipB.name} dataKey={result.shipB.name} stroke="#82ca9d" fill="#82ca9d" fillOpacity={0.6} />
          </RadarChart>

          <h3 style={{ marginTop: "2rem" }}>📈 Cargo & Speed</h3>
          <BarChart width={500} height={300} data={radarData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="stat" />
            <YAxis />
            <Tooltip />
            <Bar dataKey={result.shipA.name} fill="#8884d8" />
            <Bar dataKey={result.shipB.name} fill="#82ca9d" />
          </BarChart>
        </>
      )}
    </div>
  );
}

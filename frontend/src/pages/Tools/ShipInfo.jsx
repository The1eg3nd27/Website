import React, { useState, useContext } from "react";
import useShipInfoApi from "../../api/shipInfo";
import { ShipNameContext } from "../../context/ShipNameContext";

export default function ShipInfo() {
  const { shipNames, refreshShipNames } = useContext(ShipNameContext);
  const { getShipInfo } = useShipInfoApi();

  const [query, setQuery] = useState("");
  const [ship, setShip] = useState(null);
  const [error, setError] = useState(null);

  const handleSearch = async () => {
    if (!query.trim()) return;
    try {
      const data = await getShipInfo(query);
      setShip(data);
      setError(null);
    } catch (e) {
      setShip(null);
      setError("❌ Ship not found or unauthorized.");
    }
  };

  const handleReset = () => {
    setQuery("");
    setShip(null);
    setError(null);
  };

  let imageSrc = null;
  if (ship?.image && Array.isArray(ship.image)) {
    const byteArray = new Uint8Array(ship.image);
    const base64String = btoa(
      byteArray.reduce((data, byte) => data + String.fromCharCode(byte), "")
    );
    imageSrc = `data:image/png;base64,${base64String}`;
  }

  return (
    <div className="page ship-info-page">
      <h2>🚀 Ship Info</h2>

      <input
        type="text"
        list="ship-list"
        placeholder="Search for a ship..."
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        style={{ padding: "0.5rem", width: "100%", fontSize: "1rem" }}
      />
      <datalist id="ship-list">
        {shipNames.map((name, i) => (
          <option key={i} value={name} />
        ))}
      </datalist>

      <div style={{ marginTop: "1rem", display: "flex", gap: "1rem" }}>
        <button onClick={handleSearch}>🔍 Search</button>
        <button onClick={handleReset}>❌ Reset</button>
        <button onClick={refreshShipNames}>🔄 Refresh Ship Names</button>
      </div>

      {error && <p style={{ color: "red", marginTop: "1rem" }}>{error}</p>}

      {ship && (
        <div
          style={{
            marginTop: "2rem",
            padding: "1rem",
            border: "1px solid #ccc",
            borderRadius: "8px",
            background: "#f9f9f9",
          }}
        >
          <h3>{ship.name}</h3>
          <p><strong>Manufacturer:</strong> {ship.manufacturer}</p>
          <p><strong>Type:</strong> {ship.type}</p>
          <p><strong>Focus:</strong> {ship.focus}</p>
          <p><strong>Size:</strong> {ship.size}</p>
          <p><strong>Crew:</strong> {ship.crewMin} {ship.crewMax}</p>
          <p><strong>Shield HP:</strong> {ship.shieldHp}</p>
          <p><strong>SCM Speed:</strong> {ship.scmSpeed}</p>
          <p><strong>Max Speed:</strong> {ship.navMaxSpeed}</p>
          <p><strong>Pitch/Yaw/Roll:</strong> {ship.pitch} / {ship.yaw} / {ship.roll}</p>
          <p><strong>Cargo:</strong> {ship.cargoCapacity} SCU</p>
          <p><strong>Base Price:</strong> {ship.msrp} $</p>
          <p><strong>Status:</strong> {ship.productionStatus}</p>
          <p><strong>Description:</strong> {ship.description}</p>
          {imageSrc && (
            <img
              src={imageSrc}
              alt={ship.name}
              style={{ marginTop: "1rem", width: "100%", maxWidth: "400px" }}
            />
          )}
        </div>
      )}
    </div>
  );
}

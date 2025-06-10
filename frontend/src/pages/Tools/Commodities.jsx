import React, { useState, useEffect } from "react";
import useCommodityApi from "../../api/commodities";

export default function Commodities() {
  const [names, setNames] = useState([]);
  const [name, setName] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const { getCommodityNames, getCommodityInfo } = useCommodityApi();

  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    getCommodityNames()
      .then(setNames)
      .catch(() => setError("❌ Failed to load commodity names."));
  }, [getCommodityNames]);

  const handleSubmit = async () => {
    try {
      const data = await getCommodityInfo({ name, quantity });
      if (!data || data.fallbackInfo) {
        setError(data?.fallbackInfo || "❌ No data found.");
        setResult(null);
      } else {
        setResult(data);
        setError(null);
      }
    } catch (err) {
      console.error(err);
      setError("❌ Request failed.");
      setResult(null);
    }
  };

  return (
    <div className="page commodities-page">
      <h2>📦 Commodities</h2>

      <input
        type="text"
        list="commodities"
        value={name}
        onChange={(e) => setName(e.target.value.trim())} 
        placeholder="Enter commodity name"
        />
      <datalist id="commodities">
        {names.map((n, i) => (
          <option key={i} value={n} />
        ))}
      </datalist>

      <input
        type="number"
        min="1"
        value={quantity}
        onChange={(e) => setQuantity(Number(e.target.value))}
        placeholder="Quantity"
      />
      <button onClick={handleSubmit}>🔍 Search</button>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {result && (
        <div style={{ marginTop: "1rem" }}>
          <h3>{result.commodityName}</h3>
          <p><strong>Buy:</strong> {result.bestBuyPrice} at {result.bestBuyLocation}</p>
          <p><strong>Sell:</strong> {result.bestSellPrice} at {result.bestSellLocation}</p>
          <p><strong>Profit/unit:</strong> {result.profitPerUnit}</p>
          <p><strong>Total Profit:</strong> {result.potentialProfit}</p>
          <p><strong>Recommendation:</strong> {result.recommendation}</p>
        </div>
      )}
    </div>
  );
}

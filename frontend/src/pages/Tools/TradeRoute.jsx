import React, { useState } from 'react';
import useTradeRouteApi from '../../api/tradeRoute';
import useCommodityApi from '../../api/commodities';

const systems = ['Stanton', 'Pyro'];
const locations = [
  'Area18',
  'Lorville',
  'New Babbage',
  'Orison',
  'Port Olisar',
  'Everus Harbor',
  'Bajini Point',
  'Grim HEX',
];


const distance = (a, b) => {
  const dp = Array.from({ length: a.length + 1 }, () => Array(b.length + 1).fill(0));
  for (let i = 0; i <= a.length; i++) dp[i][0] = i;
  for (let j = 0; j <= b.length; j++) dp[0][j] = j;
  for (let i = 1; i <= a.length; i++) {
    for (let j = 1; j <= b.length; j++) {
      dp[i][j] = Math.min(
        dp[i - 1][j] + 1,
        dp[i][j - 1] + 1,
        dp[i - 1][j - 1] + (a[i - 1] === b[j - 1] ? 0 : 1),
      );
    }
  }
  
  return dp[a.length][b.length];
};

const fuzzyFilter = (opts, input, max = 2) =>
  opts.filter((o) =>
    o.toLowerCase().includes(input.toLowerCase()) ||
    distance(o.toLowerCase(), input.toLowerCase()) <= max
  );

const TradeRoute = () => {
  const { getTradeRoute } = useTradeRouteApi();
  const { getCommodityNames } = useCommodityApi();

  const [form, setForm] = useState({
    commodityName: '',
    quantity: 0,
    currentSystem: 'Stanton',
    currentLocation: '',
    canTravelSystems: true,
  });

  const [result, setResult] = useState(null);
  const [suggestions, setSuggestions] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const val = type === 'checkbox' ? checked : value;
    setForm((prev) => ({ ...prev, [name]: val }));

    if (name === 'commodityName') {
      setSuggestions((prev) => fuzzyFilter(prev, value));
    }
  };

  const loadSuggestions = async () => {
    try {
      const allNames = await getCommodityNames();
      setSuggestions(allNames);
    } catch (e) {
      console.error('Failed to load suggestions:', e);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setResult(null);
    setLoading(true);
    try {
      const data = await getTradeRoute(form);
      setResult(data);
    } catch (err) {
      console.error('❌ Trade route fetch error:', err);
      setError(err?.response?.data?.message || 'Something went wrong. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>🚚 Trade Route Optimizer</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '1rem', position: 'relative' }}>
          <label>Commodity Name:</label>
          <input
            type="text"
            name="commodityName"
            value={form.commodityName}
            onChange={handleChange}
            onFocus={loadSuggestions}
            autoComplete="off"
          />
          {suggestions.length > 0 && form.commodityName && (
            <ul
              style={{
                border: '1px solid #ccc',
                maxHeight: '150px',
                overflowY: 'auto',
                listStyle: 'none',
                padding: '0.5rem',
                margin: 0,
                position: 'absolute',
                background: '#fff',
                zIndex: 10,
                width: '300px',
              }}
            >
              {fuzzyFilter(suggestions, form.commodityName).map((name, idx) => (
                <li
                  key={idx}
                  style={{ padding: '0.25rem', cursor: 'pointer' }}
                  onClick={() => {
                    setForm((prev) => ({ ...prev, commodityName: name }));
                    setSuggestions([]);
                  }}
                >
                  {name}
                </li>
              ))}
            </ul>
          )}
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>Quantity:</label>
          <input
            type="number"
            name="quantity"
            value={form.quantity}
            onChange={handleChange}
          />
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>Current System:</label>
          <input
            type="text"
            name="currentSystem"
            list="systems"
            value={form.currentSystem}
            onChange={handleChange}
          />
          <datalist id="systems">
            {systems.map((s) => (
              <option key={s} value={s} />
            ))}
          </datalist>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>Current Location:</label>
          <input
            type="text"
            name="currentLocation"
            list="locations"
            value={form.currentLocation}
            onChange={handleChange}
          />
          <datalist id="locations">
            {locations.map((l) => (
              <option key={l} value={l} />
            ))}
          </datalist>
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>
            <input
              type="checkbox"
              name="canTravelSystems"
              checked={form.canTravelSystems}
              onChange={handleChange}
            />
            Allow System Change
          </label>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Searching...' : 'Get Trade Route'}
        </button>
      </form>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {result && (
        <div
          style={{
            marginTop: '2rem',
            padding: '1rem',
            border: '1px solid #ccc',
            borderRadius: '8px',
            background: '#f5f5f5',
          }}
        >
          <h3>💡 Recommended Trade</h3>
          <p><strong>System:</strong> {result.system}</p>
          <p><strong>Risk Level:</strong> {result.riskLevel}</p>
          <p><strong>Best Buy Location:</strong> {result.bestBuyLocation}</p>
          <p><strong>Best Buy Price:</strong> {result.bestBuyPrice} aUEC</p>
          <p><strong>Best Sell Location:</strong> {result.bestSellLocation}</p>
          <p><strong>Sell Price:</strong> {result.sellPrice} aUEC</p>
          <p><strong>Profit:</strong> {result.expectedProfit} aUEC</p>
          <p><strong>Summary:</strong> {result.recommendation}</p>
        </div>
      )}
    </div>
  );
};

export default TradeRoute;
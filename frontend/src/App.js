import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/layout/Navbar";
import LoginSuccess from "./pages/LoginSuccess";
import { ShipNameProvider } from "./context/ShipNameContext";


import Home from "./pages/Home";
import Events from "./pages/Events";
import Posts from "./pages/Posts";
import Images from "./pages/Images";

import ShipInfo from "./pages/Tools/ShipInfo";
import ShipCompare from "./pages/Tools/ShipCompare";
import Commodities from "./pages/Tools/Commodities";
import TradeRoute from "./pages/Tools/TradeRoute";
import Earnings from "./pages/Tools/Earnings";

import PrivateRoute from "./components/common/PrivateRoute";
function App() {
  return (
    <AuthProvider>
      <ShipNameProvider>
        <Router>
          <Navbar />
          <Routes>
            {/* Public */}
            <Route path="/" element={<Home />} />
            <Route path="/events" element={<Events />} />
            <Route path="/posts" element={<Posts />} />
            <Route path="/images" element={<Images />} />
            <Route path="/login/success" element={<LoginSuccess />} />

            {/* Protected: Tools */}
            <Route
              path="/tools/shipcompare"
              element={
                <PrivateRoute requiredRoles={["ROLE_USER", "ROLE_ADMIN"]}>
                  <ShipCompare />
                </PrivateRoute>
              }
            />
            <Route
              path="/tools/commodities"
              element={
                <PrivateRoute requiredRoles={["ROLE_USER", "ROLE_ADMIN"]}>
                  <Commodities />
                </PrivateRoute>
              }
            />
            <Route
              path="/tools/traderoute"
              element={
                <PrivateRoute requiredRoles={["ROLE_USER", "ROLE_ADMIN"]}>
                  <TradeRoute />
                </PrivateRoute>
              }
            />
            <Route
              path="/tools/earnings"
              element={
                <PrivateRoute requiredRoles={["ROLE_USER", "ROLE_ADMIN"]}>
                  <Earnings />
                </PrivateRoute>
              }
            />
            <Route
              path="/tools/shipinfo"
              element={
                <PrivateRoute requiredRoles={["ROLE_USER", "ROLE_ADMIN"]}>
                  <ShipInfo />
                </PrivateRoute>
              }
            />
          </Routes>
        </Router>
      </ShipNameProvider>
    </AuthProvider>
  );
}


export default App;

import { Link } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import ConnectButton from "./ConnectButton";

const Navbar = () => {
  const { user } = useAuth();

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/">Home</Link>
        <Link to="/events">Events</Link>
        <Link to="/posts">Posts</Link>
        <Link to="/images">Images</Link>

        {(user?.role === 'ROLE_ADMIN' || user?.role === 'ROLE_USER') && (
          <div className="tools-menu">
            <Link to="/tools/shipinfo">Ship Info</Link>
            <Link to="/tools/shipcompare">Compare</Link>
            <Link to="/tools/commodities">Commodities</Link>
            <Link to="/tools/traderoute">Trade</Link>
            <Link to="/tools/earnings">Earnings</Link>
          </div>
        )}
      </div>

      <div className="navbar-right">
        <ConnectButton />
      </div>
    </nav>
  );
};

export default Navbar;

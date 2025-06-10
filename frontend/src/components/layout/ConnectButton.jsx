import { useAuth } from "../../context/AuthContext";

const ConnectButton = () => {
  const { user, logout } = useAuth();

  const handleLogin = () => {
    window.location.href = "http://localhost:8080/discord";
  };

  const handleLogout = () => {
    logout();
  };

  if (!user) {
    return (
      <button onClick={handleLogin} className="connect-btn">
        Connect to Discord
      </button>
    );
  }

  return (
    <div className="user-info">
      <img
        src={user.avatar}
        alt="User avatar"
        style={{ width: 32, height: 32, borderRadius: "50%", marginRight: 8 }}
      />
      <span style={{ marginRight: 8 }}>
        {user.username} ({user.role.replace("ROLE_", "")})
      </span>
      <button onClick={handleLogout} className="connect-btn">
        Logout
      </button>
    </div>
  );
};

export default ConnectButton;

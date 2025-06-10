import { motion } from "framer-motion";

export default function Home() {
  return (
    <motion.div
      className="home-page"
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.6 }}
      style={{ padding: "2rem", textAlign: "center" }}
    >
      <h1>🛰️ Welcome to Spectre</h1>
      <p>Explore our fleet, compare ships, and plan your trade routes.</p>
    </motion.div>
  );
}

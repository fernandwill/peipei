import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Standalone output lets the Docker image ship only the server + static assets.
  output: "standalone",
};

export default nextConfig;

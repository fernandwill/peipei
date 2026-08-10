import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  // Standalone output lets the Docker image ship only the server + static assets.
  output: "standalone",
  // cacheComponents merges Partial Prerendering + the `"use cache"` directive (Next 16.3):
  // the static shell (sidebar/header/layout) is prerendered at build time and only dynamic
  // data sections stream per-route (§ optimal SSR).
  experimental: {
    cacheComponents: true,
  },
};

export default nextConfig;

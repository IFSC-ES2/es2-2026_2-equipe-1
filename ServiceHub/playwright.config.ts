import { defineConfig, devices } from "@playwright/test";

const baseURL = process.env.PLAYWRIGHT_BASE_URL ?? "http://localhost:5173";
const apiUrl = process.env.PLAYWRIGHT_API_URL ?? "http://localhost:8081/api";

const comandoBackend = process.platform === "win32"
  ? "gradlew.bat bootRun"
  : "./gradlew bootRun";

export default defineConfig({
  testDir: "./e2e",
  fullyParallel: false,
  workers: 1,
  reporter: "list",
  use: {
    ...devices["Desktop Chrome"],
    baseURL,
    screenshot: "only-on-failure",
    trace: "retain-on-failure",
    video: "retain-on-failure",
  },
  webServer: [
    {
      command: "npm run dev -- --host localhost",
      url: baseURL,
      reuseExistingServer: !process.env.CI,
      timeout: 30_000,
    },
    {
      command: comandoBackend,
      cwd: "../back-end",
      url: `${apiUrl}/usuarios/csrf`,
      reuseExistingServer: !process.env.CI,
      timeout: 120_000,
      env: {
        ...process.env,
        JWT_SECRET: process.env.JWT_SECRET ?? "playwright-local-secret-at-least-32-bytes",
      },
    },
  ],
});

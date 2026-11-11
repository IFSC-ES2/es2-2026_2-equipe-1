import { defineConfig, devices } from "@playwright/test";

const baseURL = process.env.PLAYWRIGHT_BASE_URL ?? "http://localhost:5173";

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
      command: "..\\back-end\\gradlew.bat bootRun",
      url: "http://localhost:8081/api/usuarios/csrf",
      reuseExistingServer: !process.env.CI,
      timeout: 120_000,
    },
  ],
});

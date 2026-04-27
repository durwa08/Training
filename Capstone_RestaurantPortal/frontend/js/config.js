export const CONFIG = {
  API_BASE_URL: "http://localhost:8080",
  TOKEN_STORAGE_KEY: "rp_jwt_token",
  // If backend is down / CORS / endpoint mismatch, UI still works with mock responses.
  MOCK_ON_FAILURE: true,
  // Optional preview endpoint for featured items
  FOOD_PREVIEW_PATH: "/api/menuitems",
};


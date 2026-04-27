// Import base config (API URL, feature flags like mock mode)
import { CONFIG } from "./config.js";

// Utility to get JWT token from storage
import { getToken } from "./auth.js";

/*
  Simulates network delay for mock APIs.
  Helps test loading states (spinners, skeletons).
*/
function mockDelay(ms = 500) {
  return new Promise((r) => window.setTimeout(r, ms));
}

/*
  Mock food data used when backend is unavailable.
  Ensures UI doesn't break during development/demo.
*/
function mockFoodItems() {
  return [
    {
      id: 1,
      name: "White Sauce Pasta",
      description: "Creamy • Cheesy • Italian herbs",
      price: 229,
      imageUrl:
        "https://images.unsplash.com/photo-1555949258-eb67b1ef0ceb?auto=format&fit=crop&w=1200&q=60",
    },
    {
      id: 2,
      name: "Rajasthani Thali",
      description: "Dal Baati • Churma • Gatte",
      price: 299,
      imageUrl:
        "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=1200&q=60",
    },
    {
      id: 3,
      name: "Veg Party Combo",
      description: "Paneer wrap • Fries • Dip",
      price: 279,
      imageUrl:
        "https://images.unsplash.com/photo-1551782450-a2132b4ba21d?auto=format&fit=crop&w=1200&q=60",
    },
  ];
}

/*
  Mock register API (fallback when backend fails)
*/
async function mockRegister() {
  await mockDelay(650);
  return { message: "Mock registration success" };
}

/*
  Mock login API returning a fake JWT
  Useful for testing UI flow without backend
*/
async function mockLogin() {
  await mockDelay(650);
  return { token: "mock.jwt.token.for-ui-preview" };
}

/*
  Safely parses JSON without throwing error
*/
function safeJsonParse(text) {
  try {
    return JSON.parse(text);
  } catch {
    return null;
  }
}

/*
  Extracts meaningful error message from API response
  Handles different backend error formats
*/
function extractErrorMessage({ status, payload, fallbackText }) {
  if (payload && typeof payload === "object") {
    return (
      payload.message ||
      payload.error ||
      payload.details ||
      `Request failed (${status})`
    );
  }
  return fallbackText || `Request failed (${status})`;
}

/*
  Core API wrapper for all HTTP calls
  Handles:
  - Headers
  - JWT auth
  - Error parsing
  - Network failure fallback
*/
export async function apiFetch(path, { method = "GET", body, auth = false } = {}) {
  const headers = {
    "Content-Type": "application/json",
  };

  // Attach JWT token if request requires authentication
  if (auth) {
    const token = getToken();
    if (token) headers.Authorization = `Bearer ${token}`;
  }

  let res;

  try {
    // Actual API call
    res = await fetch(`${CONFIG.API_BASE_URL}${path}`, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
    });
  } catch (networkErr) {
    /*
      Handles:
      - Backend down
      - CORS issues
      - No internet
    */
    if (CONFIG.MOCK_ON_FAILURE) {
      const err = new Error(
        `Backend unreachable. Using mock mode for UI preview. (${networkErr?.message || "Network error"})`
      );
      err.isMockFallback = true;
      throw err;
    }
    throw networkErr;
  }

  // Read raw response
  const rawText = await res.text();

  // Try parsing JSON safely
  const json = rawText ? safeJsonParse(rawText) : null;

  // Handle non-2xx responses
  if (!res.ok) {
    const message = extractErrorMessage({
      status: res.status,
      payload: json,
      fallbackText: rawText,
    });

    const err = new Error(message);
    err.status = res.status;
    err.payload = json;
    throw err;
  }

  // Return parsed JSON or raw text
  return json ?? rawText;
}

/*
  Register user API
*/
export async function registerUser(payload) {
  try {
    return await apiFetch("/api/users/register", {
      method: "POST",
      body: payload,
    });
  } catch (e) {
    // Fallback to mock if enabled
    if (CONFIG.MOCK_ON_FAILURE) return mockRegister(payload);
    throw e;
  }
}

/*
  Login user API
*/
export async function loginUser(payload) {
  try {
    return await apiFetch("/api/users/login", {
      method: "POST",
      body: payload,
    });
  } catch (e) {
    if (CONFIG.MOCK_ON_FAILURE) return mockLogin(payload);
    throw e;
  }
}

/*
  Fetch featured food items (landing page preview)
  Supports:
  - Direct array response
  - Wrapped response { data: [...] }
*/
export async function fetchFeaturedFoodItems() {
  try {
    const data = await apiFetch(CONFIG.FOOD_PREVIEW_PATH, {
      method: "GET",
    });

    // Handle multiple response formats
    if (Array.isArray(data)) return data;
    if (data && typeof data === "object" && Array.isArray(data.data)) {
      return data.data;
    }

    // Fallback if response shape is unexpected
    return mockFoodItems();
  } catch (e) {
    if (CONFIG.MOCK_ON_FAILURE) return mockFoodItems();
    throw e;
  }
}

/*
  Extract JWT token from login response
  Handles multiple backend formats
*/
export function extractJwtToken(loginResponse) {
  if (!loginResponse) return null;

  // Common response formats
  if (typeof loginResponse === "object") {
    return (
      loginResponse.token ||
      loginResponse.jwt ||
      loginResponse.jwtToken ||
      loginResponse.accessToken ||
      null
    );
  }

  // If backend returns plain string token
  if (typeof loginResponse === "string") {
    const trimmed = loginResponse.trim();
    return trimmed.length ? trimmed : null;
  }

  return null;
}
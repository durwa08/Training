// Import config to access storage key
import { CONFIG } from "./config.js";

/*
  Get JWT token from localStorage
  Used whenever we need to check auth or attach token to API calls
*/
export function getToken() {
  return localStorage.getItem(CONFIG.TOKEN_STORAGE_KEY);
}

/*
  Save JWT token after successful login
  This enables session persistence across page reloads
*/
export function setToken(token) {
  localStorage.setItem(CONFIG.TOKEN_STORAGE_KEY, token);
}

/*
  Remove token on logout
  Ensures user is fully signed out
*/
export function clearToken() {
  localStorage.removeItem(CONFIG.TOKEN_STORAGE_KEY);
}

/*
  Check if user is logged in
  Returns true if token exists, false otherwise
*/
export function isLoggedIn() {
  return Boolean(getToken());
}
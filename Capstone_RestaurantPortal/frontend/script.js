// Import page-specific initialization functions
// Each function contains logic related to that particular page
import { initLoginPage } from "./js/pages/loginPage.js";
import { initRegisterPage } from "./js/pages/registerPage.js";
import { initDashboardPage } from "./js/pages/dashboardPage.js";
import { initLandingPage } from "./js/pages/landingPage.js";

// Read the custom data attribute from <body>
// This tells us which page is currently loaded
const page = document.body?.dataset?.page;

// Route control: initialize logic based on current page
switch (page) {
  case "landing":
    // Landing page → handles hero, featured items, modals, etc.
    initLandingPage();
    break;

  case "login":
    // Login page → handles form validation, API call, JWT storage
    initLoginPage();
    break;

  case "register":
    // Register page → handles validation, password checks, API call
    initRegisterPage();
    break;

  case "dashboard":
    // Dashboard → protected page, checks JWT and loads user data
    initDashboardPage();
    break;

  default:
    // No matching page → do nothing
    // Keeps code safe for future pages without breaking
    break;
}
/*
  Validates email format using regex
  Covers basic structure: text@domain.extension
*/
export function isEmail(value) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(
    String(value || "").trim()
  );
}

/*
  Password validation rules
  Returns:
  - ok → overall validity
  - checks → individual rule results (useful for UI hints)
*/
export function passwordRules(password) {
  const value = String(password || "");

  // Individual validation checks
  const checks = {
    minLength: value.length >= 8,          // At least 8 characters
    hasUpper: /[A-Z]/.test(value),         // At least one uppercase letter
    hasLower: /[a-z]/.test(value),         // At least one lowercase letter
    hasNumber: /[0-9]/.test(value),        // At least one number
    hasSpecial: /[^A-Za-z0-9]/.test(value) // At least one special character
  };

  // Overall validity → all checks must pass
  const ok =
    checks.minLength &&
    checks.hasUpper &&
    checks.hasLower &&
    checks.hasNumber &&
    checks.hasSpecial;

  return { ok, checks };
}

/*
  Checks if a field is non-empty
  Used for required field validation
*/
export function required(value) {
  return String(value || "").trim().length > 0;
}
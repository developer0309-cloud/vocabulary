import type { Context, Language } from "@/api/schemas";
import { contextSchema } from "@/api/schemas";
import { languageSchema } from "@/api/schemas";

const USERNAME_KEY = "vocabulary.username";
const LANGUAGE_KEY = "vocabulary.language";
const CONTEXT_KEY = "vocabulary.context";

export function getUsername(): string | null {
  const value = sessionStorage.getItem(USERNAME_KEY);
  return value === null || value.trim() === "" ? null : value;
}

export function setUsername(username: string): void {
  sessionStorage.setItem(USERNAME_KEY, username.trim());
}

export function clearUsername(): void {
  sessionStorage.removeItem(USERNAME_KEY);
}

export function getLanguage(): Language | null {
  const value = sessionStorage.getItem(LANGUAGE_KEY);
  const parsed = languageSchema.safeParse(value);
  return parsed.success ? parsed.data : null;
}

export function setLanguage(language: Language): void {
  sessionStorage.setItem(LANGUAGE_KEY, language);
}

export function getContext(): Context | null {
  const value = sessionStorage.getItem(CONTEXT_KEY);
  const parsed = contextSchema.safeParse(value);
  return parsed.success ? parsed.data : null;
}

export function setContext(context: Context): void {
  sessionStorage.setItem(CONTEXT_KEY, context);
}

export function hasUsername(): boolean {
  return getUsername() !== null;
}

export function hasLearnSelection(): boolean {
  return hasUsername() && getLanguage() !== null && getContext() !== null;
}

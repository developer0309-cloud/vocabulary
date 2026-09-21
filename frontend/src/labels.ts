import type { Context, Form, Language } from "@/api/schemas";

export const languageLabels: Record<Language, string> = {
  GERMAN: "Deutsch",
  ENGLISH: "Englisch",
};

export const contextLabels: Record<Context, string> = {
  NOUN: "Substantiv",
  VERB: "Verb",
  ADJECTIVE: "Adjektiv",
};

export const formLabels: Record<Form, string> = {
  SINGULAR: "Singular",
  PLURAL: "Plural",
  PRESENT: "Gegenwart",
  FIRST_PAST: "1. Vergangenheit",
  SECOND_PAST: "2. Vergangenheit",
  BASE: "Grundform",
  FIRST_COMPARATIVE: "1. Steigerung",
  SECOND_COMPARATIVE: "2. Steigerung",
};

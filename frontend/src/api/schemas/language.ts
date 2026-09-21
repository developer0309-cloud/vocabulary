import { z } from "zod";

export const languageSchema = z.enum(["GERMAN", "ENGLISH"]);

export type Language = z.infer<typeof languageSchema>;

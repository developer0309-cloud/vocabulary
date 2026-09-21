import { z } from "zod";
import { contextSchema } from "./context";
import { languageSchema } from "./language";

export const randomVocableQuerySchema = z.object({
  language: languageSchema,
  context: contextSchema,
});

export type RandomVocableQuery = z.infer<typeof randomVocableQuerySchema>;

import { z } from "zod";
import { associateSchema } from "./vocable-response";

export const scoreResponseSchema = z.object({
  correct: z.boolean(),
  associates: z.array(associateSchema).min(1),
});

export type ScoreResponse = z.infer<typeof scoreResponseSchema>;

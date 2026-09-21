import { z } from "zod";

export const scoreRequestSchema = z.object({
  translation: z.string().trim().min(1).max(255),
});

export type ScoreRequest = z.infer<typeof scoreRequestSchema>;

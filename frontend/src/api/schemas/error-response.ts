import { z } from "zod";

export const errorResponseSchema = z.object({
  message: z.string().min(1),
});

export type ErrorResponse = z.infer<typeof errorResponseSchema>;

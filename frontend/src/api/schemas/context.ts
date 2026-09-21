import { z } from "zod";

export const contextSchema = z.enum(["NOUN", "VERB", "ADJECTIVE"]);

export type Context = z.infer<typeof contextSchema>;

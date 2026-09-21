import { z } from "zod";

export const nounFormSchema = z.enum(["SINGULAR", "PLURAL"]);
export const verbFormSchema = z.enum(["PRESENT", "FIRST_PAST", "SECOND_PAST"]);
export const adjectiveFormSchema = z.enum([
  "BASE",
  "FIRST_COMPARATIVE",
  "SECOND_COMPARATIVE",
]);

export const formSchema = z.union([
  nounFormSchema,
  verbFormSchema,
  adjectiveFormSchema,
]);

export type NounForm = z.infer<typeof nounFormSchema>;
export type VerbForm = z.infer<typeof verbFormSchema>;
export type AdjectiveForm = z.infer<typeof adjectiveFormSchema>;
export type Form = z.infer<typeof formSchema>;

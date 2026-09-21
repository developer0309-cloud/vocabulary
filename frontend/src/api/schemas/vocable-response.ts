import { z } from "zod";
import {
  adjectiveFormSchema,
  formSchema,
  nounFormSchema,
  verbFormSchema,
} from "./form";
import { languageSchema } from "./language";

const vocableIdSchema = z.string().uuid();
const vocableTextSchema = z.string().trim().min(1).max(255);

export const associateSchema = z.object({
  id: vocableIdSchema,
  text: vocableTextSchema,
  language: languageSchema,
  form: formSchema,
});

const vocableFields = {
  id: vocableIdSchema,
  text: vocableTextSchema,
  language: languageSchema,
  associates: z.array(associateSchema).min(1),
};

export const vocableResponseSchema = z
  .discriminatedUnion("context", [
    z.object({
      ...vocableFields,
      context: z.literal("NOUN"),
      form: nounFormSchema,
    }),
    z.object({
      ...vocableFields,
      context: z.literal("VERB"),
      form: verbFormSchema,
    }),
    z.object({
      ...vocableFields,
      context: z.literal("ADJECTIVE"),
      form: adjectiveFormSchema,
    }),
  ])
  .superRefine((vocable, ctx) => {
    const expectedPartner =
      vocable.language === "GERMAN" ? "ENGLISH" : "GERMAN";
    for (const [index, associate] of vocable.associates.entries()) {
      if (associate.language !== expectedPartner) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["associates", index, "language"],
          message: `Associate language must be ${expectedPartner}`,
        });
      }
      if (associate.form !== vocable.form) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          path: ["associates", index, "form"],
          message: "Associate form must match the vocable form",
        });
      }
    }
  });

export type Associate = z.infer<typeof associateSchema>;
export type VocableResponse = z.infer<typeof vocableResponseSchema>;

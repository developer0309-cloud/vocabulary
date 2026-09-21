import { describe, expect, it } from "vitest";
import { errorResponseSchema } from "./error-response";
import { randomVocableQuerySchema } from "./random-vocable-query";
import { scoreRequestSchema } from "./score-request";
import { scoreResponseSchema } from "./score-response";
import { vocableResponseSchema } from "./vocable-response";

const house: unknown = {
  id: "11111111-1111-4111-8111-111111111111",
  text: "Haus",
  language: "GERMAN",
  context: "NOUN",
  form: "SINGULAR",
  associates: [
    {
      id: "22222222-2222-4222-8222-222222222222",
      text: "house",
      language: "ENGLISH",
      form: "SINGULAR",
    },
  ],
};

describe("vocableResponseSchema", () => {
  it("parses a German noun with English associates", () => {
    const parsed = vocableResponseSchema.parse(house);
    expect(parsed.text).toBe("Haus");
    expect(parsed.associates).toHaveLength(1);
  });

  it("rejects a form that does not match the context", () => {
    const result = vocableResponseSchema.safeParse({
      ...house,
      form: "PRESENT",
    });
    expect(result.success).toBe(false);
  });

  it("rejects associates in the same language", () => {
    const result = vocableResponseSchema.safeParse({
      ...house,
      associates: [
        {
          id: "22222222-2222-4222-8222-222222222222",
          text: "Heim",
          language: "GERMAN",
          form: "SINGULAR",
        },
      ],
    });
    expect(result.success).toBe(false);
  });

  it("rejects an empty associate list", () => {
    const result = vocableResponseSchema.safeParse({
      ...house,
      associates: [],
    });
    expect(result.success).toBe(false);
  });
});

describe("randomVocableQuerySchema", () => {
  it("parses language and context query parameters", () => {
    expect(
      randomVocableQuerySchema.parse({ language: "ENGLISH", context: "VERB" }),
    ).toEqual({ language: "ENGLISH", context: "VERB" });
  });
});

describe("errorResponseSchema", () => {
  it("parses a 404 body", () => {
    expect(
      errorResponseSchema.parse({
        message: "No vocable found for language GERMAN and context NOUN",
      }),
    ).toEqual({
      message: "No vocable found for language GERMAN and context NOUN",
    });
  });
});

describe("scoreRequestSchema", () => {
  it("trims translation text", () => {
    expect(scoreRequestSchema.parse({ translation: "  house  " })).toEqual({
      translation: "house",
    });
  });
});

describe("scoreResponseSchema", () => {
  it("parses a server score result", () => {
    const parsed = scoreResponseSchema.parse({
      correct: true,
      associates: [
        {
          id: "22222222-2222-4222-8222-222222222222",
          text: "house",
          language: "ENGLISH",
          form: "SINGULAR",
        },
      ],
    });
    expect(parsed.correct).toBe(true);
  });
});

import {
  errorResponseSchema,
  randomVocableQuerySchema,
  scoreRequestSchema,
  scoreResponseSchema,
  vocableResponseSchema,
  type RandomVocableQuery,
  type ScoreResponse,
  type VocableResponse,
} from "@/api/schemas";

const API_ROOT = "/vocabulary/v1/vocables";

export type RandomVocableResult =
  | { kind: "found"; vocable: VocableResponse }
  | { kind: "empty" };

export async function fetchRandomVocable(
  query: RandomVocableQuery,
): Promise<RandomVocableResult> {
  const params = randomVocableQuerySchema.parse(query);
  const search = new URLSearchParams(params);
  const response = await fetch(`${API_ROOT}/random?${search.toString()}`);
  const body: unknown = await response.json();
  if (response.status === 404) {
    errorResponseSchema.parse(body);
    return { kind: "empty" };
  }
  if (!response.ok) {
    throw new Error("Random vocable request failed");
  }
  return { kind: "found", vocable: vocableResponseSchema.parse(body) };
}

export async function scoreTranslation(
  vocableId: string,
  translation: string,
): Promise<ScoreResponse> {
  const payload = scoreRequestSchema.parse({ translation });
  const response = await fetch(`${API_ROOT}/${vocableId}/score`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  const body: unknown = await response.json();
  if (!response.ok) {
    throw new Error("Score request failed");
  }
  return scoreResponseSchema.parse(body);
}

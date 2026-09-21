import { useEffect, useState, type FormEvent, type JSX } from "react";
import { useNavigate } from "react-router-dom";
import type { ScoreResponse, VocableResponse } from "@/api/schemas";
import { fetchRandomVocable, scoreTranslation } from "@/api/vocables";
import { AppNav } from "@/components/AppNav";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { formLabels } from "@/labels";
import { getContext, getLanguage } from "@/session";

export function LearnPage(): JSX.Element {
  const navigate = useNavigate();
  const language = getLanguage();
  const context = getContext();
  const [vocable, setVocable] = useState<VocableResponse | null>(null);
  const [translation, setTranslation] = useState("");
  const [score, setScore] = useState<ScoreResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  async function loadVocable(): Promise<void> {
    if (language === null || context === null) {
      navigate("/", { replace: true });
      return;
    }
    setLoading(true);
    setError(null);
    setScore(null);
    setTranslation("");
    try {
      const result = await fetchRandomVocable({ language, context });
      if (result.kind === "empty") {
        navigate("/empty", { replace: true });
        return;
      }
      setVocable(result.vocable);
    } catch {
      setError("Die Vokabel konnte nicht geladen werden.");
      setVocable(null);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void loadVocable();
  }, []);

  async function onReveal(event: FormEvent<HTMLFormElement>): Promise<void> {
    event.preventDefault();
    if (vocable === null) {
      return;
    }
    setError(null);
    try {
      const result = await scoreTranslation(vocable.id, translation);
      setScore(result);
    } catch {
      setError("Die Übersetzung konnte nicht geprüft werden.");
    }
  }

  return (
    <main className="mx-auto max-w-md p-6">
      <AppNav />
      <h1 className="mb-6 text-2xl font-semibold">Lernseite</h1>
      {loading ? <p>Lade Vokabel…</p> : null}
      {error !== null ? <p className="text-red-700">{error}</p> : null}
      {vocable !== null && !loading ? (
        <form className="space-y-4" onSubmit={(event) => void onReveal(event)}>
          <p className="text-lg">
            <span className="font-medium">{vocable.text}</span>
            <span className="ml-2 text-slate-600">
              ({formLabels[vocable.form]})
            </span>
          </p>
          <div className="space-y-2">
            <Label htmlFor="translation">Übersetzung</Label>
            <Input
              id="translation"
              name="translation"
              required
              value={translation}
              onChange={(event) => setTranslation(event.target.value)}
            />
          </div>
          <Button type="submit">Anzeigen</Button>
        </form>
      ) : null}
      {score !== null ? (
        <section className="mt-6 space-y-3">
          <p className={score.correct ? "text-green-700" : "text-red-700"}>
            {score.correct ? "Korrekte Übersetzung" : "Leider falsch"}
          </p>
          <ul className="list-disc pl-5">
            {score.associates.map((associate) => (
              <li key={associate.id}>{associate.text}</li>
            ))}
          </ul>
          <Button type="button" variant="outline" onClick={() => void loadVocable()}>
            Neue Vokabel
          </Button>
        </section>
      ) : null}
    </main>
  );
}

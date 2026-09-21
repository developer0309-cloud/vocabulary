import { useState, type FormEvent, type JSX } from "react";
import { useNavigate } from "react-router-dom";
import { contextSchema, languageSchema, type Context, type Language } from "@/api/schemas";
import { AppNav } from "@/components/AppNav";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { contextLabels, languageLabels } from "@/labels";
import { getUsername, setContext, setLanguage } from "@/session";

const languages: Language[] = ["GERMAN", "ENGLISH"];
const contexts: Context[] = ["NOUN", "VERB", "ADJECTIVE"];

export function SelectionPage(): JSX.Element {
  const navigate = useNavigate();
  const username = getUsername() ?? "";
  const [language, setLanguageField] = useState<Language>("GERMAN");
  const [context, setContextField] = useState<Context>("NOUN");

  function onSubmit(event: FormEvent<HTMLFormElement>): void {
    event.preventDefault();
    setLanguage(language);
    setContext(context);
    navigate("/learn");
  }

  return (
    <main className="mx-auto max-w-md p-6">
      <AppNav />
      <h1 className="mb-2 text-2xl font-semibold">Auswahl</h1>
      <p className="mb-6 text-slate-700">Hallo {username}.</p>
      <form className="space-y-4" onSubmit={onSubmit}>
        <div className="space-y-2">
          <Label htmlFor="language">Vokabelsprache</Label>
          <select
            id="language"
            className="flex h-10 w-full rounded-md border border-slate-300 bg-white px-3 text-sm"
            value={language}
            onChange={(event) =>
              setLanguageField(languageSchema.parse(event.target.value))
            }
            required
          >
            {languages.map((value) => (
              <option key={value} value={value}>
                {languageLabels[value]}
              </option>
            ))}
          </select>
        </div>
        <div className="space-y-2">
          <Label htmlFor="context">Kontext</Label>
          <select
            id="context"
            className="flex h-10 w-full rounded-md border border-slate-300 bg-white px-3 text-sm"
            value={context}
            onChange={(event) =>
              setContextField(contextSchema.parse(event.target.value))
            }
            required
          >
            {contexts.map((value) => (
              <option key={value} value={value}>
                {contextLabels[value]}
              </option>
            ))}
          </select>
        </div>
        <Button type="submit">Zur Lernseite</Button>
      </form>
    </main>
  );
}

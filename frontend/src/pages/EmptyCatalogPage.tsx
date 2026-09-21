import type { JSX } from "react";
import { Link } from "react-router-dom";
import { AppNav } from "@/components/AppNav";
import { Button } from "@/components/ui/button";

export function EmptyCatalogPage(): JSX.Element {
  return (
    <main className="mx-auto max-w-md p-6">
      <AppNav />
      <h1 className="mb-4 text-2xl font-semibold">Keine Vokabeln vorhanden</h1>
      <p className="mb-6 text-slate-700">
        Zur ausgewählten Sprache und zum ausgewählten Kontext konnten keine
        Vokabeln gefunden werden.
      </p>
      <Button asChild>
        <Link to="/select">Zur Sprach- und Kontextauswahl</Link>
      </Button>
    </main>
  );
}

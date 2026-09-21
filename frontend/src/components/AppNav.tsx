import type { JSX } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

export function AppNav(): JSX.Element {
  return (
    <nav className="mb-6 flex flex-wrap gap-2">
      <Button asChild variant="outline" size="sm">
        <Link to="/select">Zur Sprach- und Kontextauswahl</Link>
      </Button>
      <Button asChild variant="outline" size="sm">
        <Link to="/end">Zur Endeseite</Link>
      </Button>
    </nav>
  );
}

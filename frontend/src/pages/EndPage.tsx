import { useState, type JSX } from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { clearUsername, getUsername } from "@/session";

const FAREWELL_KEY = "vocabulary.farewell";

export function EndPage(): JSX.Element {
  const [username] = useState(() => {
    const live = getUsername();
    if (live !== null) {
      sessionStorage.setItem(FAREWELL_KEY, live);
      clearUsername();
      return live;
    }
    return sessionStorage.getItem(FAREWELL_KEY) ?? "";
  });

  return (
    <main className="mx-auto max-w-md p-6">
      <h1 className="mb-4 text-2xl font-semibold">Ende</h1>
      <p className="mb-6 text-slate-700">Auf Wiedersehen {username}.</p>
      <Button asChild>
        <Link to="/" onClick={() => sessionStorage.removeItem(FAREWELL_KEY)}>
          Zurück zum Start
        </Link>
      </Button>
    </main>
  );
}

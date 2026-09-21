import { useState, type FormEvent, type JSX } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { setUsername } from "@/session";

export function StartPage(): JSX.Element {
  const navigate = useNavigate();
  const [username, setUsernameField] = useState("");

  function onSubmit(event: FormEvent<HTMLFormElement>): void {
    event.preventDefault();
    const trimmed = username.trim();
    if (trimmed === "") {
      return;
    }
    setUsername(trimmed);
    navigate("/select");
  }

  return (
    <main className="mx-auto max-w-md p-6">
      <h1 className="mb-6 text-2xl font-semibold">Vokabeltrainer</h1>
      <form className="space-y-4" onSubmit={onSubmit}>
        <div className="space-y-2">
          <Label htmlFor="username">Benutzername</Label>
          <Input
            id="username"
            name="username"
            required
            value={username}
            onChange={(event) => setUsernameField(event.target.value)}
          />
        </div>
        <Button type="submit">Anmelden</Button>
      </form>
    </main>
  );
}

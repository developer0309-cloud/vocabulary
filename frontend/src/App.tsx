import type { JSX } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import {
  RequireLearnSelection,
  RequireUsername,
} from "@/components/RouteGuards";
import { EmptyCatalogPage } from "@/pages/EmptyCatalogPage";
import { EndPage } from "@/pages/EndPage";
import { LearnPage } from "@/pages/LearnPage";
import { SelectionPage } from "@/pages/SelectionPage";
import { StartPage } from "@/pages/StartPage";

export function App(): JSX.Element {
  return (
    <Routes>
      <Route path="/" element={<StartPage />} />
      <Route
        path="/select"
        element={
          <RequireUsername>
            <SelectionPage />
          </RequireUsername>
        }
      />
      <Route
        path="/learn"
        element={
          <RequireLearnSelection>
            <LearnPage />
          </RequireLearnSelection>
        }
      />
      <Route
        path="/empty"
        element={
          <RequireLearnSelection>
            <EmptyCatalogPage />
          </RequireLearnSelection>
        }
      />
      <Route
        path="/end"
        element={
          <RequireUsername>
            <EndPage />
          </RequireUsername>
        }
      />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

import type { JSX } from "react";
import { Navigate } from "react-router-dom";
import { hasLearnSelection, hasUsername } from "@/session";

interface GuardProps {
  children: JSX.Element;
}

export function RequireUsername({ children }: GuardProps): JSX.Element {
  if (!hasUsername()) {
    return <Navigate to="/" replace />;
  }
  return children;
}

export function RequireLearnSelection({
  children,
}: GuardProps): JSX.Element {
  if (!hasLearnSelection()) {
    return <Navigate to="/" replace />;
  }
  return children;
}

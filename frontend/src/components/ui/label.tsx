import * as LabelPrimitive from "@radix-ui/react-label";
import { type JSX } from "react";
import * as React from "react";
import { cn } from "@/lib/utils";

export type LabelProps = React.ComponentPropsWithoutRef<typeof LabelPrimitive.Root>;

export function Label({ className, ...props }: LabelProps): JSX.Element {
  return (
    <LabelPrimitive.Root
      className={cn("text-sm font-medium leading-none", className)}
      {...props}
    />
  );
}

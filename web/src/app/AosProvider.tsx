"use client";

import { useEffect } from "react";
import Aos from "aos";
import "aos/dist/aos.css";

interface AosProviderProps {
  readonly children: React.ReactNode;
}

export default function AosProvider({ children }: AosProviderProps) {
  useEffect(() => {
    Aos.init({ once: true });
  }, []);

  return <>{children}</>;
}
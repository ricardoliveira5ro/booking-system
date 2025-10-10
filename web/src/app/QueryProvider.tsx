"use client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

interface QueryProviderProps {
  readonly children: React.ReactNode;
}

export default function QueryProvider({ children }: QueryProviderProps) {
    const queryClient = new QueryClient();

    return <QueryClientProvider client={queryClient}>
                {children}
            </QueryClientProvider>;
}
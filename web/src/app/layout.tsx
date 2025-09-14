import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { NextIntlClientProvider } from "next-intl";
import AosProvider from "./AosProvider";
import QueryProvider from "./QueryProvider";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Abel BarberShop",
  description: "Book your next appointment in a flash",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode}>) {

  return (
    <html>
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <QueryProvider>
          <NextIntlClientProvider>
            <AosProvider>{children}</AosProvider>
          </NextIntlClientProvider>
        </QueryProvider>
      </body>
    </html>
  );
}

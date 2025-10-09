import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import { NextIntlClientProvider } from "next-intl";
import { ToastContainer } from "react-toastify";

import AosProvider from "./AosProvider";
import QueryProvider from "./QueryProvider";

import "./globals.css";

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
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <QueryProvider>
          <NextIntlClientProvider>
            <AosProvider>
              {children}
              <ToastContainer className="px-8 py-2" />
            </AosProvider>
          </NextIntlClientProvider>
        </QueryProvider>
      </body>
    </html>
  );
}

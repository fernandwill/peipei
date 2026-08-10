import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Peipei — Payment Gateway Sandbox",
  description: "Sandbox payment gateway merchant dashboard",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}

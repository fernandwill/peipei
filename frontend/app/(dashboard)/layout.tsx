import {
  Banknote,
  Gauge,
  KeyRound,
  Layers,
  RefreshCcw,
  Settings,
  Users,
  Webhook,
} from "lucide-react";

const navigation = [
  { name: "Dashboard", icon: Gauge },
  { name: "Payments", icon: Banknote },
  { name: "Refunds", icon: RefreshCcw },
  { name: "Customers", icon: Users },
  { name: "Webhooks", icon: Webhook },
  { name: "API Keys", icon: KeyRound },
  { name: "Audit Logs", icon: Layers },
  { name: "Settings", icon: Settings },
];

/**
 * Static shell shared by every dashboard route. Rendered once (prerendered with PPR),
 * never re-rendered on navigation, and streamed instantly while route content loads.
 */
export default function DashboardLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="flex min-h-screen bg-slate-50 text-slate-900">
      {/* Sidebar */}
      <aside className="flex w-64 shrink-0 flex-col border-r border-slate-800 bg-slate-950 text-slate-300">
        <div className="flex items-center gap-2.5 px-6 py-5">
          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-indigo-500 font-semibold text-white">
            P
          </div>
          <div className="leading-tight">
            <div className="text-sm font-semibold text-white">Peipei</div>
            <div className="text-[11px] uppercase tracking-wider text-slate-500">Sandbox</div>
          </div>
        </div>

        <nav className="mt-2 flex-1 space-y-1 px-3">
          {navigation.map((item) => (
            <a
              key={item.name}
              href="#"
              className={`flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
                item.name === "Dashboard"
                  ? "bg-indigo-500/15 text-indigo-300"
                  : "text-slate-400 hover:bg-slate-900 hover:text-slate-200"
              }`}
            >
              <item.icon className="h-4 w-4" />
              {item.name}
            </a>
          ))}
        </nav>

        <div className="border-t border-slate-800 p-4">
          <div className="rounded-lg bg-slate-900 p-3 text-xs text-slate-400">
            <div className="font-medium text-slate-200">Sandbox account</div>
            <div className="mt-0.5 truncate">merchant@example.com</div>
            <div className="mt-2 inline-flex rounded bg-emerald-500/10 px-1.5 py-0.5 text-[11px] font-medium text-emerald-400">
              ACTIVE
            </div>
          </div>
        </div>
      </aside>

      {/* Content column — each route streams its own HTML into {children} */}
      <div className="flex-1 overflow-y-auto">
        <header className="sticky top-0 z-10 border-b border-slate-200 bg-white/80 backdrop-blur">
          <div className="flex items-center justify-end px-8 py-4">
            <span className="rounded-full border border-amber-200 bg-amber-50 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-amber-700">
              Sandbox mode
            </span>
          </div>
        </header>
        {children}
      </div>
    </div>
  );
}

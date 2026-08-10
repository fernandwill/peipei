import {
  Banknote,
  CheckCircle2,
  Gauge,
  KeyRound,
  Layers,
  ReceiptText,
  RefreshCcw,
  Settings,
  Users,
  Webhook,
  XCircle,
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

const stats = [
  { name: "Total Volume", icon: Banknote },
  { name: "Successful Payments", icon: CheckCircle2 },
  { name: "Failed Payments", icon: XCircle },
  { name: "Refunds", icon: RefreshCcw },
  { name: "Success Rate", icon: Gauge },
];

export default function DashboardPage() {
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

      {/* Main */}
      <div className="flex-1 overflow-y-auto">
        <header className="sticky top-0 z-10 border-b border-slate-200 bg-white/80 backdrop-blur">
          <div className="flex items-center justify-between px-8 py-4">
            <div>
              <h1 className="text-lg font-semibold text-slate-900">Dashboard</h1>
              <p className="text-sm text-slate-500">Overview of your gateway activity</p>
            </div>
            <span className="rounded-full border border-amber-200 bg-amber-50 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-amber-700">
              Sandbox mode
            </span>
          </div>
        </header>

        <main className="space-y-6 px-8 py-6">
          {/* Stat cards — §35 */}
          <div className="grid grid-cols-2 gap-4 lg:grid-cols-5">
            {stats.map((stat) => (
              <div
                key={stat.name}
                className="rounded-xl border border-slate-200 bg-white p-4 shadow-sm transition-shadow hover:shadow-md"
              >
                <div className="flex items-center justify-between">
                  <span className="text-xs font-medium uppercase tracking-wide text-slate-500">
                    {stat.name}
                  </span>
                  <stat.icon className="h-4 w-4 text-slate-400" />
                </div>
                <div className="mt-2 text-2xl font-semibold tabular-nums text-slate-900">—</div>
                <div className="mt-1 text-xs text-slate-400">Awaiting API connection</div>
              </div>
            ))}
          </div>

          {/* Recent activity placeholder */}
          <div className="rounded-xl border border-dashed border-slate-300 bg-white p-12 text-center">
            <ReceiptText className="mx-auto h-10 w-10 text-slate-300" />
            <h2 className="mt-4 text-sm font-semibold text-slate-700">No payments yet</h2>
            <p className="mx-auto mt-1 max-w-sm text-sm text-slate-500">
              Once the backend API is connected, payments, refunds, and webhook deliveries will
              appear here.
            </p>
          </div>
        </main>
      </div>
    </div>
  );
}

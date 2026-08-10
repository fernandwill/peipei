import {
  Banknote,
  CheckCircle2,
  Gauge,
  ReceiptText,
  RefreshCcw,
  XCircle,
} from "lucide-react";

const stats = [
  { name: "Total Volume", icon: Banknote },
  { name: "Successful Payments", icon: CheckCircle2 },
  { name: "Failed Payments", icon: XCircle },
  { name: "Refunds", icon: RefreshCcw },
  { name: "Success Rate", icon: Gauge },
];

export default function DashboardPage() {
  return (
    <main className="space-y-6 px-8 py-6">
      <div>
        <h1 className="text-lg font-semibold text-slate-900">Dashboard</h1>
        <p className="text-sm text-slate-500">Overview of your gateway activity</p>
      </div>

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
          Once the backend API is connected, payments, refunds, and webhook deliveries will appear
          here.
        </p>
      </div>
    </main>
  );
}

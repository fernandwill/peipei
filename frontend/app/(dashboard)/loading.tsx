/**
 * Streamed fallback for the dashboard route. The shell (layout) paints instantly;
 * this skeleton shows while route data resolves.
 */
export default function DashboardLoading() {
  return (
    <main className="space-y-6 px-8 py-6">
      <div className="animate-pulse space-y-1.5">
        <div className="h-5 w-40 rounded bg-slate-200" />
        <div className="h-4 w-64 rounded bg-slate-200" />
      </div>

      <div className="grid grid-cols-2 gap-4 lg:grid-cols-5">
        {Array.from({ length: 5 }).map((_, i) => (
          <div
            key={i}
            className="animate-pulse rounded-xl border border-slate-200 bg-white p-4 shadow-sm"
          >
            <div className="h-3 w-24 rounded bg-slate-200" />
            <div className="mt-3 h-7 w-16 rounded bg-slate-200" />
            <div className="mt-2 h-3 w-28 rounded bg-slate-200" />
          </div>
        ))}
      </div>

      <div className="animate-pulse rounded-xl border border-dashed border-slate-300 bg-white p-12">
        <div className="mx-auto h-10 w-10 rounded-full bg-slate-200" />
        <div className="mx-auto mt-4 h-4 w-32 rounded bg-slate-200" />
        <div className="mx-auto mt-2 h-3 w-72 rounded bg-slate-200" />
      </div>
    </main>
  );
}

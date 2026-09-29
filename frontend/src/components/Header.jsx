import { CreditCard, Activity, ShieldCheck } from 'lucide-react'

export default function Header() {
  return (
    <header className="hero">
      <div>
        <span className="eyebrow">JAVA 21 • SPRING BOOT • KAFKA • REDIS • POSTGRESQL</span>
        <h1>Payment Gateway Engine</h1>
        <p>
          High-throughput payment processing microservice with idempotency, retries,
          distributed tracing, and a real-time friendly operations dashboard.
        </p>
      </div>
      <div className="hero-cards">
        <div className="glass-card floating">
          <CreditCard size={26} />
          <strong>Concurrent Payments</strong>
          <span>Reliable async processing</span>
        </div>
        <div className="glass-card floating delay-1">
          <ShieldCheck size={26} />
          <strong>Idempotent Requests</strong>
          <span>No duplicate charges</span>
        </div>
        <div className="glass-card floating delay-2">
          <Activity size={26} />
          <strong>Zipkin Tracing</strong>
          <span>Full request visibility</span>
        </div>
      </div>
    </header>
  )
}

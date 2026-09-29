import { IndianRupee, Clock3, CheckCircle2, RotateCcw } from 'lucide-react'

export default function StatsGrid({ payments }) {
  const success = payments.filter(p => p.status === 'SUCCESS').length
  const pending = payments.filter(p => ['PENDING', 'PROCESSING', 'RETRYING'].includes(p.status)).length
  const retries = payments.filter(p => p.status === 'RETRYING').length
  const volume = payments.reduce((sum, p) => sum + Number(p.amount || 0), 0)

  const cards = [
    { title: 'Processed Volume', value: `₹${volume.toLocaleString()}`, icon: <IndianRupee size={20} /> },
    { title: 'Successful Payments', value: success, icon: <CheckCircle2 size={20} /> },
    { title: 'In Pipeline', value: pending, icon: <Clock3 size={20} /> },
    { title: 'Retries Triggered', value: retries, icon: <RotateCcw size={20} /> }
  ]

  return (
    <section className="stats-grid">
      {cards.map(card => (
        <div key={card.title} className="panel stat-card">
          <div className="stat-icon">{card.icon}</div>
          <div>
            <p>{card.title}</p>
            <h3>{card.value}</h3>
          </div>
        </div>
      ))}
    </section>
  )
}

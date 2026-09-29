import { useEffect, useState } from 'react'
import Header from './components/Header'
import StatsGrid from './components/StatsGrid'
import PaymentForm from './components/PaymentForm'
import PaymentTable from './components/PaymentTable'
import { createPayment, fetchPayments } from './services/api'

export default function App() {
  const [payments, setPayments] = useState([])
  const [loading, setLoading] = useState(false)
  const [toast, setToast] = useState('')

  const loadPayments = async () => {
    try {
      const data = await fetchPayments()
      setPayments(data)
    } catch {
      setToast('Backend not reachable. Start Spring Boot on port 8080.')
    }
  }

  useEffect(() => {
    loadPayments()
    const interval = setInterval(loadPayments, 3000)
    return () => clearInterval(interval)
  }, [])

  const handleCreatePayment = async (payload) => {
    try {
      setLoading(true)
      const response = await createPayment(payload)
      setToast(`Payment accepted: ${response.paymentId || 'existing reference returned'}`)
      await loadPayments()
    } catch (error) {
      setToast(error?.response?.data?.error || 'Failed to create payment')
    } finally {
      setLoading(false)
      setTimeout(() => setToast(''), 3000)
    }
  }

  return (
    <div className="app-shell">
      <div className="background-blur blur-one" />
      <div className="background-blur blur-two" />
      <main className="container">
        <Header />
        {toast && <div className="toast">{toast}</div>}
        <StatsGrid payments={payments} />
        <div className="content-grid">
          <PaymentForm onSubmit={handleCreatePayment} loading={loading} />
          <div className="panel side-panel">
            <h2>Architecture</h2>
            <ul>
              <li>REST request enters Spring Boot API</li>
              <li>Idempotency checked in Redis + PostgreSQL</li>
              <li>Payment event pushed to Kafka topic</li>
              <li>Consumer processes event with retry logic</li>
              <li>Tracing exported to Zipkin</li>
            </ul>
            <div className="trace-box">
              <span className="trace-dot" />
              Trace enabled for end-to-end payment flow
            </div>
          </div>
        </div>
        <PaymentTable payments={payments} />
      </main>
    </div>
  )
}

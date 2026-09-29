import { useState } from 'react'
import { SendHorizonal } from 'lucide-react'

const initialState = {
  idempotencyKey: `txn-${Date.now()}`,
  merchantId: 'M-100',
  customerId: 'C-200',
  amount: '4999',
  currency: 'INR',
  paymentMethod: 'UPI'
}

export default function PaymentForm({ onSubmit, loading }) {
  const [form, setForm] = useState(initialState)

  const handleChange = (e) => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    await onSubmit({ ...form, amount: Number(form.amount) })
    setForm(prev => ({ ...prev, idempotencyKey: `txn-${Date.now()}` }))
  }

  return (
    <section className="panel form-panel">
      <div className="section-head">
        <div>
          <h2>Create Payment</h2>
          <p>Submit a new payment request and test idempotency with a repeated key.</p>
        </div>
      </div>
      <form className="payment-form" onSubmit={handleSubmit}>
        <div className="input-grid">
          <label>
            Idempotency Key
            <input name="idempotencyKey" value={form.idempotencyKey} onChange={handleChange} required />
          </label>
          <label>
            Merchant ID
            <input name="merchantId" value={form.merchantId} onChange={handleChange} required />
          </label>
          <label>
            Customer ID
            <input name="customerId" value={form.customerId} onChange={handleChange} required />
          </label>
          <label>
            Amount
            <input name="amount" type="number" min="1" value={form.amount} onChange={handleChange} required />
          </label>
          <label>
            Currency
            <select name="currency" value={form.currency} onChange={handleChange}>
              <option>INR</option>
              <option>USD</option>
              <option>EUR</option>
            </select>
          </label>
          <label>
            Method
            <select name="paymentMethod" value={form.paymentMethod} onChange={handleChange}>
              <option>UPI</option>
              <option>CARD</option>
              <option>NET_BANKING</option>
              <option>WALLET</option>
            </select>
          </label>
        </div>
        <button className="primary-btn" type="submit" disabled={loading}>
          <SendHorizonal size={18} />
          {loading ? 'Submitting...' : 'Process Payment'}
        </button>
      </form>
    </section>
  )
}

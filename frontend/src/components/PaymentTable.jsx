const statusClass = {
  SUCCESS: 'success',
  FAILED: 'failed',
  RETRYING: 'retrying',
  PROCESSING: 'processing',
  PENDING: 'pending'
}

export default function PaymentTable({ payments }) {
  return (
    <section className="panel table-panel">
      <div className="section-head">
        <div>
          <h2>Recent Transactions</h2>
          <p>Latest 20 requests persisted in PostgreSQL.</p>
        </div>
      </div>
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Payment ID</th>
              <th>Merchant</th>
              <th>Customer</th>
              <th>Amount</th>
              <th>Method</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {payments.length === 0 ? (
              <tr>
                <td colSpan="6" className="empty-state">No payments yet. Submit one from the form.</td>
              </tr>
            ) : (
              payments.map(payment => (
                <tr key={payment.paymentId}>
                  <td className="mono">{payment.paymentId?.slice(0, 8)}...</td>
                  <td>{payment.merchantId}</td>
                  <td>{payment.customerId}</td>
                  <td>₹{Number(payment.amount || 0).toLocaleString()}</td>
                  <td>{payment.paymentMethod}</td>
                  <td>
                    <span className={`pill ${statusClass[payment.status] || 'pending'}`}>{payment.status || 'ACCEPTED'}</span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </section>
  )
}

import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})

export const fetchPayments = async () => {
  const { data } = await api.get('/payments')
  return data
}

export const createPayment = async (payload) => {
  const { data } = await api.post('/payments', payload)
  return data
}

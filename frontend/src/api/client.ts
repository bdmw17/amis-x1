import axios from 'axios'
import { useToastStore } from '../stores/toast'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request-Interceptor: Basic-Auth aus Env
api.interceptors.request.use((config) => {
  const user = import.meta.env.VITE_API_USER ?? 'admin'
  const pass = import.meta.env.VITE_API_PASS ?? 'changeme'
  config.auth = { username: user, password: pass }
  return config
})

// Response-Interceptor: globale Fehlerbehandlung
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const toast = useToastStore()
    const status = error.response?.status

    if (status === 409) {
      // Optimistic Locking: Datensatz zwischenzeitlich geändert
      const detail = error.response?.data?.detail ?? 'Datensatz wurde zwischenzeitlich geändert. Bitte Seite neu laden.'
      toast.show(detail, 'warning', 8000)
    } else if (status === 401) {
      toast.show('Sitzung abgelaufen – bitte neu anmelden.', 'error')
    } else if (status === 403) {
      toast.show('Keine Berechtigung für diese Aktion.', 'error')
    } else if (status >= 500) {
      toast.show('Serverfehler – bitte später erneut versuchen.', 'error')
    }

    return Promise.reject(error)
  },
)

export default api

export default api

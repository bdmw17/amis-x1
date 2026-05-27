import { defineStore } from 'pinia'
import { ref } from 'vue'

export type ToastType = 'success' | 'warning' | 'error'

export interface Toast {
  id: number
  message: string
  type: ToastType
}

export const useToastStore = defineStore('toast', () => {
  const toasts = ref<Toast[]>([])
  let nextId = 0

  function show(message: string, type: ToastType = 'success', durationMs = 5000) {
    const id = nextId++
    toasts.value.push({ id, message, type })
    setTimeout(() => remove(id), durationMs)
  }

  function remove(id: number) {
    const idx = toasts.value.findIndex((t) => t.id === id)
    if (idx !== -1) toasts.value.splice(idx, 1)
  }

  return { toasts, show, remove }
})

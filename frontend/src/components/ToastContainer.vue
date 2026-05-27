<script setup lang="ts">
import { useToastStore } from '../stores/toast'

const toastStore = useToastStore()
</script>

<template>
  <div class="toast-container" role="region" aria-label="Benachrichtigungen" aria-live="polite">
    <div
      v-for="toast in toastStore.toasts"
      :key="toast.id"
      class="toast"
      :class="`toast-${toast.type}`"
      role="alert"
    >
      <span>{{ toast.message }}</span>
      <button
        class="btn-ghost"
        style="margin-left:12px;padding:2px 6px;font-size:.75rem;color:inherit;border-color:rgba(255,255,255,.4)"
        @click="toastStore.remove(toast.id)"
        aria-label="Schließen"
      >✕</button>
    </div>
  </div>
</template>

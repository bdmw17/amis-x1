<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
import ToastContainer from './components/ToastContainer.vue'
</script>

<template>
  <!-- Topbar -->
  <header class="topbar">
    <span class="brand">AMIS</span>
    <span class="text-muted" style="font-size:.8rem">Asylverwaltungsprogramm</span>
  </header>

  <!-- Sidebar -->
  <aside class="sidebar">
    <nav>
      <RouterLink to="/bewohner">Bewohner</RouterLink>
      <RouterLink to="/termine">Termine</RouterLink>
      <RouterLink to="/liegenschaft">Liegenschaft</RouterLink>
    </nav>
  </aside>

  <!-- Hauptinhalt -->
  <main class="main-content">
    <RouterView />
  </main>

  <!-- Globale Toast-Benachrichtigungen -->
  <ToastContainer />
</template>

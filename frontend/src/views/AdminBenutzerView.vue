<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '../api/client'

interface AfA { id: number; kuerzel: string; name: string }
interface Rolle { id: number; name: string }
interface Benutzer {
  id: number
  benutzername: string
  vorname: string
  nachname: string
  afa: AfA | null
  rollen: Rolle[]
  aktiv: boolean
}

const benutzer = ref<Benutzer[]>([])
const afaList = ref<AfA[]>([])
const rollenList = ref<Rolle[]>([])
const loading = ref(false)
const error = ref('')

const showForm = ref(false)
const editId = ref<number | null>(null)
const form = ref({
  benutzername: '',
  passwort: '',
  vorname: '',
  nachname: '',
  afaId: null as number | null,
  rollenIds: [] as number[],
  aktiv: true,
})

async function load() {
  loading.value = true
  error.value = ''
  try {
    const [bu, af, ro] = await Promise.all([
      api.get('/v1/admin/benutzer'),
      api.get('/v1/afa'),
      api.get('/v1/admin/rollen'),
    ])
    benutzer.value = bu.data
    afaList.value = af.data
    rollenList.value = ro.data
  } catch (e: any) {
    error.value = e.response?.data?.detail ?? 'Fehler beim Laden'
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editId.value = null
  form.value = { benutzername: '', passwort: '', vorname: '', nachname: '', afaId: null, rollenIds: [], aktiv: true }
  showForm.value = true
}

function openEdit(b: Benutzer) {
  editId.value = b.id
  form.value = {
    benutzername: b.benutzername,
    passwort: '',
    vorname: b.vorname,
    nachname: b.nachname,
    afaId: b.afa?.id ?? null,
    rollenIds: b.rollen.map(r => r.id),
    aktiv: b.aktiv,
  }
  showForm.value = true
}

async function save() {
  try {
    const payload = { ...form.value }
    if (editId.value) {
      await api.put(`/v1/admin/benutzer/${editId.value}`, payload)
    } else {
      await api.post('/v1/admin/benutzer', payload)
    }
    showForm.value = false
    await load()
  } catch (e: any) {
    error.value = e.response?.data?.detail ?? 'Speichern fehlgeschlagen'
  }
}

async function remove(id: number) {
  if (!confirm('Benutzer wirklich löschen?')) return
  try {
    await api.delete(`/v1/admin/benutzer/${id}`)
    await load()
  } catch (e: any) {
    error.value = e.response?.data?.detail ?? 'Löschen fehlgeschlagen'
  }
}

onMounted(load)
</script>

<template>
  <div class="page">
    <div class="page-header">
      <h1>Benutzerverwaltung</h1>
      <button class="btn-primary" @click="openCreate">+ Neuer Benutzer</button>
    </div>

    <p v-if="error" class="error-msg">{{ error }}</p>

    <div v-if="loading" class="loading">Lade…</div>

    <table v-else class="data-table">
      <thead>
        <tr>
          <th>Benutzername</th>
          <th>Name</th>
          <th>AfA</th>
          <th>Rollen</th>
          <th>Aktiv</th>
          <th>Aktionen</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="b in benutzer" :key="b.id">
          <td>{{ b.benutzername }}</td>
          <td>{{ b.vorname }} {{ b.nachname }}</td>
          <td>{{ b.afa?.kuerzel ?? '–' }}</td>
          <td>{{ b.rollen.map(r => r.name).join(', ') || '–' }}</td>
          <td>{{ b.aktiv ? 'Ja' : 'Nein' }}</td>
          <td>
            <button class="btn-sm" @click="openEdit(b)">Bearbeiten</button>
            <button class="btn-sm btn-danger" @click="remove(b.id)">Löschen</button>
          </td>
        </tr>
        <tr v-if="!benutzer.length">
          <td colspan="6" class="text-center text-muted">Keine Benutzer vorhanden</td>
        </tr>
      </tbody>
    </table>

    <!-- Formular-Modal -->
    <div v-if="showForm" class="modal-backdrop" @click.self="showForm = false">
      <div class="modal-box">
        <h2>{{ editId ? 'Benutzer bearbeiten' : 'Neuer Benutzer' }}</h2>

        <label>Benutzername *
          <input v-model="form.benutzername" :disabled="!!editId" required />
        </label>
        <label>Passwort {{ editId ? '(leer = unverändert)' : '*' }}
          <input v-model="form.passwort" type="password" :required="!editId" />
        </label>
        <label>Vorname <input v-model="form.vorname" /></label>
        <label>Nachname <input v-model="form.nachname" /></label>

        <label>AfA
          <select v-model="form.afaId">
            <option :value="null">– keine –</option>
            <option v-for="a in afaList" :key="a.id" :value="a.id">{{ a.kuerzel }} – {{ a.name }}</option>
          </select>
        </label>

        <label>Rollen
          <select v-model="form.rollenIds" multiple size="5">
            <option v-for="r in rollenList" :key="r.id" :value="r.id">{{ r.name }}</option>
          </select>
        </label>

        <label class="checkbox-label">
          <input type="checkbox" v-model="form.aktiv" /> Aktiv
        </label>

        <div class="modal-actions">
          <button class="btn-primary" @click="save">Speichern</button>
          <button @click="showForm = false">Abbrechen</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page { padding: 1.5rem; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem; }
.error-msg { color: #e53e3e; margin-bottom: .75rem; }
.loading { color: #718096; }
.data-table { width: 100%; border-collapse: collapse; }
.data-table th, .data-table td { padding: .5rem .75rem; border-bottom: 1px solid #e2e8f0; text-align: left; }
.data-table th { background: #f7fafc; font-weight: 600; }
.btn-primary { background: #3182ce; color: #fff; border: none; padding: .4rem .9rem; border-radius: 4px; cursor: pointer; }
.btn-sm { padding: .25rem .6rem; border: 1px solid #cbd5e0; border-radius: 4px; cursor: pointer; margin-right: .25rem; background: #fff; }
.btn-danger { border-color: #fc8181; color: #c53030; }
.text-center { text-align: center; }
.text-muted { color: #718096; }
.modal-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal-box { background: #fff; border-radius: 8px; padding: 1.5rem; width: 480px; max-height: 90vh; overflow-y: auto; }
.modal-box h2 { margin-top: 0; }
.modal-box label { display: flex; flex-direction: column; margin-bottom: .75rem; font-size: .875rem; font-weight: 500; gap: .25rem; }
.modal-box input, .modal-box select { border: 1px solid #cbd5e0; border-radius: 4px; padding: .35rem .5rem; font-size: .875rem; }
.checkbox-label { flex-direction: row !important; align-items: center; gap: .5rem; }
.modal-actions { display: flex; gap: .75rem; margin-top: 1rem; }
</style>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import api from '../api/client'

const MODULE_NAMES = [
  'BEWOHNER','AZR','SONDERSTATUS','FREITEXT','TERMINE','FORMULARE',
  'AUSWEIS','LIEGENSCHAFTEN','BELEGUNG','ANWESENHEIT','VERLEGUNG',
  'VERTEILUNG','BEWACHUNG','KRANKENSTATION','SACHMITTEL',
  'SOZIALLEISTUNGEN','STATISTIKEN','ADMINISTRATION',
]
const BERECHTIGUNGEN = ['LESEN', 'SCHREIBEN', 'ADMINISTRIEREN']

interface ModulBerechtigung { modul: string; berechtigung: string }
interface Rolle { id: number; name: string; beschreibung: string; berechtigungen: ModulBerechtigung[] }

const rollen = ref<Rolle[]>([])
const loading = ref(false)
const error = ref('')
const showForm = ref(false)
const editId = ref<number | null>(null)
const form = ref({ name: '', beschreibung: '', berechtigungen: [] as ModulBerechtigung[] })

function hasPermission(modul: string, berechtigung: string) {
  return form.value.berechtigungen.some(b => b.modul === modul && b.berechtigung === berechtigung)
}

function togglePermission(modul: string, berechtigung: string) {
  const idx = form.value.berechtigungen.findIndex(b => b.modul === modul && b.berechtigung === berechtigung)
  if (idx >= 0) {
    form.value.berechtigungen.splice(idx, 1)
  } else {
    form.value.berechtigungen.push({ modul, berechtigung })
  }
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await api.get('/v1/admin/rollen')
    rollen.value = res.data
  } catch (e: any) {
    error.value = e.response?.data?.detail ?? 'Fehler beim Laden'
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editId.value = null
  form.value = { name: '', beschreibung: '', berechtigungen: [] }
  showForm.value = true
}

function openEdit(r: Rolle) {
  editId.value = r.id
  form.value = { name: r.name, beschreibung: r.beschreibung ?? '', berechtigungen: [...r.berechtigungen] }
  showForm.value = true
}

async function save() {
  try {
    if (editId.value) {
      await api.put(`/v1/admin/rollen/${editId.value}`, form.value)
    } else {
      await api.post('/v1/admin/rollen', form.value)
    }
    showForm.value = false
    await load()
  } catch (e: any) {
    error.value = e.response?.data?.detail ?? 'Speichern fehlgeschlagen'
  }
}

async function remove(id: number) {
  if (!confirm('Rolle wirklich löschen?')) return
  try {
    await api.delete(`/v1/admin/rollen/${id}`)
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
      <h1>Rollenverwaltung</h1>
      <button class="btn-primary" @click="openCreate">+ Neue Rolle</button>
    </div>

    <p v-if="error" class="error-msg">{{ error }}</p>
    <div v-if="loading" class="loading">Lade…</div>

    <div v-else class="rollen-grid">
      <div v-for="r in rollen" :key="r.id" class="rolle-card">
        <div class="rolle-header">
          <strong>{{ r.name }}</strong>
          <div>
            <button class="btn-sm" @click="openEdit(r)">Bearbeiten</button>
            <button class="btn-sm btn-danger" @click="remove(r.id)">Löschen</button>
          </div>
        </div>
        <p v-if="r.beschreibung" class="beschreibung">{{ r.beschreibung }}</p>
        <p class="text-muted" style="font-size:.8rem">
          {{ r.berechtigungen.length }} Berechtigung(en)
        </p>
      </div>
      <p v-if="!rollen.length" class="text-muted">Keine Rollen vorhanden.</p>
    </div>

    <!-- Formular-Modal -->
    <div v-if="showForm" class="modal-backdrop" @click.self="showForm = false">
      <div class="modal-box">
        <h2>{{ editId ? 'Rolle bearbeiten' : 'Neue Rolle' }}</h2>

        <label>Name * <input v-model="form.name" required /></label>
        <label>Beschreibung <textarea v-model="form.beschreibung" rows="2" /></label>

        <h3 style="margin:.75rem 0 .5rem">Berechtigungen</h3>
        <div class="perm-table-wrapper">
          <table class="perm-table">
            <thead>
              <tr>
                <th>Modul</th>
                <th v-for="b in BERECHTIGUNGEN" :key="b">{{ b }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="modul in MODULE_NAMES" :key="modul">
                <td>{{ modul }}</td>
                <td v-for="b in BERECHTIGUNGEN" :key="b" class="text-center">
                  <input
                    type="checkbox"
                    :checked="hasPermission(modul, b)"
                    @change="togglePermission(modul, b)"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>

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
.error-msg { color: #e53e3e; }
.loading, .text-muted { color: #718096; }
.rollen-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1rem; }
.rolle-card { border: 1px solid #e2e8f0; border-radius: 8px; padding: 1rem; }
.rolle-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: .25rem; }
.beschreibung { font-size: .875rem; color: #4a5568; margin: .25rem 0; }
.btn-primary { background: #3182ce; color: #fff; border: none; padding: .4rem .9rem; border-radius: 4px; cursor: pointer; }
.btn-sm { padding: .25rem .6rem; border: 1px solid #cbd5e0; border-radius: 4px; cursor: pointer; margin-right: .25rem; background: #fff; }
.btn-danger { border-color: #fc8181; color: #c53030; }
.text-center { text-align: center; }
.modal-backdrop { position: fixed; inset: 0; background: rgba(0,0,0,.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal-box { background: #fff; border-radius: 8px; padding: 1.5rem; width: 700px; max-height: 90vh; overflow-y: auto; }
.modal-box h2 { margin-top: 0; }
.modal-box label { display: flex; flex-direction: column; margin-bottom: .75rem; font-size: .875rem; font-weight: 500; gap: .25rem; }
.modal-box input[type=text], .modal-box textarea { border: 1px solid #cbd5e0; border-radius: 4px; padding: .35rem .5rem; }
.perm-table-wrapper { overflow-x: auto; max-height: 340px; overflow-y: auto; border: 1px solid #e2e8f0; border-radius: 4px; }
.perm-table { width: 100%; border-collapse: collapse; font-size: .8rem; }
.perm-table th, .perm-table td { padding: .3rem .5rem; border-bottom: 1px solid #e2e8f0; }
.perm-table th { background: #f7fafc; position: sticky; top: 0; }
.modal-actions { display: flex; gap: .75rem; margin-top: 1rem; }
</style>

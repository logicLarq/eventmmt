// Minimal in-browser data store using localStorage
const STORAGE_KEY = 'ces_manager_v1';

function loadState() {
  try { return JSON.parse(localStorage.getItem(STORAGE_KEY)) || { nextId: 1, events: [] }; }
  catch { return { nextId: 1, events: [] }; }
}
function saveState(state) { localStorage.setItem(STORAGE_KEY, JSON.stringify(state)); }

const Store = {
  state: loadState(),
  getEvents() { return this.state.events.slice(); },
  createEvent(name, date, venue, maxParticipants) {
    const id = this.state.nextId++;
    const ev = { id, name, date, venue, maxParticipants: Number(maxParticipants), participants: [] };
    this.state.events.push(ev);
    saveState(this.state); return ev;
  },
  findEventById(id) { return this.state.events.find(e => e.id === Number(id)); },
  registerToEvent(eventId, participant) {
    const ev = this.findEventById(eventId);
    if (!ev) return { ok: false, reason: 'not_found' };
    if (ev.participants.length >= ev.maxParticipants) return { ok: false, reason: 'full' };
    const dup = ev.participants.some(p => p.rollNo.toLowerCase() === participant.rollNo.toLowerCase());
    if (dup) return { ok: false, reason: 'duplicate' };
    ev.participants.push(participant); saveState(this.state); return { ok: true };
  }
};

// UI helpers
function $(sel, root = document) { return root.querySelector(sel); }
function $all(sel, root = document) { return [...root.querySelectorAll(sel)]; }
function toast(msg, kind = 'info') {
  const el = $('#toast');
  el.textContent = msg;
  el.style.background = kind === 'success' ? '#baf7d3' : kind === 'warn' ? '#ffe7b0' : kind === 'error' ? '#ffc1d4' : '#fff';
  el.classList.add('show');
  setTimeout(() => el.classList.remove('show'), 2200);
}

// Tabs
$all('.tab').forEach(btn => btn.addEventListener('click', () => {
  $all('.tab').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  const tab = btn.dataset.tab;
  $all('.tab-panel').forEach(p => p.classList.remove('active'));
  const panel = document.getElementById(`tab-${tab}`);
  if (panel) panel.classList.add('active');
}));

// Create Event
$('#createForm').addEventListener('submit', (e) => {
  e.preventDefault();
  const name = $('#name').value.trim();
  const date = $('#date').value;
  const venue = $('#venue').value.trim();
  const max = Number($('#max').value);
  if (!name) { toast('Name is required', 'warn'); return; }
  if (!date) { toast('Date is required', 'warn'); return; }
  if (!venue) { toast('Venue is required', 'warn'); return; }
  if (!Number.isFinite(max) || max < 1) { toast('Max must be a positive number', 'warn'); return; }
  const ev = Store.createEvent(name, date, venue, max);
  toast(`Created event #${ev.id}: ${ev.name}`, 'success');
  e.target.reset();
  refreshEventsTable();
});

// Events table
function refreshEventsTable() {
  const tbody = $('#eventsTable tbody');
  tbody.innerHTML = '';
  Store.getEvents().forEach(ev => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${ev.id}</td>
      <td>${ev.name}</td>
      <td>${formatDate(ev.date)}</td>
      <td>${ev.venue}</td>
      <td>${ev.participants.length}</td>
      <td>${ev.maxParticipants}</td>
    `;
    tbody.appendChild(tr);
  });
}
$('#refreshEvents').addEventListener('click', refreshEventsTable);

// Register form
$('#registerForm').addEventListener('submit', (e) => {
  e.preventDefault();
  const eventId = Number($('#eventId').value);
  const name = $('#pname').value.trim();
  const roll = $('#proll').value.trim();
  const dept = $('#pdept').value.trim();
  if (!eventId) { toast('Event ID is required', 'warn'); return; }
  if (!name || !roll) { toast('Name and Roll No are required', 'warn'); return; }
  const res = Store.registerToEvent(eventId, { name, rollNo: roll, department: dept });
  if (res.ok) { toast('Registered successfully', 'success'); e.target.reset(); refreshEventsTable(); refreshParticipantsTable(); }
  else if (res.reason === 'not_found') toast('Event not found', 'error');
  else if (res.reason === 'full') toast('Event is full', 'error');
  else if (res.reason === 'duplicate') toast('Duplicate roll number for this event', 'error');
  else toast('Registration failed', 'error');
});

// Participants table
function refreshParticipantsTable() {
  const tbody = $('#participantsTable tbody');
  tbody.innerHTML = '';
  Store.getEvents().forEach(ev => {
    ev.participants.forEach(p => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${ev.id}</td>
        <td>${ev.name}</td>
        <td>${p.name} (Roll: ${p.rollNo}, Dept: ${p.department || '-'} )</td>
      `;
      tbody.appendChild(tr);
    });
  });
}
$('#refreshParticipants').addEventListener('click', refreshParticipantsTable);

function formatDate(d) {
  if (!d) return '';
  try {
    const dt = new Date(d);
    if (isNaN(dt)) return d; // already formatted
    return dt.toLocaleDateString();
  } catch { return d; }
}

// Initial paint
refreshEventsTable();
refreshParticipantsTable();

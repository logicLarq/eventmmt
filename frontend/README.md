# College Event Management – Frontend

A minimal, colorful, responsive web UI that mirrors the Java Swing app features using in-browser storage (localStorage).

Features
- Create events (id, name, date, venue, max participants)
- List events with registered counts
- Register participants by event id
- View all participants across events

Tech
- HTML + CSS + JavaScript only (no build tools)
- Persistence via `localStorage` (per-browser)

Quick Start
- Open `frontend/index.html` directly in your browser, or use VS Code Live Server.

Windows PowerShell (optional Live Server with VS Code extension):
```powershell
# If using VS Code, install "Live Server" extension (ritwickdey.LiveServer)
# Then right-click index.html > Open with Live Server
```

Notes
- This frontend does not connect to the Java code; it is a standalone client-side implementation for quick demo/testing.
- Data is stored in `localStorage` key `ces_manager_v1`. To reset, clear site data in your browser DevTools or run:
  - Open DevTools Console and run: `localStorage.removeItem('ces_manager_v1')`.

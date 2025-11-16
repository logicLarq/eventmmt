# College Event Management

[![Netlify Status](https://api.netlify.com/api/v1/badges/363ebdd7-1ef2-496c-b9dc-9b543ec8ab6a/deploy-status)](https://app.netlify.com/sites/eventmmt/deploys)

Live Demo: https://eventmmt.netlify.app

A minimal, colorful, responsive frontend (SPA) for managing college events and registrations. Mirrors the desktop Java Swing app’s core workflows using in-browser storage.

## Features
- Create events (name, date, venue, max participants)
- View events with registered vs max counts
- Register participants by event ID (duplicate and capacity checks)
- List all participants across events

## Quick Start (Frontend)
- Open `frontend/index.html` directly in your browser, or
- Use VS Code “Live Server” for auto-reload.

## Local Java Desktop App (optional)
```powershell
Push-Location "C:\Users\lsing\Desktop\pranjali\CollegeEventManagementSystemProject"
javac -d . *.java
java CollegeEventSystem.MainGUI
Pop-Location
```

## Deploy (Netlify CLI)
```powershell
# One-time (already done):
netlify login

# Deploy from project root
netlify deploy --dir frontend --prod
```

## Project Structure
- `frontend/` — Single Page App (HTML/CSS/JS, localStorage persistence)
- `Event.java`, `Participant.java`, `EventManager.java`, `MainGUI.java` — Swing desktop app
- `netlify.toml` — Netlify configuration (publish `frontend`)

## Notes
- The web frontend is standalone and stores data per browser via `localStorage`.
- Reset frontend data: open DevTools Console and run `localStorage.removeItem('ces_manager_v1')`.

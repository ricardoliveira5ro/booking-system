# Web

Built with Next.js (App Router), TypeScript, and TailwindCSS, and hosted on Vercel.

### 🔌 Tech Stack and Packages

- Next.js v15
- TypeScript
- TailwindCSS
- @tanstack/react-query (State & Data Fetching)
- AOS (Animate On Scroll)
- lucide-react (icons)
- next-intl (Internationalization)
- react-toastify (Notifications)
- uuid

### 📁 Project Structure

```

📁 web/
├── 📂 public/      
│   └── 📂 locales/
│       ├── 📄 en.json
│       └── 📄 pt.json
├── 📂 src/            
│   ├── 📂 app/             # App router pages and layouts
│   │   ├── 📂 (routes)     
│   │   ├── 📄 layout.tsx   
│   │   └── 📄 page.tsx     
│   ├── 📂 components       # Reusable UI components
│   ├── 📂 hooks            # Custom React hooks
│   ├── 📂 i18n             # Localization setup using next-intl
│   ├── 📂 models           # TypeScript models
│   ├── 📂 utils            # Utility and helper functions
│   └── 📄 middleware.ts    
├── 📄 tailwind.config.ts  
├── 📄 tsconfig.json       
├── 📄 package.json
└── ...
```

### 🌐 Localization (i18n)

- The app uses next-intl for internationalization and message management.
- A custom middleware detects the browser’s `Accept-Language` header (`pt-PT`, `en-US`) and saves the preferred language (`pt` or `en`) into a `NEXT_LOCALE` cookie.
- On every request, the app reads that cookie to determine which language messages to load.
- Translation files are stored in /public/locales (`en.json`, `pt.json`).
- The app defaults to English.
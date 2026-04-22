# Bidrag til Wishlist

Tak fordi du vil bidrage til projektet! Følg retningslinjerne herunder for at sikre et ensartet og velfungerende samarbejde.

## Workflow

1. **Fork** repository (eksternt) eller opret en branch fra `main`
2. **Opret en branch** med et beskrivende navn (se branching-strategi nedenfor)
3. **Lav dine ændringer**
4. **Commit** med en tydelig besked
5. **Push** din branch
6. **Opret en Pull Request** mod `main`

## Branching-strategi

| Branch       | Formål                              |
|--------------|-------------------------------------|
| `main`       | Stabil, kørende kode — deploy-klar  |
| `feature/*`  | Nye features (f.eks. `feature/reserve-wish`) |
| `fix/*`      | Bugfixes (f.eks. `fix/login-session`) |

Eksempel på at oprette en branch:

```bash
git checkout -b feature/mit-feature
```

## Commit-beskeder

Skriv korte og præcise commit-beskeder på engelsk eller dansk:

```
Tilføj redigering af ønsker
Fix: session nulstilles ikke ved logout
Add wish deletion endpoint
```

Undgå uklare beskeder som `fix`, `update` eller `ændringer`.

## Kodestandarder

- Følg standard Java-konventioner (camelCase for metoder og variabler, PascalCase for klasser)
- Brug meningsfulde navne — undgå forkortelser
- Ingen hardcodede værdier; brug konfiguration eller konstanter
- Hold metoder korte og med ét ansvar
- Skriv kommentarer kun hvor logikken ikke er selvforklarende

## Pull Requests

- Beskriv tydeligt hvad ændringen gør og hvorfor
- Link til det relevante issue, hvis det findes (f.eks. `Closes #12`)
- Sørg for at applikationen stadig kører efter dine ændringer
- Få mindst ét review fra et andet gruppemedlem før merge

## Tests

- Tilføj tests til ny funktionalitet, hvor det er muligt
- Kør eksisterende tests inden du opretter en Pull Request:

```bash
mvn test
```

## Spørgsmål

Har du spørgsmål, så opret et issue i repository'et eller kontakt gruppen direkte.

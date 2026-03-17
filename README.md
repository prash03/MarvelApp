# Marvel Characters App (Jetpack Compose)

Android application that displays Marvel characters using the Marvel Developer API.

## Tech Stack

- Kotlin

- Jetpack Compose
- MVVM Architecture
- Retrofit
- Coroutines + Flow
- Navigation Compose
- Coil
- Character list screen
- Pagination using LazyColumn
- Character detail screen
- Image loading with Coil
- Clean architecture structure

## Setup

1. Get API key from:
https://developer.marvel.com/

2. Update:

CharacterRepository.kt

Replace:

YOUR_API_KEY
YOUR_HASH

Marvel API authentication requires:

ts + privateKey + publicKey → MD5 hash

## Architecture

MVVM with layered architecture:

UI (Compose)
↓
ViewModel
↓
Repository
↓
Retrofit API

## Pagination

Pagination implemented manually using:

LazyListState + offset

When user reaches last item → next page loads.

## Future Improvements

- Paging3
- Error handling
- Loading states
- Full detail screen with comics, series, events

## Author

Prashant Kumar

<img width="1080" height="2340" alt="Screenshot_1773739166" src="https://github.com/user-attachments/assets/01483bb8-caba-49ef-a151-f17a8aeba166" />
<img width="1080" height="2340" alt="Screenshot_1773739176" src="https://github.com/user-attachments/assets/be5f04ed-ca89-4673-9313-0ff27b038d95" />
<img width="1080" height="2340" alt="Screenshot_1773739181" src="https://github.com/user-attachments/assets/87942ca6-120b-44b8-a6d4-6f70be181113" />






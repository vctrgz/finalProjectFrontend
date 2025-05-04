# HealHub 

Is designed to help nurses easily view, add, and update patient data in a simple and clear interface, supporting better decision-making and healthcare service.

---

## Main Features

### Login System

* Simple nurse login screen to access the system

### Room List

* Lists all rooms with patient names and diagnosis status
* Tap on a room to access patient-specific data

### Patient Menu

* Divided into three sections:

  * Personal Data
  * Medical Data
  * Care Data

### Personal Data

* View patient name, birth date, address, and language
* Default read-only view with "Edit" mode toggle
* Editable form with save and cancel options

### Medical Data

* Manage if the patient has:

  * Urinary catheter
  * Venous catheter
  * Oxygen therapy

### Care Data

* Add nursing care records including:

  * Vital signs (blood pressure, pulse, temperature, etc.)
  * Diet, hygiene, posture, drainage, and shift notes
* All care records listed clearly
* Visual red highlight for abnormal vital values

### Chart View (Optional / In Progress)

* Display vitals as line chart per patient

---

## UI

* All screens use:

  * Green square buttons (Save, Back, Edit)
  * Consistent spacing and layout

---

## Tech Stack

* Jetpack Compose (Kotlin)
* Room Database (local data)
* Material 3 components

---

## Folder Structure

* `ui/` – All composable screens
* `database/` – Entities, DAOs and AppDatabase
* `theme/` – Color and style definitions

---

## Made by

* With team: Víctor González, Albert Salom, Jiajiao Xu

Thanks for visiting.

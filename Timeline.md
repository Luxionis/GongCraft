# GongCraft — Development Timeline

> Dokumentasi progres pengembangan. Diperbarui otomatis oleh AI Agent setiap ada perubahan.

## Ringkasan Status
- Fase saat ini: 🚧 Setup JavaFX+Spring Boot Desktop Skeleton
- Progress keseluruhan: 5%
- Terakhir diperbarui: 2026-07-07 00:00

---

## [2026-07-07] Initial Desktop Project Skeleton (JavaFX + embedded Spring Boot)

**Layer:** GUI / Config
**Status:** ✅ Selesai

**Apa yang dikerjakan:**
- Membuat project `gongcraft-desktop/` (Maven) dengan `pom.xml` (mengikuti tech stack panduan).
- Membuat entrypoint `GongCraftApplication` yang meng-embed Spring Boot dan men-launch JavaFX.
- Membuat `MainWindow` + panel-panel placeholder (Dashboard, Customers, Products, Orders, Production, Payments, Reports, Settings).
- Menambahkan `application.properties` untuk koneksi MySQL (dev).

**File yang dibuat/diubah:**
- `gongcraft-desktop/pom.xml`
- `gongcraft-desktop/src/main/java/com/gongcraft/GongCraftApplication.java`
- `gongcraft-desktop/src/main/java/com/gongcraft/ui/MainWindow.java`
- `gongcraft-desktop/src/main/java/com/gongcraft/ui/panels/*Panel.java` (placeholder)
- `gongcraft-desktop/src/main/resources/application.properties`

**Keputusan teknis (jika ada):**
- Menggunakan approach embedded Spring context dengan dependency injection untuk `MainWindow` dan panel-panel.

**Belum selesai / TODO berikutnya:**
- Implement database entities & repository sesuai `sql/01_GongCraft_Database_Schema.sql`.
- Implement service layer untuk modul inti (auth, customers, products, orders, production tracking, dll).
- Replace panel placeholder menjadi UI yang terhubung ke service (dengan threading JavaFX via `Task`).


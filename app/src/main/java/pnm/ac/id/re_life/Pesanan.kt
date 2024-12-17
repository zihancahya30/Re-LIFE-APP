package pnm.ac.id.re_life.model

data class Pesanan(
    val pesananId: String? = null,       // Pesanan ID bisa null jika belum ada
    val userId: String? = null,          // User ID bisa null
    val status: String? = null,          // Status bisa null
    val namaLengkap: String? = null,     // Nama Lengkap bisa null
    val nomorTelepon: String? = null,    // Nomor Telepon bisa null
    val namaPetugas: String? = null,    // Nama Petugas bisa null
    val nomorPetugas: String? = null,    // Nomor Telepon Petugas bisa null
    val alamatLengkap: String? = null,   // Alamat Lengkap bisa null
    val detailAlamat: String? = null,    // Detail Alamat bisa null
    val beratSampahDaurUlang: String? = null,  // Berat Sampah Daur Ulang bisa null
    val beratSampahNonDaurUlang: String? = null // Berat Sampah Non-Daur Ulang bisa null
)

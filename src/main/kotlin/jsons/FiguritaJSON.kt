package jsons

import figuritas.NivelImpresion

data class FiguritaJSON(
    val nro: Int,
    val idJugador: Int,
    val onFire: Boolean,
    val nivelImpresion: NivelImpresion,
    val url: String
)
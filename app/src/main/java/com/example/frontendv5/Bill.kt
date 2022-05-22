package com.example.frontendv5

/**
 * This is a data class which is used to create a bill model to retrieve object from API request
 * Parameters used to create is:
 * Integer idFactura
 * String name (of the user)
 * String address (of the user)
 * Float suma (total of the bill)
 */
data class Bill(val idFactura: Integer,
                val nume: String,
                val adresa: String,
                val suma: Float
)

package com.example.meditrack.feature.domain.utils

sealed class OrderType {
    object Ascending:OrderType()
    object Descending:OrderType()
}
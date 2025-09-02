package com.example.meditrack.feature.domain.utils

sealed class MedicineOrder(val orderType: OrderType) {
    class Date(orderType: OrderType) : MedicineOrder(orderType)
    class Time(orderType: OrderType) : MedicineOrder(orderType)

    //descending -> high-low // ascending -> low-high
    class Priority(orderType: OrderType) : MedicineOrder(orderType)

    //class PriorityForToday(orderType: OrderType):TaskOrder(orderType)


    class Default(orderType: OrderType) : MedicineOrder(orderType)

    // descending // ascending // pended
    class Today(orderType: OrderType, val pendedHighPriority: Boolean = true) :
        MedicineOrder(orderType)


    //class All(orderType: OrderType):TaskOrder(orderType)


    fun copy(orderType: OrderType): MedicineOrder {
        return when (this) {
            is Time -> Time(orderType)
            is Date -> Date(orderType)
            is Today -> Today(orderType, pendedHighPriority)
            is Priority -> Priority(orderType)
            is Default -> Default(orderType)
        }
    }


}
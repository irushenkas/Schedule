package com.example.schedule.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object BooleanArg: ReadWriteProperty<Bundle, Boolean?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Boolean?) {
        value?.let { thisRef.putBoolean(property.name, it) }
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Boolean? =
        thisRef.getBoolean(property.name)
}
package com.example.task.infra.memory

import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object LoginCounter {

    private val counter = ConcurrentHashMap<LocalDate, AtomicInteger>()

    fun increase() {
        counter
            .computeIfAbsent(LocalDate.now()) { AtomicInteger(0) }
            .incrementAndGet()
    }

    fun getTodayCount(): Int =
        counter[LocalDate.now()]?.get() ?: 0
}

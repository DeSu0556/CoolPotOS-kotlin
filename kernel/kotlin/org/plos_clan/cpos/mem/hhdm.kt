@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.plos_clan.cpos.mem

import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toCPointer
import natives.hhdm_request

object Hhdm {
    var offset: ULong = 0u
        private set

    val isReady: Boolean
        get() = offset != 0uL

    fun initialize(): ULong? {
        val response = hhdm_request.response?.pointed ?: return null
        offset = response.offset
        println("HHDM offset: 0x${offset.toHex64()}")
        return offset
    }

    fun toVirtual(physicalAddress: ULong): ULong = physicalAddress + offset

    fun toPhysical(virtualAddress: ULong): ULong = virtualAddress - offset

    fun <T : CPointed> toVirtualPointer(physicalAddress: ULong): CPointer<T>? =
        toVirtual(physicalAddress).toPointer()

    fun <T : CPointed> toPhysicalPointer(virtualAddress: ULong): CPointer<T>? =
        toPhysical(virtualAddress).toPointer()

    fun <T : CPointed> toVirtualPointer(pointer: CPointer<T>): CPointer<T>? =
        toVirtual(pointer.toAddress()).toPointer()

    fun <T : CPointed> toPhysicalPointer(pointer: CPointer<T>): CPointer<T>? =
        toPhysical(pointer.toAddress()).toPointer()
}

fun ULong.toVirtualAddress(): ULong = Hhdm.toVirtual(this)

fun ULong.toPhysicalAddress(): ULong = Hhdm.toPhysical(this)

fun <T : CPointed> ULong.toVirtualPointer(): CPointer<T>? = Hhdm.toVirtualPointer(this)

fun <T : CPointed> ULong.toPhysicalPointer(): CPointer<T>? = Hhdm.toPhysicalPointer(this)

fun <T : CPointed> CPointer<T>.toVirtualPointer(): CPointer<T>? = Hhdm.toVirtualPointer(this)

fun <T : CPointed> CPointer<T>.toPhysicalPointer(): CPointer<T>? = Hhdm.toPhysicalPointer(this)

private fun <T : CPointed> CPointer<T>.toAddress(): ULong = rawValue.toLong().toULong()

private fun <T : CPointed> ULong.toPointer(): CPointer<T>? = toLong().toCPointer()

private fun ULong.toHex64(): String = toString(16).padStart(16, '0')

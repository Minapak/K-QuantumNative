/**
 * K-QuantumNative - Quantum Computing Models
 * Port of QuantumNative/Models/QuantumCircuit.swift & QubitState.swift
 * Based on SwiftQuantum Core
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.*

/**
 * Complex number representation for quantum amplitudes
 */
@Serializable
data class Complex(
    val real: Double = 0.0,
    val imaginary: Double = 0.0
) {
    val magnitude: Double
        get() = sqrt(real * real + imaginary * imaginary)

    val phase: Double
        get() = atan2(imaginary, real)

    val conjugate: Complex
        get() = Complex(real, -imaginary)

    operator fun plus(other: Complex) = Complex(real + other.real, imaginary + other.imaginary)
    operator fun minus(other: Complex) = Complex(real - other.real, imaginary - other.imaginary)
    operator fun times(other: Complex) = Complex(
        real * other.real - imaginary * other.imaginary,
        real * other.imaginary + imaginary * other.real
    )
    operator fun times(scalar: Double) = Complex(real * scalar, imaginary * scalar)

    override fun toString(): String {
        return when {
            imaginary == 0.0 -> String.format("%.4f", real)
            real == 0.0 -> String.format("%.4fi", imaginary)
            imaginary < 0 -> String.format("%.4f - %.4fi", real, -imaginary)
            else -> String.format("%.4f + %.4fi", real, imaginary)
        }
    }

    companion object {
        val ZERO = Complex(0.0, 0.0)
        val ONE = Complex(1.0, 0.0)
        val I = Complex(0.0, 1.0)

        fun fromPolar(magnitude: Double, phase: Double) =
            Complex(magnitude * cos(phase), magnitude * sin(phase))
    }
}

/**
 * Qubit state representation |ψ⟩ = α|0⟩ + β|1⟩
 */
@Serializable
data class QubitState(
    var alpha: Complex = Complex(1.0, 0.0),
    var beta: Complex = Complex(0.0, 0.0)
) {
    val prob0: Double get() = alpha.magnitude.pow(2)
    val prob1: Double get() = beta.magnitude.pow(2)

    fun reset() {
        alpha = Complex(1.0, 0.0)
        beta = Complex(0.0, 0.0)
    }

    fun applyHadamard() {
        val sqrt2Inv = 1.0 / sqrt(2.0)
        val newAlpha = (alpha + beta) * sqrt2Inv
        val newBeta = (alpha - beta) * sqrt2Inv
        alpha = newAlpha
        beta = newBeta
    }

    fun applyPauliX() {
        val temp = alpha
        alpha = beta
        beta = temp
    }

    fun applyPauliY() {
        val newAlpha = beta * Complex(0.0, 1.0)
        val newBeta = alpha * Complex(0.0, -1.0)
        alpha = newAlpha
        beta = newBeta
    }

    fun applyPauliZ() {
        beta = beta * Complex(-1.0, 0.0)
    }

    fun applyPhaseS() {
        beta = beta * Complex.I
    }

    fun applyTGate() {
        val phase = PI / 4
        beta = beta * Complex.fromPolar(1.0, phase)
    }

    // Bloch sphere coordinates
    fun blochX(): Double = 2 * (alpha.conjugate * beta).real
    fun blochY(): Double = 2 * (alpha.conjugate * beta).imaginary
    fun blochZ(): Double = prob0 - prob1

    fun copy(): QubitState = QubitState(alpha, beta)
}

/**
 * Quantum gate types
 */
@Serializable
enum class QuantumGateType {
    @SerialName("hadamard")
    HADAMARD,
    @SerialName("pauli_x")
    PAULI_X,
    @SerialName("pauli_y")
    PAULI_Y,
    @SerialName("pauli_z")
    PAULI_Z,
    @SerialName("phase")
    PHASE,
    @SerialName("t_gate")
    T_GATE,
    @SerialName("cnot")
    CNOT,
    @SerialName("swap")
    SWAP,
    @SerialName("toffoli")
    TOFFOLI,
    @SerialName("measure")
    MEASURE,
    @SerialName("rx")
    ROTATION_X,
    @SerialName("ry")
    ROTATION_Y,
    @SerialName("rz")
    ROTATION_Z;

    val displayName: String
        get() = when (this) {
            HADAMARD -> "H"
            PAULI_X -> "X"
            PAULI_Y -> "Y"
            PAULI_Z -> "Z"
            PHASE -> "S"
            T_GATE -> "T"
            CNOT -> "CNOT"
            SWAP -> "SWAP"
            TOFFOLI -> "CCX"
            MEASURE -> "M"
            ROTATION_X -> "Rx"
            ROTATION_Y -> "Ry"
            ROTATION_Z -> "Rz"
        }

    val isSingleQubit: Boolean
        get() = this in listOf(HADAMARD, PAULI_X, PAULI_Y, PAULI_Z, PHASE, T_GATE, ROTATION_X, ROTATION_Y, ROTATION_Z, MEASURE)

    val color: Color
        get() = when (this) {
            HADAMARD -> Color(0xFF2196F3)
            PAULI_X -> Color(0xFFF44336)
            PAULI_Y -> Color(0xFF4CAF50)
            PAULI_Z -> Color(0xFFFF9800)
            PHASE -> Color(0xFF9C27B0)
            T_GATE -> Color(0xFF00BCD4)
            CNOT -> Color(0xFF3F51B5)
            SWAP -> Color(0xFFE91E63)
            TOFFOLI -> Color(0xFF795548)
            MEASURE -> Color(0xFF607D8B)
            ROTATION_X -> Color(0xFFFF5722)
            ROTATION_Y -> Color(0xFF8BC34A)
            ROTATION_Z -> Color(0xFFFFEB3B)
        }
}

/**
 * Quantum gate operation
 */
@Serializable
data class QuantumGate(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: QuantumGateType,
    val targetQubit: Int,
    val controlQubit: Int? = null,
    val controlQubit2: Int? = null,
    val parameter: Double? = null, // For rotation gates
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Noise model for realistic quantum simulation
 * Based on Harvard-MIT 2025 research
 */
@Serializable
data class NoiseModel(
    val dephasingRate: Double = 0.001,
    val relaxationRate: Double = 0.0005,
    val gateErrorRate: Double = 0.005,
    val measurementError: Double = 0.002,
    val atomLossRate: Double = 0.0001,
    val continuousOperationCorrection: Boolean = true
) {
    companion object {
        val IDEAL = NoiseModel(0.0, 0.0, 0.0, 0.0, 0.0, false)

        val HARVARD_MIT_2025 = NoiseModel(
            dephasingRate = 0.0005,
            relaxationRate = 0.0003,
            gateErrorRate = 0.003,
            measurementError = 0.001,
            atomLossRate = 0.0001,
            continuousOperationCorrection = true
        )

        val NISQ_REALISTIC = NoiseModel(
            dephasingRate = 0.005,
            relaxationRate = 0.002,
            gateErrorRate = 0.02,
            measurementError = 0.01,
            atomLossRate = 0.001,
            continuousOperationCorrection = false
        )
    }

    fun calculateFidelity(circuitDepth: Int): Double {
        val gateError = 1.0 - (1.0 - gateErrorRate).pow(circuitDepth)
        val dephasingError = 1.0 - exp(-circuitDepth * dephasingRate)
        val relaxationError = 1.0 - exp(-circuitDepth * relaxationRate)

        val totalError = gateError + dephasingError + relaxationError
        val rawFidelity = max(0.0, 1.0 - totalError)

        return if (continuousOperationCorrection) {
            // Surface code error correction improvement
            val correctedError = totalError.pow(3.0) // Cubic suppression
            max(0.0, 1.0 - correctedError)
        } else {
            rawFidelity
        }
    }
}

/**
 * Quantum circuit representation
 */
@Serializable
data class QuantumCircuit(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Untitled Circuit",
    val numberOfQubits: Int = 2,
    val gates: List<QuantumGate> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    val depth: Int
        get() = gates.size

    fun toQASM(): String {
        val sb = StringBuilder()
        sb.appendLine("OPENQASM 2.0;")
        sb.appendLine("include \"qelib1.inc\";")
        sb.appendLine()
        sb.appendLine("qreg q[$numberOfQubits];")
        sb.appendLine("creg c[$numberOfQubits];")
        sb.appendLine()

        for (gate in gates) {
            when (gate.type) {
                QuantumGateType.HADAMARD -> sb.appendLine("h q[${gate.targetQubit}];")
                QuantumGateType.PAULI_X -> sb.appendLine("x q[${gate.targetQubit}];")
                QuantumGateType.PAULI_Y -> sb.appendLine("y q[${gate.targetQubit}];")
                QuantumGateType.PAULI_Z -> sb.appendLine("z q[${gate.targetQubit}];")
                QuantumGateType.PHASE -> sb.appendLine("s q[${gate.targetQubit}];")
                QuantumGateType.T_GATE -> sb.appendLine("t q[${gate.targetQubit}];")
                QuantumGateType.CNOT -> sb.appendLine("cx q[${gate.controlQubit}], q[${gate.targetQubit}];")
                QuantumGateType.SWAP -> sb.appendLine("swap q[${gate.controlQubit}], q[${gate.targetQubit}];")
                QuantumGateType.TOFFOLI -> sb.appendLine("ccx q[${gate.controlQubit}], q[${gate.controlQubit2}], q[${gate.targetQubit}];")
                QuantumGateType.ROTATION_X -> sb.appendLine("rx(${gate.parameter}) q[${gate.targetQubit}];")
                QuantumGateType.ROTATION_Y -> sb.appendLine("ry(${gate.parameter}) q[${gate.targetQubit}];")
                QuantumGateType.ROTATION_Z -> sb.appendLine("rz(${gate.parameter}) q[${gate.targetQubit}];")
                QuantumGateType.MEASURE -> sb.appendLine("measure q[${gate.targetQubit}] -> c[${gate.targetQubit}];")
            }
        }

        return sb.toString()
    }
}

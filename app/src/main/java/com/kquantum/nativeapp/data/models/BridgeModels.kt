/**
 * K-QuantumNative - QuantumBridge Models
 * Port of QuantumNative/Services/QuantumBridgeService.swift models
 * Integration with IBM Quantum hardware via QuantumBridge
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Bridge job status
 */
@Serializable
enum class BridgeJobStatus {
    @SerialName("queued")
    QUEUED,
    @SerialName("validating")
    VALIDATING,
    @SerialName("running")
    RUNNING,
    @SerialName("completed")
    COMPLETED,
    @SerialName("failed")
    FAILED,
    @SerialName("cancelled")
    CANCELLED;

    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }

    val isTerminal: Boolean
        get() = this in listOf(COMPLETED, FAILED, CANCELLED)
}

/**
 * Bridge job representing a quantum circuit execution
 */
@Serializable
data class BridgeJob(
    val id: String,
    @SerialName("circuit_data")
    val circuitData: String? = null,
    val status: BridgeJobStatus = BridgeJobStatus.QUEUED,
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @SerialName("started_at")
    val startedAt: Long? = null,
    @SerialName("completed_at")
    val completedAt: Long? = null,
    val results: BridgeJobResults? = null,
    val error: String? = null,
    @SerialName("estimated_time")
    val estimatedTime: Int? = null, // seconds
    @SerialName("queue_position")
    val queuePosition: Int? = null
)

/**
 * Results from a completed bridge job
 */
@Serializable
data class BridgeJobResults(
    val measurements: Map<String, Int> = emptyMap(),
    @SerialName("final_state_vector")
    val finalStateVector: List<ComplexNumber>? = null,
    val fidelity: Double = 1.0,
    @SerialName("execution_time_ms")
    val executionTimeMs: Long = 0,
    @SerialName("noise_events")
    val noiseEvents: List<NoiseEventData> = emptyList(),
    @SerialName("atom_replenishments")
    val atomReplenishments: Int = 0,
    @SerialName("coherence_time_seconds")
    val coherenceTimeSeconds: Double = 0.0
) {
    val mostLikelyOutcome: String?
        get() = measurements.maxByOrNull { it.value }?.key

    val probabilities: Map<String, Double>
        get() {
            val total = measurements.values.sum().toDouble()
            return if (total > 0) measurements.mapValues { it.value / total }
            else emptyMap()
        }
}

@Serializable
data class ComplexNumber(
    val real: Double,
    val imaginary: Double
) {
    fun toComplex() = Complex(real, imaginary)
}

@Serializable
data class NoiseEventData(
    val timestamp: Long,
    val qubit: Int,
    val type: String,
    val magnitude: Double
)

/**
 * Real-time noise monitoring data
 */
@Serializable
data class RealTimeNoiseData(
    val timestamp: Long = System.currentTimeMillis(),
    @SerialName("qubit_noise_map")
    val qubitNoiseMap: Map<String, QubitNoiseLevel> = emptyMap(),
    @SerialName("overall_fidelity")
    val overallFidelity: Double = 1.0,
    @SerialName("coherence_remaining")
    val coherenceRemaining: Double = 1.0,
    @SerialName("atom_loss_rate")
    val atomLossRate: Double = 0.0,
    @SerialName("replenishment_rate")
    val replenishmentRate: Double = 0.0
)

@Serializable
data class QubitNoiseLevel(
    val dephasing: Double = 0.0,
    val relaxation: Double = 0.0,
    @SerialName("gate_error")
    val gateError: Double = 0.0,
    val status: String = "healthy" // healthy, warning, critical
)

/**
 * Hardware specifications (Harvard-MIT 2025/2026)
 */
data class HardwareSpecs(
    val maxQubits: Int = 3000,
    val continuousOperationHours: Double = 2.0,
    val faultTolerantLogicalQubits: Int = 96,
    val atomReplenishmentLatencyMs: Double = 50.0,
    val averageFidelity: Double = 0.9985
)

/**
 * Execution tier with limits
 */
enum class ExecutionTier(
    val displayName: String,
    val maxQubits: Int,
    val monthlyCredits: Int,
    val features: List<String>
) {
    FREE(
        displayName = "Free",
        maxQubits = 8,
        monthlyCredits = 10,
        features = listOf("Basic simulations", "Limited circuit depth")
    ),
    PRO(
        displayName = "Pro",
        maxQubits = 64,
        monthlyCredits = 100,
        features = listOf(
            "64 qubit simulations",
            "Continuous operation",
            "Advanced noise visualization"
        )
    ),
    PREMIUM(
        displayName = "Premium",
        maxQubits = 256,
        monthlyCredits = 1000,
        features = listOf(
            "256 qubit simulations",
            "Fault-tolerant computing",
            "Priority queue",
            "Unlimited error correction"
        )
    )
}

/**
 * Premium feature flags
 */
enum class PremiumFeature {
    CONTINUOUS_OPERATION,
    FAULT_TOLERANT,
    UNLIMITED_ERROR_CORRECTION,
    PRIORITY_QUEUE,
    ADVANCED_NOISE_VISUALIZATION,
    CAREER_PASSPORT;

    fun requiredTier(): ExecutionTier = when (this) {
        CONTINUOUS_OPERATION, ADVANCED_NOISE_VISUALIZATION -> ExecutionTier.PRO
        else -> ExecutionTier.PREMIUM
    }
}

/**
 * Circuit submission request
 */
@Serializable
data class CircuitSubmissionRequest(
    @SerialName("circuit_qasm")
    val circuitQasm: String,
    @SerialName("num_shots")
    val numShots: Int = 1000,
    @SerialName("noise_model")
    val noiseModel: String = "harvard_mit_2025",
    @SerialName("error_mitigation")
    val errorMitigation: Boolean = true
)

/**
 * Hardware status
 */
@Serializable
data class HardwareStatus(
    @SerialName("is_online")
    val isOnline: Boolean = true,
    @SerialName("queue_length")
    val queueLength: Int = 0,
    @SerialName("estimated_wait_seconds")
    val estimatedWaitSeconds: Int = 0,
    @SerialName("current_fidelity")
    val currentFidelity: Double = 0.998,
    @SerialName("maintenance_scheduled")
    val maintenanceScheduled: String? = null
)

/**
 * Quantum backend information
 */
@Serializable
data class QuantumBackend(
    val name: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("num_qubits")
    val numQubits: Int,
    val status: String = "online",
    @SerialName("queue_length")
    val queueLength: Int = 0,
    val description: String? = null
)

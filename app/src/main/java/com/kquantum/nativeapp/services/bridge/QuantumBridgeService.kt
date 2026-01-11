/**
 * K-QuantumNative - QuantumBridge Service
 * Port of QuantumNative/Services/QuantumBridgeService.swift
 * Integration with IBM Quantum hardware via QuantumBridge API
 * Copyright (c) 2025 Eunmin Park. All rights reserved.
 */
package com.kquantum.nativeapp.services.bridge

import com.kquantum.nativeapp.BuildConfig
import com.kquantum.nativeapp.data.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class QuantumBridgeService @Inject constructor() {

    companion object {
        private val HARDWARE_SPECS = HardwareSpecs()
        private const val NOISE_MONITORING_INTERVAL_MS = 500L
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private var noiseMonitoringJob: Job? = null

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _currentJob = MutableStateFlow<BridgeJob?>(null)
    val currentJob: StateFlow<BridgeJob?> = _currentJob.asStateFlow()

    private val _jobHistory = MutableStateFlow<List<BridgeJob>>(emptyList())
    val jobHistory: StateFlow<List<BridgeJob>> = _jobHistory.asStateFlow()

    private val _realTimeNoiseData = MutableStateFlow<RealTimeNoiseData?>(null)
    val realTimeNoiseData: StateFlow<RealTimeNoiseData?> = _realTimeNoiseData.asStateFlow()

    // Alias for noiseData (used by BridgeViewModel)
    val noiseData: StateFlow<RealTimeNoiseData?> = _realTimeNoiseData.asStateFlow()

    private val _availableBackends = MutableStateFlow<List<QuantumBackend>>(emptyList())
    val availableBackends: StateFlow<List<QuantumBackend>> = _availableBackends.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentTier = MutableStateFlow(ExecutionTier.FREE)
    val currentTier: StateFlow<ExecutionTier> = _currentTier.asStateFlow()

    private val _remainingCredits = MutableStateFlow(10)
    val remainingCredits: StateFlow<Int> = _remainingCredits.asStateFlow()

    private val _hardwareStatus = MutableStateFlow<HardwareStatus?>(null)
    val hardwareStatus: StateFlow<HardwareStatus?> = _hardwareStatus.asStateFlow()

    val hardwareSpecs: HardwareSpecs = HARDWARE_SPECS

    fun setTier(tier: ExecutionTier) {
        _currentTier.value = tier
        _remainingCredits.value = tier.monthlyCredits
    }

    suspend fun connect(apiKey: String? = null): Boolean {
        if (_isConnected.value) return true

        _isLoading.value = true
        _error.value = null

        return try {
            // In production, connect to actual WebSocket
            // For now, simulate connection
            delay(500)
            _isConnected.value = true
            checkHardwareStatus()
            true
        } catch (e: Exception) {
            _error.value = "Failed to connect: ${e.message}"
            false
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun loadAvailableBackends() {
        _isLoading.value = true
        try {
            // Simulate loading backends
            delay(300)
            _availableBackends.value = listOf(
                QuantumBackend(
                    name = "ibm_brisbane",
                    displayName = "IBM Brisbane",
                    numQubits = 127,
                    status = "online",
                    queueLength = 5
                ),
                QuantumBackend(
                    name = "ibm_osaka",
                    displayName = "IBM Osaka",
                    numQubits = 127,
                    status = "online",
                    queueLength = 12
                ),
                QuantumBackend(
                    name = "ibm_kyoto",
                    displayName = "IBM Kyoto",
                    numQubits = 127,
                    status = "maintenance",
                    queueLength = 0
                ),
                QuantumBackend(
                    name = "simulator",
                    displayName = "Quantum Simulator",
                    numQubits = 32,
                    status = "online",
                    queueLength = 0
                )
            )
        } catch (e: Exception) {
            _error.value = "Failed to load backends: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun submitJob(circuit: String, backend: String, shots: Int): BridgeJob? {
        if (!_isConnected.value) {
            _error.value = "Not connected to QuantumBridge"
            return null
        }

        _isLoading.value = true

        return try {
            val job = BridgeJob(
                id = "qb-${System.currentTimeMillis()}-${kotlin.random.Random.nextInt(1000)}",
                circuitData = circuit,
                status = BridgeJobStatus.QUEUED,
                estimatedTime = 5 + kotlin.random.Random.nextInt(10),
                queuePosition = kotlin.random.Random.nextInt(1, 10)
            )

            _currentJob.value = job
            _remainingCredits.value = _remainingCredits.value - 1

            // Simulate job execution in background
            scope.launch {
                simulateJobExecution(job)
            }

            job
        } catch (e: Exception) {
            _error.value = "Failed to submit job: ${e.message}"
            null
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun getJobStatus(jobId: String): BridgeJob? {
        return _currentJob.value?.takeIf { it.id == jobId }
            ?: _jobHistory.value.find { it.id == jobId }
    }

    suspend fun getJobResults(jobId: String): BridgeJobResults? {
        return getJobStatus(jobId)?.results
    }

    suspend fun getNoiseData(backendName: String) {
        _realTimeNoiseData.value = generateNoiseData()
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
        _isConnected.value = false
        stopNoiseMonitoring()
    }

    suspend fun submitCircuit(circuit: QuantumCircuit, tier: ExecutionTier = _currentTier.value): BridgeJob {
        if (!_isConnected.value) {
            throw IllegalStateException("Not connected to QuantumBridge")
        }

        if (circuit.numberOfQubits > tier.maxQubits) {
            throw IllegalArgumentException(
                "Circuit requires ${circuit.numberOfQubits} qubits but tier allows max ${tier.maxQubits}"
            )
        }

        if (_remainingCredits.value <= 0) {
            throw IllegalStateException("No remaining credits")
        }

        _isLoading.value = true

        val job = BridgeJob(
            id = "qb-${System.currentTimeMillis()}-${Random.nextInt(1000)}",
            circuitData = circuit.toQASM(),
            status = BridgeJobStatus.QUEUED,
            estimatedTime = calculateEstimatedTime(circuit),
            queuePosition = Random.nextInt(1, 10)
        )

        _currentJob.value = job
        _remainingCredits.value = _remainingCredits.value - 1

        // Simulate job execution
        scope.launch {
            simulateJobExecution(job)
        }

        _isLoading.value = false
        return job
    }

    private suspend fun simulateJobExecution(job: BridgeJob) {
        // Move to running
        delay(1000)
        updateJobStatus(job.id, BridgeJobStatus.RUNNING)

        // Start noise monitoring
        startNoiseMonitoring(job)

        // Simulate execution
        delay(2000 + Random.nextLong(1000))

        // Complete with results
        val results = generateMockResults(job)
        completeJob(job.id, results)

        stopNoiseMonitoring()
    }

    private fun updateJobStatus(jobId: String, status: BridgeJobStatus) {
        _currentJob.value = _currentJob.value?.copy(status = status)
    }

    private fun completeJob(jobId: String, results: BridgeJobResults) {
        val completedJob = _currentJob.value?.copy(
            status = BridgeJobStatus.COMPLETED,
            completedAt = System.currentTimeMillis(),
            results = results
        )
        _currentJob.value = completedJob

        completedJob?.let { job ->
            _jobHistory.value = listOf(job) + _jobHistory.value.take(49)
        }
    }

    private fun generateMockResults(job: BridgeJob): BridgeJobResults {
        // Generate realistic mock measurements
        val measurements = mutableMapOf<String, Int>()
        val numQubits = job.circuitData?.lines()?.count { it.contains("qreg") }?.let {
            Regex("\\[(\\d+)\\]").find(job.circuitData)?.groupValues?.get(1)?.toIntOrNull()
        } ?: 2

        val totalShots = 1000
        val numOutcomes = 1 shl numQubits

        // Bell state-like distribution
        val outcome1 = "0".repeat(numQubits)
        val outcome2 = "1".repeat(numQubits)
        measurements[outcome1] = totalShots / 2 + Random.nextInt(-50, 50)
        measurements[outcome2] = totalShots - measurements[outcome1]!!

        return BridgeJobResults(
            measurements = measurements,
            fidelity = 0.98 + Random.nextDouble() * 0.015,
            executionTimeMs = 2000 + Random.nextLong(500),
            coherenceTimeSeconds = 1.8 + Random.nextDouble() * 0.4,
            atomReplenishments = Random.nextInt(0, 3)
        )
    }

    private fun calculateEstimatedTime(circuit: QuantumCircuit): Int {
        // Estimate based on circuit complexity
        val baseTime = 2 // seconds
        val depthFactor = circuit.depth * 0.1
        val qubitFactor = circuit.numberOfQubits * 0.05
        return (baseTime + depthFactor + qubitFactor).toInt()
    }

    fun startNoiseMonitoring(job: BridgeJob) {
        noiseMonitoringJob?.cancel()
        noiseMonitoringJob = scope.launch {
            while (isActive) {
                _realTimeNoiseData.value = generateNoiseData()
                delay(NOISE_MONITORING_INTERVAL_MS)
            }
        }
    }

    fun stopNoiseMonitoring() {
        noiseMonitoringJob?.cancel()
        noiseMonitoringJob = null
        _realTimeNoiseData.value = null
    }

    private fun generateNoiseData(): RealTimeNoiseData {
        val qubitNoiseMap = (0 until 4).associate { qubit ->
            qubit.toString() to QubitNoiseLevel(
                dephasing = 0.001 + Random.nextDouble() * 0.002,
                relaxation = 0.0005 + Random.nextDouble() * 0.001,
                gateError = 0.003 + Random.nextDouble() * 0.002,
                status = if (Random.nextDouble() > 0.95) "warning" else "healthy"
            )
        }

        return RealTimeNoiseData(
            qubitNoiseMap = qubitNoiseMap,
            overallFidelity = 0.995 + Random.nextDouble() * 0.004,
            coherenceRemaining = 0.8 + Random.nextDouble() * 0.2,
            atomLossRate = 0.0001 + Random.nextDouble() * 0.0002,
            replenishmentRate = 20.0 + Random.nextDouble() * 10.0
        )
    }

    suspend fun cancelJob(jobId: String) {
        _currentJob.value?.let { job ->
            if (job.id == jobId && !job.status.isTerminal) {
                _currentJob.value = job.copy(status = BridgeJobStatus.CANCELLED)
                stopNoiseMonitoring()
            }
        }
    }

    suspend fun checkHardwareStatus() {
        // Simulate hardware status check
        _hardwareStatus.value = HardwareStatus(
            isOnline = true,
            queueLength = Random.nextInt(5, 20),
            estimatedWaitSeconds = Random.nextInt(30, 300),
            currentFidelity = 0.998 + Random.nextDouble() * 0.001
        )
    }

    fun hasFeature(feature: PremiumFeature): Boolean {
        return when (feature.requiredTier()) {
            ExecutionTier.FREE -> true
            ExecutionTier.PRO -> _currentTier.value in listOf(ExecutionTier.PRO, ExecutionTier.PREMIUM)
            ExecutionTier.PREMIUM -> _currentTier.value == ExecutionTier.PREMIUM
        }
    }

    fun clearError() {
        _error.value = null
    }
}

package com.ryancasler.solscan.core

//https://docs.solana.com/introduction#what-are-sols
// The system may perform micropayments of fractional SOLs, which are called lamports.
// They are named in honor of Solana's biggest technical influence, Leslie Lamport.
// A lamport has a value of 0.000000001 SOL.

typealias Lamports = Long
const val solPerLamport: Double = 0.000000001
fun Lamports.toSol() = this * solPerLamport

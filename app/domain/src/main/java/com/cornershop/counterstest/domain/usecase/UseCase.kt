package com.cornershop.counterstest.domain.usecase

interface UseCase<ParamsType, ReturnType> {
    operator fun invoke(params: ParamsType): ReturnType
}

interface SuspendedUseCase<ParamsType, ReturnType> {
    suspend operator fun invoke(params: ParamsType): ReturnType
}

object NoParams

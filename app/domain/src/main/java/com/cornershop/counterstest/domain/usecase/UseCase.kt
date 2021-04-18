package com.cornershop.counterstest.domain.usecase

interface UseCase<ParamsType, ReturnType> {
    suspend operator fun invoke(params: ParamsType): ReturnType
}

object NoParams
